/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-28
*/
package com.acooly.showcase.link.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.daliy.entity.DmDomain;
import com.acooly.showcase.daliy.entity.DmPixel;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.daliy.service.DmDomainService;
import com.acooly.showcase.daliy.service.DmPixelService;
import com.acooly.showcase.daliy.service.LinkService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.link.entity.LinkInt;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.LinkSrcs;
import com.acooly.showcase.link.service.LinkSrcsService;

import com.google.common.collect.Maps;

/**
 * link_srcs 管理控制器
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Controller
@RequestMapping(value = "/manage/link/linkSrcs")
public class LinkSrcsManagerController extends AbstractJsonEntityController<LinkSrcs, LinkSrcsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private LinkSrcsService linkSrcsService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private DmPixelService dmPixelService;


	@Override
	protected PageInfo<LinkSrcs> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
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
	protected LinkSrcs onSave(HttpServletRequest request, HttpServletResponse response, Model model, LinkSrcs entity, boolean isCreate) throws Exception {
		if (isCreate){
			User principal = (User) SecurityUtils.getSubject().getPrincipal();
			entity.setUserName(principal.getUsername());
		}
		return super.onSave(request, response, model, entity, isCreate);
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_holder", principal.getUsername());
		List<Link> query = linkService.query(mapQuery, null);
		List<String> list = query.stream().map(Link::getAccessAddress).collect(Collectors.toList());
//		List<String> list = query.stream().map(DmPixel::getDomain).collect(Collectors.toList());
		model.put("list" ,list);
	}
}
