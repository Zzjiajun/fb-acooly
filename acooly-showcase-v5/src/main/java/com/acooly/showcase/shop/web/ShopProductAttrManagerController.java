/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-22
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopProductAttr;
import com.acooly.showcase.shop.service.ShopProductAttrService;

import com.google.common.collect.Maps;

/**
 * 商品属性关联表 管理控制器
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Controller
@RequestMapping(value = "/manage/shop/shopProductAttr")
public class ShopProductAttrManagerController extends AbstractJsonEntityController<ShopProductAttr, ShopProductAttrService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopProductAttrService shopProductAttrService;







}
