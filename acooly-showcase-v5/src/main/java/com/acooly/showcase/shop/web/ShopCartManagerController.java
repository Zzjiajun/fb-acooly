/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-01
*/
package com.acooly.showcase.shop.web;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.showcase.shop.dto.ShopCartItemDto;
import com.acooly.showcase.shop.entity.ShopCartItem;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopUsers;
import com.acooly.showcase.shop.service.ShopCartItemService;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.service.ShopUsersService;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopCart;
import com.acooly.showcase.shop.service.ShopCartService;

import com.google.common.collect.Maps;

/**
 * shop_cart 管理控制器
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopCart")
public class ShopCartManagerController extends AbstractJsonEntityController<ShopCart, ShopCartService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopCartService shopCartService;

	@Autowired
	private ShopCartItemService shopCartItemService;

	@Autowired
	private ShopUsersService shopUsersService;
	@Autowired
	private ShopProductsService shopProductsService;


	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<ShopUsers> usersServiceAll = shopUsersService.getAll();
		Map<String, String> emailByUserIdMap = usersServiceAll.stream()
				.filter(u -> u.getEmail() != null)
				.collect(Collectors.toMap(
						u -> String.valueOf(u.getId()),
						ShopUsers::getEmail
				));
		model.put("emailByUserIdMap", emailByUserIdMap);
	}

	@Override
	public String show(HttpServletRequest request, HttpServletResponse response, Model model) {
		super.show(request, response, model);
		try {
			String id = request.getParameter("id");
			if (id!= null) {
				Map<String, Object> params = Maps.newHashMap();
				params.put("EQ_cartId", id);
				List<ShopCartItem> shopCartItems = shopCartItemService.query(params, null);
				if (CollectionUtils.isEmpty(shopCartItems)) {
					model.addAttribute("shopCartItemDtos", Collections.emptyList());
					return "manage/shop/shopCartShow";
				}
				List<ShopProducts> all = shopProductsService.getAll();
				Map<Long, String> shopProductIdByNameMap = all.stream().collect(Collectors.toMap(
						ShopProducts::getId, ShopProducts::getName));
				// 将 ShopCartItem 转 DTO
				List<ShopCartItemDto> shopCartItemDtos = shopCartItems.stream()
						.map(item -> {
							ShopCartItemDto dto = new ShopCartItemDto();
							dto.setId(item.getId());
							dto.setCartId(item.getCartId());
							dto.setUserId(item.getUserId());
							dto.setProductName(shopProductIdByNameMap.get(item.getProductId()));
							dto.setQuantity(item.getQuantity());
							dto.setPrice(item.getPrice());
							dto.setTotalPrice(item.getTotalPrice());
							dto.setProductImage(item.getProductImage());
							dto.setCreateTime(item.getCreateTime());
							return dto;
						})
						.collect(Collectors.toList());
				model.addAttribute("shopCartItemDtos", shopCartItemDtos);
			}
		}catch (Exception e) {
			// 如果查询订单项失败，不影响订单基本信息显示
			// 可以在日志中记录错误，但不抛出异常
		}
		return "manage/shop/shopCartShow";
	}
}
