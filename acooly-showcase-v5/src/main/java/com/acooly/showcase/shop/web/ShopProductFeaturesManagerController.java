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
import com.acooly.showcase.shop.entity.ShopProductFeatures;
import com.acooly.showcase.shop.service.ShopProductFeaturesService;

import com.google.common.collect.Maps;

/**
 * 商品特性表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Controller
@RequestMapping(value = "/manage/shop/shopProductFeatures")
public class ShopProductFeaturesManagerController extends AbstractJsonEntityController<ShopProductFeatures, ShopProductFeaturesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopProductFeaturesService shopProductFeaturesService;







}
