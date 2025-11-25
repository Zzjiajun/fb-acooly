/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.showcase.daliy.entity.DmShow;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.shop.entity.ShopCoupon;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.service.ShopCouponService;
import com.acooly.showcase.shop.service.ShopProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopOrders;
import com.acooly.showcase.shop.entity.ShopOrderItems;
import com.acooly.showcase.shop.service.ShopOrdersService;
import com.acooly.showcase.shop.service.ShopOrderItemsService;

import com.google.common.collect.Maps;

/**
 * 订单表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopOrders")
public class ShopOrdersManagerController extends AbstractJsonEntityController<ShopOrders, ShopOrdersService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopOrdersService shopOrdersService;

	@Autowired
	private ShopOrderItemsService shopOrderItemsService;
	@Autowired
	private ShopProductsService shopProductsService;
	@Autowired
	private ShopCouponService shopCouponService;

	@Override
	public JsonListResult<ShopOrders> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopOrders> result = new JsonListResult();
		this.allow(request, response, MappingMethod.list);
		try {
			result.appendData(this.referenceData(request));
			PageInfo<ShopOrders> pageInfo = this.doList(request, response);
			result.setTotal(pageInfo.getTotalCount());
			result.setRows(pageInfo.getPageResults());
			result.setHasNext(pageInfo.hasNext());
			result.setPageNo(pageInfo.getCurrentPage());
			result.setPageSize(pageInfo.getCountOfCurrentPage());
		} catch (Exception var5) {
			this.handleException(result, "分页查询", var5);
		}

		return result;
	}

	/**
	 * 重写 show 方法，查询订单项列表
	 */
	@Override
	public String show(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			// 先调用父类方法获取订单信息
			super.show(request, response, model);
			
			// 获取订单ID（从父类方法中已加载的实体获取）
			String id = request.getParameter("id");
			if (id != null) {
				// 查询订单项列表（注意类型转换：Long -> Integer）
				Map<String, Object> params = Maps.newHashMap();
				params.put("EQ_orderId", id);
				List<ShopOrderItems> orderItems = shopOrderItemsService.query(params, null);
				List<Integer> productIdList = orderItems.stream().map(ShopOrderItems::getProductId)
						.distinct().collect(Collectors.toList());
				Map<String, Object> mapProductId = Maps.newHashMap();
				mapProductId.put("IN_id", productIdList);
				List<ShopProducts> productsList = shopProductsService.query(mapProductId, null);
				Map<Long, String> ImgUrlMap = productsList.stream().collect(Collectors.toMap(ShopProducts::getId, ShopProducts::getImageUrl));
				orderItems.forEach(item -> {
					String imgUrl = ImgUrlMap.get(Long.valueOf(item.getProductId()));
					item.setProductImage(imgUrl);
				});
				// 将订单项列表添加到 Model
				model.addAttribute("orderItems", orderItems);
			}
		} catch (Exception e) {
			// 如果查询订单项失败，不影响订单基本信息显示
			// 可以在日志中记录错误，但不抛出异常
		}
		return "manage/shop/shopOrdersShow";
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<ShopCoupon> couponList = shopCouponService.getAll();
//		Map<Long, String> couponMap = couponList.stream().collect(Collectors.toMap(ShopCoupon::getId,ShopCoupon::getCode));
		model.put("couponList", couponList);
		super.referenceData(request, model);
	}

	//	@Override
//	public String show(HttpServletRequest request, HttpServletResponse response, Model model) {
//		try {
//			// 1️⃣ 调用父类方法加载基础订单信息
//			super.show(request, response, model);
//
//			String idParam = request.getParameter("id");
//			if (idParam == null || idParam.isEmpty()) {
//				log.warn("订单详情请求缺少参数: id");
//				return "manage/shop/shopOrdersShow";
//			}
//
//			Long orderId;
//			try {
//				orderId = Long.parseLong(idParam);
//			} catch (NumberFormatException e) {
//				log.warn("订单ID格式错误: {}", idParam);
//				return "manage/shop/shopOrdersShow";
//			}
//
//			// 2️⃣ 查询订单项列表
//			Map<String, Object> params = Maps.newHashMap();
//			params.put("EQ_orderId", orderId);
//
//			List<ShopOrderItems> orderItems = shopOrderItemsService.query(params, null);
//			if (orderItems == null || orderItems.isEmpty()) {
//				log.info("订单无商品项: orderId={}", orderId);
//				model.addAttribute("orderItems", Collections.emptyList());
//				return "manage/shop/shopOrdersShow";
//			}
//
//			// 3️⃣ 提取所有商品ID
//			List<Long> productIds = orderItems.stream()
//					.map(ShopOrderItems::getProductId)
//					.filter(Objects::nonNull)
//					.map(Long::valueOf)
//					.distinct()
//					.collect(Collectors.toList());
//
//			if (productIds.isEmpty()) {
//				log.warn("订单项无有效商品ID: orderId={}", orderId);
//				model.addAttribute("orderItems", orderItems);
//				return "manage/shop/shopOrdersShow";
//			}
//
//			// 4️⃣ 批量查询商品信息
//			Map<String, Object> queryProducts = Maps.newHashMap();
//			queryProducts.put("IN_id", productIds);
//
//			List<ShopProducts> productList = shopProductsService.query(queryProducts, null);
//
//			// 5️⃣ 构建映射表（防止重复ID抛异常）
//			Map<Long, String> imageUrlMap = productList.stream()
//					.filter(p -> p.getId() != null)
//					.collect(Collectors.toMap(
//							ShopProducts::getId,
//							ShopProducts::getImageUrl,
//							(a, b) -> a  // 遇重复ID保留第一个
//					));
//
//			// 6️⃣ 为订单项补全图片
//			orderItems.forEach(item -> item.setProductImage(imageUrlMap.get(item.getProductId())));
//
//			// 7️⃣ 放入 model
//			model.addAttribute("orderItems", orderItems);
//
//			log.info("订单详情加载成功: orderId={}, 商品数量={}", orderId, orderItems.size());
//
//		} catch (Exception e) {
//			// 即便出错，也不阻止基础订单页展示
//			log.error("加载订单详情失败: {}", e.getMessage(), e);
//			model.addAttribute("orderItems", Collections.emptyList());
//		}
//
//		return "manage/shop/shopOrdersShow";
//	}

}
