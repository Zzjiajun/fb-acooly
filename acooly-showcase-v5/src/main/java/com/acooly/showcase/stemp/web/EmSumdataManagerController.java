/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.stemp.entity.EmSumdata;
import com.acooly.showcase.stemp.service.EmSumdataService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * em_sumdata 管理控制器
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Controller
@RequestMapping(value = "/manage/stemp/emSumdata")
public class EmSumdataManagerController extends AbstractJsonEntityController<EmSumdata, EmSumdataService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private EmSumdataService emSumdataService;










}
