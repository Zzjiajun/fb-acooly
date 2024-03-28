/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-25
*/
package com.acooly.showcase.daliy.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.DmRegion;
import com.acooly.showcase.daliy.service.DmRegionService;

import com.google.common.collect.Maps;

/**
 * dm_region 管理控制器
 *
 * @author acooly
 * @date 2024-03-25 12:05:46
 */
@Controller
@RequestMapping(value = "/manage/link/dmRegion")
public class DmRegionManagerController extends AbstractJsonEntityController<DmRegion, DmRegionService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmRegionService dmRegionService;







}
