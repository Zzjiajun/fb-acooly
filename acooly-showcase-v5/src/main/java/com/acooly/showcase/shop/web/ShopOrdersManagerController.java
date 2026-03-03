/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.showcase.daliy.entity.DmShow;
import com.acooly.showcase.daliy.entity.Link;
import com.acooly.showcase.shop.entity.*;
import com.acooly.showcase.shop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@Autowired
	private ShopTeamService shopTeamService;
	@Autowired
	private ShopUsersService shopUsersService;

	@Override
	public JsonListResult<ShopOrders> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopOrders> result = new JsonListResult();
		this.allow(request, response, MappingMethod.list);
		try {
			result.appendData(this.referenceData(request));
			PageInfo<ShopOrders> pageInfo = this.doList(request, response);
			result.setTotal(pageInfo.getTotalCount());
			List<ShopOrders> orders = pageInfo.getPageResults();
			Set<Long> userIds = orders.stream()
					.map(ShopOrders::getUserId)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			Map<Long, ShopUsers> userMap = shopUsersService.getAll()
					.stream()
					.filter(u -> userIds.contains(u.getId()))
					.collect(Collectors.toMap(
							ShopUsers::getId,
							Function.identity(),
							(a, b) -> a
					));
			Set<Long> teamIds = userMap.values().stream()
					.map(ShopUsers::getTeamId)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			Map<Long, String> teamMap = shopTeamService.getAll()
					.stream()
					.filter(t -> teamIds.contains(t.getId()))
					.collect(Collectors.toMap(
							ShopTeam::getId,
							ShopTeam::getTeamName,
							(a, b) -> a
					));
			orders.forEach(order -> {
				ShopUsers user = userMap.get(order.getUserId());
				if (user == null) {
					order.setTeamName("admin");
				} else {
					order.setTeamName(
							teamMap.getOrDefault(user.getTeamId(), "admin")
					);
				}
			});
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



	/**
	 * 根据日期范围获取订单统计数据（用于AJAX动态加载）
	 * @param startDate 开始日期，格式：yyyy-MM-dd，如果为空则默认为最近7天的开始日期
	 * @param endDate 结束日期，格式：yyyy-MM-dd，如果为空则默认为今天
	 * @return 包含订单统计数据和趋势数据的Map
	 */
	@RequestMapping(value = "/orderStats")
	@ResponseBody
	public Map<String, Object> getOrderStatsByDateRange(
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {

		Map<String, Object> result = new HashMap<>();

		try {
			// 解析日期参数，如果没有则使用默认值（最近7天）
			LocalDate start;
			LocalDate end;

			if (startDate != null && !startDate.isEmpty()) {
				start = LocalDate.parse(startDate);
			} else {
				start = LocalDate.now().minusDays(6);
			}

			if (endDate != null && !endDate.isEmpty()) {
				end = LocalDate.parse(endDate);
			} else {
				end = LocalDate.now();
			}

			// 验证日期范围
			if (start.isAfter(end)) {
				result.put("error", "开始日期不能晚于结束日期");
				return result;
			}

			// 限制最大查询范围为90天，避免性能问题
			long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);
			if (daysBetween > 90) {
				result.put("error", "日期范围不能超过90天");
				return result;
			}

			LocalDateTime startDateTime = start.atStartOfDay();
			LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();

			// 获取该日期范围内的所有订单
			List<ShopOrders> allOrders = shopOrdersService.getAll();
			// 排除状态为PENDING、CANCELLED和FAILED的订单
			List<ShopOrders> ordersInRange = allOrders.stream()
					.filter(o -> {
						if (o.getCreateTime() == null) return false;
						LocalDateTime createTime = o.getCreateTime().toInstant()
								.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
						// 排除取消、待付款和失败的订单
						boolean isValidStatus = o.getStatus() != null 
								&& !"PENDING".equalsIgnoreCase(o.getStatus())
								&& !"CANCELLED".equalsIgnoreCase(o.getStatus())
								&& !"FAILED".equalsIgnoreCase(o.getStatus());
						return createTime.isAfter(startDateTime) && createTime.isBefore(endDateTime) && isValidStatus;
					})
					.collect(Collectors.toList());

			// 计算该范围内的总订单数和总销售额（已排除无效状态订单）
			long totalOrderCount = ordersInRange.size();
			BigDecimal totalSalesAmount = ordersInRange.stream()
					.map(ShopOrders::getTotalPrice)
					.filter(price -> price != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 计算趋势数据（按天统计）
			List<Map<String, Object>> trendData = new ArrayList<>();
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate currentDate = start;
			while (!currentDate.isAfter(end)) {
				LocalDateTime dayStart = currentDate.atStartOfDay();
				LocalDateTime dayEnd = currentDate.plusDays(1).atStartOfDay();

				long dayOrderCount = ordersInRange.stream()
						.filter(o -> {
							LocalDateTime createTime = o.getCreateTime().toInstant()
									.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
							// 注意：ordersInRange已经过滤了无效状态，这里只需要过滤日期
							return createTime.isAfter(dayStart) && createTime.isBefore(dayEnd);
						})
						.count();

				BigDecimal daySalesAmount = ordersInRange.stream()
						.filter(o -> {
							LocalDateTime createTime = o.getCreateTime().toInstant()
									.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
							// 注意：ordersInRange已经过滤了无效状态，这里只需要过滤日期
							return createTime.isAfter(dayStart) && createTime.isBefore(dayEnd);
						})
						.map(ShopOrders::getTotalPrice)
						.filter(price -> price != null)
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				Map<String, Object> dayData = new HashMap<>();
				dayData.put("date", currentDate.format(dateFormatter));
				dayData.put("orderCount", dayOrderCount);
				dayData.put("salesAmount", daySalesAmount);
				trendData.add(dayData);

				currentDate = currentDate.plusDays(1);
			}

			// 计算同比数据（与上一相同时间段对比）
			long daysBetweenPeriods = java.time.temporal.ChronoUnit.DAYS.between(start, end);
			LocalDate lastPeriodStart = start.minusDays(daysBetweenPeriods + 1);
			LocalDate lastPeriodEnd = start.minusDays(1);

			LocalDateTime lastPeriodStartDateTime = lastPeriodStart.atStartOfDay();
			LocalDateTime lastPeriodEndDateTime = lastPeriodEnd.plusDays(1).atStartOfDay();

			List<ShopOrders> lastPeriodOrders = allOrders.stream()
					.filter(o -> {
						if (o.getCreateTime() == null) return false;
						LocalDateTime createTime = o.getCreateTime().toInstant()
								.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
						// 排除取消、待付款和失败的订单
						boolean isValidStatus = o.getStatus() != null 
								&& !"PENDING".equalsIgnoreCase(o.getStatus())
								&& !"CANCELLED".equalsIgnoreCase(o.getStatus())
								&& !"FAILED".equalsIgnoreCase(o.getStatus());
						return createTime.isAfter(lastPeriodStartDateTime) && createTime.isBefore(lastPeriodEndDateTime) && isValidStatus;
					})
					.collect(Collectors.toList());

			long lastPeriodOrderCount = lastPeriodOrders.size();
			BigDecimal lastPeriodSalesAmount = lastPeriodOrders.stream()
					.map(ShopOrders::getTotalPrice)
					.filter(price -> price != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// 计算同比百分比
			double orderCompare = lastPeriodOrderCount > 0 ?
					((totalOrderCount - lastPeriodOrderCount) * 100.0 / lastPeriodOrderCount) : 0.0;
			double salesCompare = lastPeriodSalesAmount.compareTo(BigDecimal.ZERO) > 0 ?
					totalSalesAmount.subtract(lastPeriodSalesAmount)
							.divide(lastPeriodSalesAmount, 4, BigDecimal.ROUND_HALF_UP)
							.multiply(new BigDecimal("100")).doubleValue() : 0.0;

			// 构建返回数据
			Map<String, Object> orderStats = new HashMap<>();
			orderStats.put("totalOrderCount", totalOrderCount);
			orderStats.put("totalSalesAmount", totalSalesAmount);
			orderStats.put("orderCompare", orderCompare);
			orderStats.put("salesCompare", salesCompare);
			orderStats.put("startDate", start.format(dateFormatter));
			orderStats.put("endDate", end.format(dateFormatter));

			result.put("orderStats", orderStats);
			result.put("trendData", trendData);
			result.put("success", true);

		} catch (Exception e) {
			log.error("获取订单统计数据失败", e);
			result.put("error", "获取数据失败：" + e.getMessage());
			result.put("success", false);
		}

		return result;
	}
}
