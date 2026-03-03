/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopUsers;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.service.ShopUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopReviews;
import com.acooly.showcase.shop.service.ShopReviewsService;

import com.google.common.collect.Maps;

/**
 * 评论表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Controller
@RequestMapping(value = "/manage/shop/shopReviews")
public class ShopReviewsManagerController extends AbstractJsonEntityController<ShopReviews, ShopReviewsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopReviewsService shopReviewsService;
	@Autowired
	private ShopProductsService shopProductsService;
	@Autowired
	private ShopUsersService shopUsersService;


	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<ShopProducts> all = shopProductsService.getAll();
		Map<Long, String> productMap = all.stream().collect(Collectors.toMap(ShopProducts::getId, ShopProducts::getName));
		List<ShopUsers> usersList = shopUsersService.getAll();
		Map<Long, String> userMap = usersList.stream().collect(Collectors.toMap(ShopUsers::getId, ShopUsers::getEmail));
		model.put("productMap", productMap);
		model.put("userMap", userMap);
	}
}
