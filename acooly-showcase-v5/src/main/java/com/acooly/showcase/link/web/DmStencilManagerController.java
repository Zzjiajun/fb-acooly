/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-26
*/
package com.acooly.showcase.link.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmStencil;
import com.acooly.showcase.link.service.DmStencilService;

import com.google.common.collect.Maps;

/**
 * dm_stencil 管理控制器
 *
 * @author acooly
 * @date 2024-04-26 04:43:38
 */
@Controller
@RequestMapping(value = "/manage/link/dmStencil")
public class DmStencilManagerController extends AbstractJsonEntityController<DmStencil, DmStencilService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmStencilService dmStencilService;







}
