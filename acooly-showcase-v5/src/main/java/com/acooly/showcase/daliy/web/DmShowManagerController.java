/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-25
 */
package com.acooly.showcase.daliy.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.DmRegion;
import com.acooly.showcase.daliy.entity.DmShow;
import com.acooly.showcase.daliy.service.DmRegionService;
import com.acooly.showcase.daliy.service.DmShowService;
import com.google.common.collect.Maps;
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
 * dm_show 管理控制器
 *
 * @author acooly
 * @date 2024-03-25 10:30:45
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/dmShow")
public class DmShowManagerController extends AbstractJsonEntityController<DmShow, DmShowService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmShowService dmShowService;
	@Autowired
	private DmRegionService dmRegionService;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<DmRegion> list = dmRegionService.getAll();
		List<String> strings = list.stream().map(DmRegion::getRegion).collect(Collectors.toList());
		List<DmShow> showList = dmShowService.getAll();
		Map<String, String> collected = showList.stream().collect(Collectors.toMap(user -> user.getRegion() + "  :  " + user.getSerialNumber(), DmShow::getSerialNumber));
		model.put("collected" ,collected);
		model.put("regionList",strings);
	}

	@RequestMapping(value = "showLink")
	public String transferOut(Model model, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Map<String, Object> map = Maps.newHashMap();
		DmShow dmShow = this.getEntityService().get(Long.valueOf(id));
		map.put("domain", "https://"+dmShow.getDomain());
		model.addAllAttributes(map);
		return "/manage/showcase/daily/showLink";
	}


	@RequestMapping(value = "showLink1")
	public String transferOut1(Model model, HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		Map<String, Object> map = Maps.newHashMap();
		map.put("domain", "https://"+name);
		model.addAllAttributes(map);
		return "/manage/showcase/daily/showLink";
	}

}
