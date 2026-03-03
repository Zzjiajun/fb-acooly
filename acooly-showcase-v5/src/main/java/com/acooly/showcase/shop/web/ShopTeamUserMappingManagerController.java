/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-12
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;
import com.acooly.showcase.shop.service.ShopTeamUserMappingService;

import com.google.common.collect.Maps;

/**
 * shop_team_user_mapping 管理控制器
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
@Controller
@RequestMapping(value = "/manage/shop/shopTeamUserMapping")
public class ShopTeamUserMappingManagerController extends AbstractJsonEntityController<ShopTeamUserMapping, ShopTeamUserMappingService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopTeamUserMappingService shopTeamUserMappingService;







}
