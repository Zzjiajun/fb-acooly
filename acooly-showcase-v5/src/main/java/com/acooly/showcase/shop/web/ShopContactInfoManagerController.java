/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-01
*/
package com.acooly.showcase.shop.web;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.shop.entity.ShopTeam;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;
import com.acooly.showcase.shop.service.ShopTeamService;
import com.acooly.showcase.shop.service.ShopTeamUserMappingService;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopContactInfo;
import com.acooly.showcase.shop.service.ShopContactInfoService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.NameList;

/**
 * shop_contact_info 管理控制器
 *
 * @author acooly
 * @date 2025-12-01 20:10:49
 */
@Controller
@RequestMapping(value = "/manage/shop/shopContactInfo")
public class ShopContactInfoManagerController extends AbstractJsonEntityController<ShopContactInfo, ShopContactInfoService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopContactInfoService shopContactInfoService;

	@Autowired
	private RedisShopUtil redisUtil;
	@Autowired
	private ShopTeamService shopTeamService;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private ShopTeamUserMappingService shopTeamUserMappingService;
	private static final List<String> CONTACT_TYPES = Arrays.asList(
			"Address", "Phone", "Email", "Line", "facebook",
			"whatsapp", "instagram", "twitter", "telegram", "tiktok"
	);

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		User user = currentUser();
		boolean admin = isAdmin(user);
		Map<Long, String> teamMap = shopTeamService.getAll()
				.stream()
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName
				));
		List<String> availableContactTypes = new ArrayList<>(CONTACT_TYPES);
		if (!admin) {
			Long teamId = getUserTeamId(user);
			if (teamId == null) {
				teamMap.clear(); // 无团队 → 不显示任何团队
				availableContactTypes.clear();
			} else {
				String teamName = teamMap.get(teamId);
				teamMap.clear();
				teamMap.put(teamId, teamName);
				Map<String, Object> params = Maps.newHashMap();
				params.put("EQ_teamId", teamId);
				List<ShopContactInfo> infoList = this.getEntityService().query(params, null);
				if (CollectionUtils.isNotEmpty(infoList)) {
					// 已存在的联系方式类型
					Set<String> existedTypes = infoList.stream()
							.map(ShopContactInfo::getName)
							.filter(Objects::nonNull)
							.collect(Collectors.toSet());
					availableContactTypes.removeIf(existedTypes::contains);
				}
			}
		}
		//查看所属团队创建了哪个联系方式
		model.put("isAdmin", admin);
		model.put("contactTypes", availableContactTypes);
		model.put("shopTeamMap", teamMap);
	}

	@Override
	protected PageInfo<ShopContactInfo> doList(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User user = currentUser();
		if (!isAdmin(user)) {
			Long teamId = getUserTeamId(user);
			if (teamId == null) {
				// ❗ 无团队 → 强制无数据（安全兜底）
				searchParams.put("EQ_id", -1L);
			} else {
				searchParams.put("EQ_teamId", teamId);
			}
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}


	@Override
	public JsonResult upJson(HttpServletRequest request, HttpServletResponse response) {
		this.allow(request, response, MappingMethod.update);
		JsonResult result = new JsonResult();
		try {
			// 1. 获取当前记录的ID
			String id = request.getParameter("id");
			if (id == null || id.trim().isEmpty()) {
				throw new BusinessException("记录ID不能为空");
			}

			// 2. 获取当前记录
			ShopContactInfo current = shopContactInfoService.get(Long.parseLong(id));
			if (current == null) {
				throw new BusinessException("记录不存在");
			}

			// 3. 查询所有记录，在内存中过滤和排序
			// 因为框架不支持 LIMIT 和 ORDERBY_ 参数，所以使用内存处理
			List<ShopContactInfo> allList = shopContactInfoService.getAll();
			
			// 过滤：sortTime < 当前sortTime，且不为null
			List<ShopContactInfo> candidates = allList.stream()
				.filter(item -> item.getSortTime() != null 
							&& current.getSortTime() != null
							&& item.getSortTime() < current.getSortTime())
				.sorted(Comparator.comparing(ShopContactInfo::getSortTime).reversed())
				.collect(Collectors.toList());

			if (candidates == null || candidates.isEmpty()) {
				// 已经是第一条，无法上移
				result.setMessage("已经是第一条，无法上移");
				return result;
			}

			ShopContactInfo previous = candidates.get(0);

			// 4. 交换sortTime值
			Integer tempSortTime = current.getSortTime();
			current.setSortTime(previous.getSortTime());
			previous.setSortTime(tempSortTime);

			// 5. 更新两条记录
			shopContactInfoService.update(current);
			shopContactInfoService.update(previous);
			result.setMessage("上移成功");

		} catch (Exception e) {
			this.handleException(result, "上移", e);
		}
		return result;
	}


	@RequestMapping({"/downJson"})
	@ResponseBody
	public JsonEntityResult<ShopContactInfo> downJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopContactInfo> result = new JsonEntityResult<>();
		this.allow(request, response, MappingMethod.update);

		try {
			// 1. 获取当前记录的ID
			String id = request.getParameter("id");
			if (id == null || id.trim().isEmpty()) {
				throw new BusinessException("记录ID不能为空");
			}
			// 2. 获取当前记录
			ShopContactInfo current = shopContactInfoService.get(Long.parseLong(id));
			if (current == null) {
				throw new BusinessException("记录不存在");
			}
			// 3. 查询所有记录，在内存中过滤和排序
			// 因为框架不支持 LIMIT 和 ORDERBY_ 参数，所以使用内存处理
			List<ShopContactInfo> allList = shopContactInfoService.getAll();
			
			// 过滤：sortTime > 当前sortTime，且不为null
			List<ShopContactInfo> candidates = allList.stream()
				.filter(item -> item.getSortTime() != null 
							&& current.getSortTime() != null
							&& item.getSortTime() > current.getSortTime())
				.sorted(Comparator.comparing(ShopContactInfo::getSortTime))
				.collect(Collectors.toList());
			
			if (candidates == null || candidates.isEmpty()) {
				// 已经是最后一条，无法下移
				result.setMessage("已经是最后一条，无法下移");
				return result;
			}
			ShopContactInfo next = candidates.get(0);
			// 4. 交换sortTime值
			Integer tempSortTime = current.getSortTime();
			current.setSortTime(next.getSortTime());
			next.setSortTime(tempSortTime);
			// 5. 更新两条记录
			shopContactInfoService.update(current);
			shopContactInfoService.update(next);
			result.setEntity(current);
			result.setMessage("下移成功");
		} catch (Exception e) {
			this.handleException(result, "下移", e);
		}

		return result;
	}

	@Override
	protected ShopContactInfo onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopContactInfo entity, boolean isCreate) throws Exception {
		// 创建时自动设置 sortTime
		if (entity != null) {
			if (entity.getValue() != null && !entity.getValue().trim().isEmpty()){
				String decodedValue = StringEscapeUtils.unescapeHtml4(entity.getValue());
				entity.setValue(decodedValue);
			}
		}
		if (isCreate && entity.getSortTime() == null) {
			// 查询所有记录，找到最大的 sortTime
			List<ShopContactInfo> allList = shopContactInfoService.getAll();
			// 找到最大的 sortTime 值
			Integer maxSortTime = allList.stream()
				.filter(item -> item.getSortTime() != null)
				.map(ShopContactInfo::getSortTime)
				.max(Comparator.naturalOrder())
				.orElse(0); // 如果没有记录或所有记录的 sortTime 都是 null，则默认为 0
			// 设置为最大值 + 1（新记录排在最后）
			entity.setSortTime(maxSortTime + 1);
		}
		redisUtil.del("site:contact:active");
		redisUtil.deleteHotResultKeys("contact:info:team:");
		return super.onSave(request, response, model, entity, isCreate);
	}

	@Override
	public JsonListResult<ShopContactInfo> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopContactInfo> result = super.listJson(request, response);
		List<ShopContactInfo> rows = result.getRows();
		List<ShopTeam> shopTeams = shopTeamService.getAll();
		if (CollectionUtils.isEmpty(shopTeams)) {
			// 没有团队数据，统一给默认值
			rows.forEach(item -> item.setTeamName("admin"));
			return result;
		}
		Map<Long, String> teamMap = shopTeams.stream()
				.filter(t -> t.getId() != null)
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName,
						(a, b) -> a   // 防止重复 key
				));

		rows.forEach(item -> {
			fillTeamName(item, teamMap);
		});

		return result;
	}

	/**
	 * 重写updateJson方法，在更新后填充teamName
	 */
	@Override
	public JsonEntityResult<ShopContactInfo> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopContactInfo> result = super.updateJson(request, response);
		// 填充teamName
		if (result.getEntity() != null) {
			fillTeamName(result.getEntity());
		}
		return result;
	}

	/**
	 * 填充teamName的辅助方法（单个实体）
	 */
	private void fillTeamName(ShopContactInfo contactInfo) {
		if (contactInfo == null || contactInfo.getTeamId() == null) {
			contactInfo.setTeamName("admin");
			return;
		}
		List<ShopTeam> shopTeams = shopTeamService.getAll();
		if (CollectionUtils.isEmpty(shopTeams)) {
			contactInfo.setTeamName("admin");
			return;
		}
		Map<Long, String> teamMap = shopTeams.stream()
				.filter(t -> t.getId() != null)
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName,
						(a, b) -> a   // 防止重复 key
				));
		fillTeamName(contactInfo, teamMap);
	}

	/**
	 * 填充teamName的辅助方法（使用已有的teamMap）
	 */
	private void fillTeamName(ShopContactInfo contactInfo, Map<Long, String> teamMap) {
		if (contactInfo == null) {
			return;
		}
		Long teamId = contactInfo.getTeamId() != null ? Long.valueOf(contactInfo.getTeamId()) : null;
		String teamName = teamId == null
				? "admin"
				: teamMap.getOrDefault(teamId, "admin");
		contactInfo.setTeamName(teamName);
	}


	private User currentUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	private boolean isAdmin(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("EQ_userName", user.getUsername());
		return CollectionUtils.isNotEmpty(permissionsService.query(params, null));
	}

	private Long getUserTeamId(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("EQ_userId", user.getId());

		List<ShopTeamUserMapping> list =
				shopTeamUserMappingService.query(params, null);

		return CollectionUtils.isEmpty(list) ? null : list.get(0).getTeamId();
	}
}
