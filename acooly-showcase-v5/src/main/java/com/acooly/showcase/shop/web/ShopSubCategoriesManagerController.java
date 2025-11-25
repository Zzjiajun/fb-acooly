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
import javax.servlet.http.HttpServletResponse;

import com.acooly.showcase.shop.entity.ShopParentCategories;
import com.acooly.showcase.shop.entity.ShopProductFeatures;
import com.acooly.showcase.shop.service.ShopParentCategoriesService;
import com.acooly.showcase.shop.service.ShopProductFeaturesService;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopSubCategories;
import com.acooly.showcase.shop.service.ShopSubCategoriesService;

import com.google.common.collect.Maps;

import static com.acooly.showcase.shop.web.ShopProductsManagerController.CATEGORY_TREE;

/**
 * 子级分类表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Controller
@RequestMapping(value = "/manage/shop/shopSubCategories")
public class ShopSubCategoriesManagerController extends AbstractJsonEntityController<ShopSubCategories, ShopSubCategoriesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopSubCategoriesService shopSubCategoriesService;

	@Autowired
	private ShopParentCategoriesService shopParentCategoriesService;

	@Autowired
	private RedisShopUtil redisUtil;
	private static final String SUB_CATEGORY_DETAIL_PREFIX = "sub_category:detail:";
	private static final String SUB_CATEGORY_PRODUCT_COUNT_PREFIX = "sub_category:product_count:";
	private static final String SUB_CATEGORY_PRODUCTS_PREFIX = "sub_category:products:";
	private static final String SUB_CATEGORY_WITH_PRODUCTS = "sub_category_with_products:";
	private static final String PARENT_CATEGORY_DETAIL_PREFIX = "parent_category:detail:";
	private static final String PARENT_CATEGORY_PRODUCTS_PREFIX = "parent_category:products:";
	public static final String PRODUCT_CATEGORY_WITH_PRODUCTS = "parent_category_with_products:";
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		//获取所有父级分类
		List<ShopParentCategories> categoriesList = shopParentCategoriesService.getAll();

		Map<Long, String> parentMap = categoriesList.stream().collect(Collectors.
				toMap(ShopParentCategories::getId, ShopParentCategories::getName));
		model.put("parentMap", parentMap);

	}

	@Override
	protected ShopSubCategories onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopSubCategories entity, boolean isCreate) throws Exception {
		invalidateSubCategoryCache(entity.getId());
		return super.onSave(request, response, model, entity, isCreate);
	}

	private void invalidateSubCategoryCache(Long subId) {
		if (subId == null) {
			return;
		}
		redisUtil.del(redisUtil.buildKey(SUB_CATEGORY_DETAIL_PREFIX, String.valueOf(subId)));
		redisUtil.del(redisUtil.buildKey(SUB_CATEGORY_PRODUCT_COUNT_PREFIX, String.valueOf(subId)));
		redisUtil.deleteByPattern(SUB_CATEGORY_PRODUCTS_PREFIX + subId + "*");
		redisUtil.deleteByPattern(SUB_CATEGORY_WITH_PRODUCTS + subId + "*");
		ShopSubCategories subCategory = shopSubCategoriesService.get(subId);
		if (subCategory != null && subCategory.getParentId() != null) {
			invalidateParentCategoryCache(subCategory.getParentId());
		}
		redisUtil.del(CATEGORY_TREE); // 使用统一常量
	}

	public void invalidateParentCategoryCache(Long parentId) {
		redisUtil.del(PARENT_CATEGORY_DETAIL_PREFIX + parentId);
		redisUtil.del("parent_category:total_products:" + parentId);
		redisUtil.deleteByPattern(PARENT_CATEGORY_PRODUCTS_PREFIX + parentId + "*");
		redisUtil.deleteByPattern(PRODUCT_CATEGORY_WITH_PRODUCTS + parentId + "*");
		redisUtil.del(CATEGORY_TREE);
	}
}
