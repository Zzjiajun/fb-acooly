/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-07
*/
package com.acooly.showcase.shop.web;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.security.domain.User;
import com.acooly.showcase.shop.utils.CouponCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopCoupon;
import com.acooly.showcase.shop.service.ShopCouponService;

import com.google.common.collect.Maps;

/**
 * shop_coupon 管理控制器
 *
 * @author acooly
 * @date 2025-11-07 17:01:53
 */
@Controller
@RequestMapping(value = "/manage/shop/shopCoupon")
public class ShopCouponManagerController extends AbstractJsonEntityController<ShopCoupon, ShopCouponService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopCouponService shopCouponService;


	@Override
	protected ShopCoupon onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopCoupon entity, boolean isCreate) throws Exception {
		if (isCreate){
			User principal = (User) SecurityUtils.getSubject().getPrincipal();
			entity.setCreatedBy(principal.getRealName());
			//生成单独的code
			String coupon = CouponCodeUtil.generateCoupon("SALE");
//			BigDecimal percent = new BigDecimal(String.valueOf(entity.getDiscountValue())).divide(new BigDecimal("100"));
//			entity.setDiscountValue(percent);
			entity.setUsedCount(0);
			entity.setCode(coupon);
		}
		return entity;
	}
}
