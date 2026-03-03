/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-12
*/
package com.acooly.showcase.shop.web;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.entity.DmDomain;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.link.entity.Board;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.shop.entity.ShopContactInfo;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;
import com.acooly.showcase.shop.service.ShopTeamUserMappingService;
import com.acooly.showcase.shop.utils.TeamCodeGenerator;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopTeam;
import com.acooly.showcase.shop.service.ShopTeamService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * shop_team 管理控制器
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
@Controller
@RequestMapping(value = "/manage/shop/shopTeam")
public class ShopTeamManagerController extends AbstractJsonEntityController<ShopTeam, ShopTeamService> {


	{
		allowMapping = "*";
	}
	private static final String CHAR_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int CODE_LENGTH = 6; // 可根据需求调整
	private static final SecureRandom RANDOM = new SecureRandom();

	@SuppressWarnings("unused")
	@Autowired
	private ShopTeamService shopTeamService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShopTeamUserMappingService shopTeamUserMappingService;
	@Value("${acooly.shop.link}")
	private String shopLink;
	@Value("${acooly.shop.registerUrl}")
	private String shopRegisterUrl;
	@Autowired
	private PermissionsService permissionsService;


	@Override
	protected PageInfo<ShopTeam> doList(
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
				searchParams.put("EQ_id", teamId);
			}
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	/**
	 * 查询可选择的用户列表（未绑定团队的用户 + 当前团队已绑定的用户）
	 */
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		// 查询所有后台用户
		List<User> allUsers = userService.getAll();
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		boolean admin = isAdmin(principal);
		// 查询所有已绑定团队的用户ID
		List<ShopTeamUserMapping> allMappings = shopTeamUserMappingService.getAll();
		List<Long> boundUserIds = allMappings.stream()
				.map(ShopTeamUserMapping::getUserId)
				.collect(Collectors.toList());
		
		// 获取当前团队ID（编辑模式下）
		String teamIdStr = request.getParameter("id");
		List<Long> currentTeamUserIds = new ArrayList<>();
		if (teamIdStr != null && !teamIdStr.isEmpty()) {
			try {
				Long teamId = Long.parseLong(teamIdStr);
				// 查询当前团队已绑定的用户
				List<ShopTeamUserMapping> currentTeamMappings = shopTeamUserMappingService.findByTeamId(teamId);
				currentTeamUserIds = currentTeamMappings.stream()
						.map(ShopTeamUserMapping::getUserId)
						.collect(Collectors.toList());
			} catch (NumberFormatException e) {
				// 忽略解析错误
			}
		}
		
		// 过滤出可选择的用户：未绑定团队的用户 + 当前团队已绑定的用户
		List<Long> finalCurrentTeamUserIds = currentTeamUserIds;
		List<User> availableUsers = allUsers.stream()
				.filter(user -> !boundUserIds.contains(user.getId()) || finalCurrentTeamUserIds.contains(user.getId()))
				.collect(Collectors.toList());
		
