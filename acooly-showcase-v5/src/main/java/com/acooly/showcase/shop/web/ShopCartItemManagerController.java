/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-01
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopCartItem;
import com.acooly.showcase.shop.service.ShopCartItemService;

import com.google.common.collect.Maps;

/**
 * 购物车表 管理控制器
 *
 * @author acooly
 * @date 2025-11-01 23:25:13
 */
@Controller
@RequestMapping(value = "/manage/shop/shopCartItem")
public class ShopCartItemManagerController extends AbstractJsonEntityController<ShopCartItem, ShopCartItemService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopCartItemService shopCartItemService;







}
