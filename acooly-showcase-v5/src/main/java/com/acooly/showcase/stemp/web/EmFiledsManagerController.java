/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.stemp.entity.EmFileds;
import com.acooly.showcase.stemp.service.EmFiledsService;

import com.google.common.collect.Maps;

/**
 * em_fileds 管理控制器
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Controller
@RequestMapping(value = "/manage/stemp/emFileds")
public class EmFiledsManagerController extends AbstractJsonEntityController<EmFileds, EmFiledsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private EmFiledsService emFiledsService;







}
