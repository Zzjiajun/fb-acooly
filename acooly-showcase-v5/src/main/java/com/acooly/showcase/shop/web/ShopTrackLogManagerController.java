/*
* acooly.cn Inc.
* Copyright (c) 2026 All Rights Reserved.
* create by acooly
* date:2026-01-04
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopTrackLog;
import com.acooly.showcase.shop.service.ShopTrackLogService;

import com.google.common.collect.Maps;

/**
 * 访问行为日志表（按日期分区，建议按月归档） 管理控制器
 *
 * @author acooly
 * @date 2026-01-04 19:04:49
 */
@Controller
@RequestMapping(value = "/manage/shop/shopTrackLog")
public class ShopTrackLogManagerController extends AbstractJsonEntityController<ShopTrackLog, ShopTrackLogService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopTrackLogService shopTrackLogService;







}
