/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopContactMessages;
import com.acooly.showcase.shop.service.ShopContactMessagesService;

import com.google.common.collect.Maps;

/**
 * 联系消息表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Controller
@RequestMapping(value = "/manage/shop/shopContactMessages")
public class ShopContactMessagesManagerController extends AbstractJsonEntityController<ShopContactMessages, ShopContactMessagesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopContactMessagesService shopContactMessagesService;







}