		// 将用户列表和当前团队已绑定的用户ID列表放入model，供前端使用
		model.put("availableUsers", availableUsers);
		model.put("isAdmin", admin);
		model.put("currentTeamUserIds", currentTeamUserIds);
	}

	/**
	 * 保存团队时处理用户选择和校验
	 * 注意：这里只做校验和设置属性，不调用save，由框架自动保存
	 */
	@Override
	protected ShopTeam onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopTeam entity, boolean isCreate) throws Exception {
		// 1. 获取用户ID列表（从请求参数中获取）
		String[] userIdArray = request.getParameterValues("userIds");
		if (userIdArray == null || userIdArray.length == 0) {
			throw new BusinessException("请至少选择一个用户");
		}
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		entity.setCreator(principal.getUsername());
		
		// 2. 获取当前团队ID（更新模式下从request参数获取）
		Long currentTeamId = null;
		if (!isCreate) {
			String teamIdStr = request.getParameter("id");
			if (teamIdStr != null && !teamIdStr.isEmpty()) {
				try {
					currentTeamId = Long.parseLong(teamIdStr);
				} catch (NumberFormatException e) {
					// 忽略解析错误
				}
			}
			// 如果从request获取不到，尝试从entity获取
			if (currentTeamId == null && entity.getId() != null) {
				currentTeamId = entity.getId();
			}
		}
		
		// 3. 校验用户并收集用户ID列表
		List<Long> userIds = new ArrayList<>();
		List<String> errorUsers = new ArrayList<>();
		
		for (String userIdStr : userIdArray) {
			try {
				Long userId = Long.parseLong(userIdStr);
				
				// 检查用户是否已绑定其他团队
				ShopTeamUserMapping existingMapping = shopTeamUserMappingService.uniqueByUserId(userId);
				if (existingMapping != null) {
					// 如果是更新模式，且用户绑定的是当前团队，则允许
					if (!isCreate && currentTeamId != null && existingMapping.getTeamId().equals(currentTeamId)) {
						userIds.add(userId);
						continue;
					}
					// 否则，用户已绑定其他团队，报错
					User user = userService.get(userId);
					String userName = user != null ? user.getUsername() : userIdStr;
					errorUsers.add(userName);
					continue;
				}
				
				userIds.add(userId);
			} catch (NumberFormatException e) {
				throw new BusinessException("用户ID格式错误: " + userIdStr);
			}
		}
		
		// 4. 如果有用户已绑定其他团队，抛出异常
		if (!errorUsers.isEmpty()) {
			throw new BusinessException("以下用户已绑定其他团队，无法重复绑定: " + String.join(", ", errorUsers));
		}
		
		if (isCreate) {
			// 5. 创建模式：生成团队分享链接
			String teamName = entity.getTeamName();
//			String urlSafe = toUrlSafe(teamName);
			String teamCode = TeamCodeGenerator.generateTeamCode();
			entity.setCode(teamCode);
			entity.setTeamLink(shopLink + "/?team="+teamCode);
			entity.setTeamRegisterLink(shopRegisterUrl + "/?team="+teamCode);
		}
		
		// 6. 将用户ID列表保存到request attribute，供saveJson/updateJson使用
		request.setAttribute("_teamUserIds", userIds);
		
		// 7. 设置userIds字段（用于存储用户ID列表）
		if (!userIds.isEmpty()) {
			String userIdsStr = userIds.stream()
					.map(String::valueOf)
					.collect(Collectors.joining(","));
			entity.setUserIds(userIdsStr);
		}
		
		return entity;
	}

	/**
	 * 重写saveJson方法，在保存后创建映射关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonEntityResult<ShopTeam> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopTeam> result = new JsonEntityResult<>();
		this.allow(request, response, MappingMethod.create);
		try {
			// 1. 调用doSave保存团队（框架会自动调用onSave进行校验和设置属性）
			ShopTeam shopTeam = this.doSave(request, response, (Model) null, true);
			
			// 2. 重要：在doSave返回后，实体已经保存到数据库，此时可以创建映射关系
			// 从request中获取用户ID列表
			@SuppressWarnings("unchecked")
			List<Long> userIds = (List<Long>) request.getAttribute("_teamUserIds");
			
			if (shopTeam != null && shopTeam.getId() != null && userIds != null && !userIds.isEmpty()) {
				Long teamId = shopTeam.getId();
				
				// 3. 创建映射关系
				for (Long userId : userIds) {
					ShopTeamUserMapping mapping = new ShopTeamUserMapping();
					mapping.setTeamId(teamId);
					mapping.setUserId(userId);
					shopTeamUserMappingService.save(mapping);
				}
				
				result.setEntity(shopTeam);
			} else {
				result.setEntity(shopTeam);
			}
		} catch (Exception e) {
			this.handleException(result, "新增", e);
		}
		String memberNames = buildMemberNames(result.getEntity().getUserIds());
		if (!StringUtils.isBlank(memberNames) ) {
			result.getEntity().setMemberNames(memberNames);
		}
		return result;
	}

	/**
	 * 重写updateJson方法，在更新后处理用户映射关系的变更
	 * 根据userIds的变化：增加新用户映射，删除取消选择的用户映射
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonEntityResult<ShopTeam> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopTeam> result = new JsonEntityResult<>();
		this.allow(request, response, MappingMethod.update);
		try {
			// 1. 获取当前团队ID
			String teamIdStr = request.getParameter("id");
			if (teamIdStr == null || teamIdStr.isEmpty()) {
				throw new BusinessException("团队ID不能为空");
			}
			Long teamId = Long.parseLong(teamIdStr);
			
			// 2. 查询当前团队已有的映射关系（保存映射对象，用于后续删除）
			List<ShopTeamUserMapping> existingMappings = shopTeamUserMappingService.findByTeamId(teamId);
			// 创建userId到mapping的映射，方便查找
			Map<Long, ShopTeamUserMapping> existingMappingMap = existingMappings.stream()
					.collect(Collectors.toMap(ShopTeamUserMapping::getUserId, mapping -> mapping, (k1, k2) -> k1));
			List<Long> existingUserIds = new ArrayList<>(existingMappingMap.keySet());
			
			// 3. 调用doSave更新团队（框架会自动调用onSave进行校验和设置属性）
			ShopTeam shopTeam = this.doSave(request, response, (Model) null, false);
			
			// 4. 重要：在doSave返回后，从request中获取新的用户ID列表
			@SuppressWarnings("unchecked")
			List<Long> newUserIds = (List<Long>) request.getAttribute("_teamUserIds");
			
			if (shopTeam != null && shopTeam.getId() != null && newUserIds != null) {
				// 5. 计算需要删除的映射（原有但不在新列表中的）
				List<Long> toDeleteUserIds = existingUserIds.stream()
						.filter(userId -> !newUserIds.contains(userId))
						.collect(Collectors.toList());
				
				// 6. 删除不再需要的映射关系（直接从existingMappings中删除，更准确）
				for (Long userId : toDeleteUserIds) {
					ShopTeamUserMapping mapping = existingMappingMap.get(userId);
					if (mapping != null) {
						shopTeamUserMappingService.removeById(mapping.getId());
					}
				}
				
				// 7. 计算需要新增的映射（新列表中有但原有中没有的）
				List<Long> toAddUserIds = newUserIds.stream()
						.filter(userId -> !existingUserIds.contains(userId))
						.collect(Collectors.toList());
				
				// 8. 创建新的映射关系
				for (Long userId : toAddUserIds) {
					ShopTeamUserMapping mapping = new ShopTeamUserMapping();
					mapping.setTeamId(teamId);
					mapping.setUserId(userId);
					shopTeamUserMappingService.save(mapping);
				}
				
				result.setEntity(shopTeam);
				result.setMessage("更新成功");
			} else {
				result.setEntity(shopTeam);
			}
		} catch (Exception e) {
			this.handleException(result, "更新", e);
		}
		String memberNames = buildMemberNames(result.getEntity().getUserIds());
		if (!StringUtils.isBlank(memberNames) ) {
			result.getEntity().setMemberNames(memberNames);
		}
		return result;
	}


	// 生成一个随机 team_code
	public static String generateTeamCode() {
		StringBuilder sb = new StringBuilder(CODE_LENGTH);
		for(int i = 0; i < CODE_LENGTH; i++) {
			sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
		}
		return sb.toString();
	}

	public static String toUrlSafe(String name) {
		StringBuilder sb = new StringBuilder();

		for (char c : name.toCharArray()) {
			if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) {
				// 中文转拼音
				String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
				if (pinyins != null && pinyins.length > 0) {
					sb.append(pinyins[0].replaceAll("\\d", "")); // 去掉声调
				}
			} else if (Character.isLetterOrDigit(c)) {
				// 英文和数字直接保留
				sb.append(c);
			} else {
				// 特殊字符统一替换为 -
				sb.append("-");
			}
		}

		// 合并连续的 '-'
		String urlSafe = sb.toString().replaceAll("-+", "-");

		// 去掉首尾的 '-'，并转小写
		urlSafe = urlSafe.replaceAll("^-|-$", "").toLowerCase();

		return urlSafe;
	}

	/**
	 * 重写listJson方法，添加用户名信息
	 */
	@Override
	public JsonListResult<ShopTeam> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopTeam> result = super.listJson(request, response);
		List<ShopTeam> rows = result.getRows();
		
		if (rows != null && !rows.isEmpty()) {
			// 查询所有用户，创建userId到用户名的映射
			List<User> allUsers = userService.getAll();
			Map<Long, String> userIdToUsernameMap = allUsers.stream()
					.collect(Collectors.toMap(User::getId, User::getUsername, (k1, k2) -> k1));
			
			// 为每个团队设置成员用户名
			for (ShopTeam team : rows) {
				if (team.getUserIds() != null && !team.getUserIds().isEmpty()) {
					String[] userIdArray = team.getUserIds().split(",");
					List<String> memberNames = new ArrayList<>();
					
					for (String userIdStr : userIdArray) {
						try {
							Long userId = Long.parseLong(userIdStr.trim());
							String username = userIdToUsernameMap.get(userId);
							if (username != null) {
								memberNames.add(username);
							}
						} catch (NumberFormatException e) {
							// 忽略无效的用户ID
						}
					}
					
					// 设置成员用户名列表（用逗号分隔）
					team.setMemberNames(String.join(", ", memberNames));
				}
			}
			
			result.setRows(rows);
		}
		
		return result;
	}

	private String buildMemberNames(String userIds) {
		// 查询所有用户，创建 userId -> username 映射
		List<User> allUsers = userService.getAll();
		if (CollectionUtils.isEmpty(allUsers)) {
			return "";
		}
		Map<Long, String> userIdToUsernameMap = allUsers.stream()
				.filter(u -> u.getId() != null)
				.collect(Collectors.toMap(
						User::getId,
						User::getUsername,
						(a, b) -> a
				));
		if (StringUtils.isBlank(userIds) ) {
			return "";
		}

		String[] userIdArray = userIds.split(",");
		List<String> memberNames = new ArrayList<>();

		for (String userIdStr : userIdArray) {
			if (StringUtils.isBlank(userIdStr)) {
				continue;
			}
			try {
				Long userId = Long.parseLong(userIdStr.trim());
				String username = userIdToUsernameMap.get(userId);
				if (StringUtils.isNotBlank(username)) {
					memberNames.add(username);
				}
			} catch (NumberFormatException e) {
				// 忽略非法 userId
			}
		}

		return String.join(", ", memberNames);
	}


	private User currentUser() {
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	private boolean isAdmin(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("EQ_userName", user.getUsername());
		return com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(permissionsService.query(params, null));
	}

	private Long getUserTeamId(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("EQ_userId", user.getId());

		List<ShopTeamUserMapping> list =
				shopTeamUserMappingService.query(params, null);

		return com.alibaba.dubbo.common.utils.CollectionUtils.isEmpty(list) ? null : list.get(0).getTeamId();
	}
}
