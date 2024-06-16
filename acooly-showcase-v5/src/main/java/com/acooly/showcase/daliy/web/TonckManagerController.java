/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-25
*/
package com.acooly.showcase.daliy.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.Tonck;
import com.acooly.showcase.daliy.service.TonckService;

import com.google.common.collect.Maps;

/**
 * tonck 管理控制器
 *
 * @author acooly
 * @date 2024-04-25 03:17:48
 */
@Controller
@RequestMapping(value = "/manage/link/tonck")
public class TonckManagerController extends AbstractJsonEntityController<Tonck, TonckService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private TonckService tonckService;







}
