/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-06-20
*/
package com.acooly.showcase.link.web;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.link.entity.DmCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmObserverPermission;
import com.acooly.showcase.link.service.DmObserverPermissionService;

import com.google.common.collect.Maps;

/**
 * dm_observer_permission 管理控制器
 *
 * @author acooly
 * @date 2025-06-20 21:45:07
 */
@Controller
@RequestMapping(value = "/manage/link/dmObserverPermission")
public class DmObserverPermissionManagerController extends AbstractJsonEntityController<DmObserverPermission, DmObserverPermissionService> {


	{
		allowMapping = "*";
	}
	private static final Logger logger = LoggerFactory.getLogger(AbstractStandardEntityController.class);

	@SuppressWarnings("unused")
	@Autowired
	private DmObserverPermissionService dmObserverPermissionService;
	@Autowired
	private DmCenterService dmCenterService;



	@RequestMapping({"editCenter"})
	public String editTwo(HttpServletRequest request, HttpServletResponse response, Model model) throws AccessDeniedException {
		this.allow(request, response, MappingMethod.update);
		try {
			model.addAllAttributes(this.referenceData(request));
			String id = request.getParameter("id");
			Map<String, Object> mapQuery = Maps.newHashMap();
			mapQuery.put("EQ_dmCenterId", id);
			List<DmObserverPermission> query = dmObserverPermissionService.query(mapQuery, null);
			if (query.size() == 0) {
				throw new AccessDeniedException("无法查找观察者中心");
			}
			DmObserverPermission entity = query.get(0);
			model.addAttribute("action", "edit");
			model.addAttribute(this.getEntityName(), entity);
			this.onEdit(request, response, model, entity);
		} catch (Exception var5) {
			logger.warn(this.getExceptionMessage("edit", var5), var5);
			this.handleException("编辑", var5, request);
		}

		String editView1 = this.getEditView();
		return this.getEditView();
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<User> allObservers = this.getEntityService().getAllObservers();
		Map<Long, String> collect = allObservers.stream().collect(Collectors.toMap(User::getId, User::getUsername));
		model.put("allObservers", collect);
	}
}
