/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.config.FrameworkPropertiesHolder;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.enums.SecurityErrorCode;
import com.acooly.module.security.service.UserService;
import com.acooly.module.security.utils.ShiroUtils;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.shop.entity.ShopTeam;
import com.acooly.showcase.shop.service.ShopTeamService;
import com.acooly.showcase.shop.utils.AESUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopUsers;
import com.acooly.showcase.shop.service.ShopUsersService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopUsers")
public class ShopUsersManagerController extends AbstractJsonEntityController<ShopUsers, ShopUsersService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopUsersService shopUsersService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShopTeamService shopTeamService;


	@Override
	public JsonListResult<ShopUsers> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopUsers> result = new JsonListResult();
		this.allow(request, response, MappingMethod.list);

		try {
			result.appendData(this.referenceData(request));
			PageInfo<ShopUsers> pageInfo = this.doList(request, response);
			List<ShopUsers> pageResults = pageInfo.getPageResults();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			List<ShopTeam> shopTeams = shopTeamService.getAll();
			if (CollectionUtils.isEmpty(shopTeams)) {
				// 没有团队数据，统一给默认值
				pageResults.forEach(item -> item.setTeamName("admin"));
			}
			Map<Long, String> teamMap = shopTeams.stream()
					.filter(t -> t.getId() != null)
					.collect(Collectors.toMap(
							ShopTeam::getId,
							ShopTeam::getTeamName,
							(a, b) -> a   // 防止重复 key
					));
			pageResults.forEach(s-> {
				s.setPassword("********");
				String teamName = s.getTeamId() == null
						? "admin"
						: teamMap.getOrDefault(s.getTeamId() , "admin");

				s.setTeamName(teamName);
			});
			result.setTotal(pageInfo.getTotalCount());
			result.setRows(pageInfo.getPageResults());
			result.setHasNext(pageInfo.hasNext());
			result.setPageNo(pageInfo.getCurrentPage());
			result.setPageSize(pageInfo.getCountOfCurrentPage());
		} catch (Exception var5) {
			this.handleException(result, "分页查询", var5);
		}

		return result;
	}


	@RequestMapping("changePasswd")
	public String changePasswd(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.allow(request, response, MappingMethod.update);
		try {
			model.addAllAttributes(this.referenceData(request));
			String id = request.getParameter("id");
			ShopUsers entity = shopUsersService.get(Long.valueOf(id));
			model.addAttribute(this.getEntityName(), entity);
			this.onEdit(request, response, model, entity);
		} catch (Exception var5) {
			log.warn(this.getExceptionMessage("edit", var5), var5);
			this.handleException("编辑", var5, request);
		}

		// 返回 FTL 文件，不带 /WEB-INF/jsp，也不带 .jsp 后缀
		String editView1 = this.getEditView();
		return StringUtils.isNotBlank(this.editView) ? this.editView : this.getRequestMapperValue() + "Change";
	}


	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("SHOP_PASSWORD_REGEX", FrameworkPropertiesHolder.get().getPasswordStrength().getRegexForJs());
		model.put("SHOP_PASSWORD_ERROR", FrameworkPropertiesHolder.get().getPasswordStrength().getDetail());
	}


	@RequestMapping({"shopUsersCn"})
	@ResponseBody
	public JsonResult shopUsersCn(HttpServletRequest request, HttpServletResponse response, Model model) {
		String newPassword = request.getParameter("newPassword");
		String adminPassword = request.getParameter("adminPassword");
		JsonResult result = new JsonResult();

		try {
//			FrameworkPropertiesHolder.get().getPasswordStrength().verify(newPassword);
			User admin = (User)this.userService.get(ShiroUtils.getCurrentUser().getId());
			if (!userService.validatePassword(admin, adminPassword)) {
				log.warn("管理员修改密码 认证失败 operator: {}", admin.getUsername());
				throw new BusinessException(SecurityErrorCode.ADMIN_PASSWORD_AUTH_FAIL, "当前用户密码错误");
			}
			//修改shop_user密码
			ShopUsers shopUsers = this.loadEntity(request);
			String encrypt = AESUtil.encrypt(shopUsers.getPassword());
			shopUsers.setPassword(encrypt);
			int i = shopUsersService.changePassword(shopUsers);

		} catch (Exception var9) {
//			this.handleException(result, "修改电商用户密码", var9);
		}
		result.setMessage("密码修改成功");
		result.setCode("200");
		result.setSuccess(true);
		return result;
	}


}
