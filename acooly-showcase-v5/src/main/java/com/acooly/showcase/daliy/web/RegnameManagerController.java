/*
 * acooly.cn Inc.
 * Copyright (c) 2023 All Rights Reserved.
 * create by acooly
 * date:2023-12-14
 */
package com.acooly.showcase.daliy.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.Regname;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RegnameService;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * dm_regname 管理控制器
 *
 * @author acooly
 * @date 2023-12-14 17:39:27
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/regname")
public class RegnameManagerController extends AbstractJsonEntityController<Regname, RegnameService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private RegnameService regnameService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionsService permissionsService;

	@Override
	protected PageInfo<Regname> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_name", principal.getUsername());
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
//		Map<String, Object> mapQuery = Maps.newHashMap();
//		mapQuery.put("EQ_userType", "2");
		List<User> query = userService.getAll();
		Map<String, String> mapName1 = query.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("mapName1", mapName1);
	}

	@Override
	protected Regname onSave(HttpServletRequest request, HttpServletResponse response, Model model, Regname entity, boolean isCreate) throws Exception {
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_username",entity.getName());
		List<User> query = userService.query(mapQuery, null);
		entity.setUserId(Math.toIntExact(query.get(0).getId()));
		return super.onSave(request, response, model, entity, isCreate);
	}
}
