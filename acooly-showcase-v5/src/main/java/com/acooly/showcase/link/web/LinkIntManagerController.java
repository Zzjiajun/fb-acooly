/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-28
*/
package com.acooly.showcase.link.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.service.DmConditionService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.LinkInt;
import com.acooly.showcase.link.service.LinkIntService;

import com.google.common.collect.Maps;

/**
 * link_int 管理控制器
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Controller
@RequestMapping(value = "/manage/link/linkInt")
public class LinkIntManagerController extends AbstractJsonEntityController<LinkInt, LinkIntService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private LinkIntService linkIntService;
	@Autowired
	private PermissionsService permissionsService;


	@Override
	protected PageInfo<LinkInt> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_userName",principal.getUsername());
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected LinkInt onSave(HttpServletRequest request, HttpServletResponse response, Model model, LinkInt entity, boolean isCreate) throws Exception {
		if (isCreate){
			User principal = (User) SecurityUtils.getSubject().getPrincipal();
			entity.setUserName(principal.getUsername());
		}
		return super.onSave(request, response, model, entity, isCreate);
	}
}
