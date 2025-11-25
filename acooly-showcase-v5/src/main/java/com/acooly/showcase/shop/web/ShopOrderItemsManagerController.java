/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-01
*/
package com.acooly.showcase.shop.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopOrderItems;
import com.acooly.showcase.shop.service.ShopOrderItemsService;

import com.google.common.collect.Maps;

/**
 * shop_order_items 管理控制器
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Controller
@RequestMapping(value = "/manage/shop/shopOrderItems")
public class ShopOrderItemsManagerController extends AbstractJsonEntityController<ShopOrderItems, ShopOrderItemsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopOrderItemsService shopOrderItemsService;







}
