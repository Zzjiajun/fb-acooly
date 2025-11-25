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







}
