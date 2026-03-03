/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-07
*/
package com.acooly.showcase.shop.web;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.shop.entity.ShopContactInfo;
import com.acooly.showcase.shop.entity.ShopTeam;
import com.acooly.showcase.shop.service.ShopTeamService;
import com.acooly.showcase.shop.utils.CouponCodeUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
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
	@Autowired
	private ShopTeamService shopTeamService;
	private static final Map<String, String> CONTACT_TYPE_MAP = Maps.newHashMap();
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		CONTACT_TYPE_MAP.put("0.95","95折");
		CONTACT_TYPE_MAP.put("0.9","9折");
		CONTACT_TYPE_MAP.put("0.85","85折");
		CONTACT_TYPE_MAP.put("0.8","8折");
		CONTACT_TYPE_MAP.put("0.75","75折");
		CONTACT_TYPE_MAP.put("0.7","7折");
		CONTACT_TYPE_MAP.put("0.65","65折");
		CONTACT_TYPE_MAP.put("0.6","6折");
		CONTACT_TYPE_MAP.put("0.55","55折");
		CONTACT_TYPE_MAP.put("0.5","5折");
		Map<Long, String> teamMap = shopTeamService.getAll()
				.stream()
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName
				));
		model.put("teamMap", teamMap);
		model.put("contactTypeMap", CONTACT_TYPE_MAP);
	}
	@Override
	public JsonListResult<ShopCoupon> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ShopCoupon> result = super.listJson(request, response);
		List<ShopCoupon> rows = result.getRows();
		List<ShopTeam> shopTeams = shopTeamService.getAll();
		if (CollectionUtils.isEmpty(shopTeams)) {
			// 没有团队数据，统一给默认值
			rows.forEach(item -> item.setTeamName("admin"));
			return result;
		}
		Map<Long, String> teamMap = shopTeams.stream()
				.filter(t -> t.getId() != null)
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName,
						(a, b) -> a   // 防止重复 key
				));

		rows.forEach(item -> {
			Long teamId = Long.valueOf(item.getTeamId());

			String teamName = teamId == null
					? "admin"
					: teamMap.getOrDefault(teamId, "admin");

			item.setTeamName(teamName);
		});

		return result;
	}


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

	/**
	 * 重写updateJson方法，在更新后填充teamName
	 */
	@Override
	public JsonEntityResult<ShopCoupon> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopCoupon> result = super.updateJson(request, response);
		// 填充teamName
		if (result.getEntity() != null) {
			fillTeamName(result.getEntity());
		}
		return result;
	}

	/**
	 * 填充teamName的辅助方法
	 */
	private void fillTeamName(ShopCoupon coupon) {
		if (coupon == null) {
			return;
		}
		if (coupon.getTeamId() == null) {
			coupon.setTeamName("admin");
			return;
		}
		List<ShopTeam> shopTeams = shopTeamService.getAll();
		if (CollectionUtils.isEmpty(shopTeams)) {
			coupon.setTeamName("admin");
			return;
		}
		Map<Long, String> teamMap = shopTeams.stream()
				.filter(t -> t.getId() != null)
				.collect(Collectors.toMap(
						ShopTeam::getId,
						ShopTeam::getTeamName,
						(a, b) -> a   // 防止重复 key
				));
		Long teamId = Long.valueOf(coupon.getTeamId());
		String teamName = teamId == null
				? "admin"
				: teamMap.getOrDefault(teamId, "admin");
		coupon.setTeamName(teamName);
	}
}
