/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-22
*/
package com.acooly.showcase.daliy.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.service.PermissionsService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.DmDomain;
import com.acooly.showcase.daliy.service.DmDomainService;

import com.google.common.collect.Maps;

/**
 * dm_domain 管理控制器
 *
 * @author acooly
 * @date 2024-03-22 17:23:50
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/dmDomain")
public class DmDomainManagerController extends AbstractJsonEntityController<DmDomain, DmDomainService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmDomainService dmDomainService;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private UserService userService;
	@Autowired
	private DmCenterService dmCenterService;

	@Override
	protected PageInfo<DmDomain> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_userName", principal.getUsername());
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected DmDomain onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmDomain entity, boolean isCreate) throws Exception {
		DmDomain dmDomain = super.onSave(request, response, model, entity, isCreate);
		if (isCreate){
			User principal = (User) SecurityUtils.getSubject().getPrincipal();
			entity.setUserName(principal.getUsername());
		}else {
			DmDomain domainOld = this.getEntityService().get(entity.getId());
			if (!domainOld.getLink().equals(entity.getLink())){
				String[] split = domainOld.getDomainName().split("/");
				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("EQ_domain",split[0]);
				map1.put("EQ_secondaryDomain",split[1]);
				List<DmCenter> list = dmCenterService.query(map1, null);
				DmCenter dmCenter = list.get(0);
				dmCenter.setLink(entity.getLink());
				dmCenterService.update(dmCenter);
			}
		}
		return dmDomain;
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query = userService.query(mapQuery, null);
		Map<String, String> mapName = query.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("mapName", mapName);
	}
}
