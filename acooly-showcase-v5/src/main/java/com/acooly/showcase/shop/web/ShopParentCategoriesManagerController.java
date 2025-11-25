/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.showcase.shop.utils.RedisShopUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopParentCategories;
import com.acooly.showcase.shop.service.ShopParentCategoriesService;

import com.google.common.collect.Maps;

import static com.acooly.showcase.shop.web.ShopProductsManagerController.CATEGORY_TREE;

/**
 * 父级分类表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:13
 */
@Controller
@RequestMapping(value = "/manage/shop/shopParentCategories")
public class ShopParentCategoriesManagerController extends AbstractJsonEntityController<ShopParentCategories, ShopParentCategoriesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopParentCategoriesService shopParentCategoriesService;
	@Autowired
	private RedisShopUtil redisUtil;
	private static final String PARENT_CATEGORY_DETAIL_PREFIX = "parent_category:detail:";
	private static final String PARENT_CATEGORY_PRODUCTS_PREFIX = "parent_category:products:";
	public static final String PRODUCT_CATEGORY_WITH_PRODUCTS = "parent_category_with_products:";

	@Override
	protected ShopParentCategories onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopParentCategories entity, boolean isCreate) throws Exception {
		invalidateParentCategoryCache(entity.getId());
		return super.onSave(request, response, model, entity, isCreate);
	}

	public void invalidateParentCategoryCache(Long parentId) {
		redisUtil.del(PARENT_CATEGORY_DETAIL_PREFIX + parentId);
		redisUtil.del("parent_category:total_products:" + parentId);
		redisUtil.deleteByPattern(PARENT_CATEGORY_PRODUCTS_PREFIX + parentId + "*");
		redisUtil.deleteByPattern(PRODUCT_CATEGORY_WITH_PRODUCTS + parentId + "*");
		redisUtil.del(CATEGORY_TREE);
	}
}
