/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-26
*/
package com.acooly.showcase.shop.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.event.EventBus;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import com.acooly.showcase.shop.service.ShopProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.service.ShopI18nLocalesService;
import com.acooly.showcase.shop.service.ShopI18nTranslationsService;

import com.google.common.collect.Maps;

/**
 * 多语言翻译表 管理控制器
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Controller
@RequestMapping(value = "/manage/shop/shopI18nTranslations")
public class ShopI18nTranslationsManagerController extends AbstractJsonEntityController<ShopI18nTranslations, ShopI18nTranslationsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopI18nTranslationsService shopI18nTranslationsService;

	@Autowired
	private ShopI18nLocalesService shopI18nLocalesService;
	@Autowired
	private ShopProductsService shopProductsService;
	@Autowired
	private EventBus eventBus;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		// 获取所有启用的语言列表
		List<ShopI18nLocales> activeLocales = shopI18nLocalesService.getActiveLocales();
		// 构建语言Map：localeCode -> localeName
		Map<String, String> localeMap = new HashMap<>();
		if (activeLocales != null) {
			for (ShopI18nLocales locale : activeLocales) {
				localeMap.put(locale.getLocaleCode(), locale.getLocaleName());
			}
		}
		model.put("localeMap", localeMap);
		super.referenceData(request, model);
	}

	@Override
	protected ShopI18nTranslations onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopI18nTranslations entity, boolean isCreate) throws Exception {
		ShopI18nTranslations saved = super.onSave(request, response, model, entity, isCreate);
		if (saved != null && "product".equals(saved.getEntityType()) && saved.getEntityId() != null) {
			ShopProducts products = shopProductsService.get(saved.getEntityId());
			if (products != null) {
				CacheShopRedisEvent event = new CacheShopRedisEvent();
				event.setShopProducts(products);
				event.setAction("Translate");
				eventBus.publish(event);
			}
		}
		return saved;
	}

	@Override
	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		for (Serializable id : ids) {
			ShopI18nTranslations entity = this.getEntityService().get(id);
			if (entity != null && "product".equals(entity.getEntityType()) && entity.getEntityId() != null) {
				ShopProducts products = shopProductsService.get(entity.getEntityId());
				if (products != null) {
					CacheShopRedisEvent event = new CacheShopRedisEvent();
					event.setShopProducts(products);
					event.setAction("Translate");
					eventBus.publish(event);
				}
			}
		}
		super.onRemove(request, response, model, ids);
	}
}
