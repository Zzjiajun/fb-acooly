/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-26
*/
package com.acooly.showcase.shop.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.showcase.shop.utils.RedisShopUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.service.ProductTranslationSyncService;
import com.acooly.showcase.shop.service.ShopI18nLocalesService;

import com.google.common.collect.Maps;

/**
 * 支持的语言列表 管理控制器
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopI18nLocales")
public class ShopI18nLocalesManagerController extends AbstractJsonEntityController<ShopI18nLocales, ShopI18nLocalesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopI18nLocalesService shopI18nLocalesService;

	@Autowired
	private ProductTranslationSyncService productTranslationSyncService;
	@Autowired
	private RedisShopUtil redisShopUtil;

	@Override
	protected ShopI18nLocales onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopI18nLocales entity, boolean isCreate) throws Exception {
		// 【自动计算排序】如果是新建，自动设置为最大排序值+1
		if (isCreate && entity != null) {
			// 查询当前最大的排序值
			Map<String, Object> params = Maps.newHashMap();
			Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
			sortMap.put("sortOrder", false); // 降序
			java.util.List<ShopI18nLocales> allLocales = getEntityService().query(params, sortMap);
			int maxSortOrder = 0;
			if (allLocales != null && !allLocales.isEmpty()) {
				ShopI18nLocales first = allLocales.get(0);
				if (first.getSortOrder() != null) {
					maxSortOrder = first.getSortOrder();
				}
			}
			entity.setSortOrder(maxSortOrder + 1);
			log.debug("自动设置排序顺序 - 新语言: {}, 排序: {}", entity.getLocaleCode(), entity.getSortOrder());
		}
		
		// 如果是更新操作，检查默认语言是否变更
		if (!isCreate && entity != null && entity.getId() != null) {
			ShopI18nLocales oldEntity = getEntityService().get(entity.getId());
			if (oldEntity != null) {
				// 检查 isDefault 字段是否从 0 变为 1（成为默认语言）
				Integer oldIsDefault = oldEntity.getIsDefault() != null ? oldEntity.getIsDefault() : 0;
				Integer newIsDefault = entity.getIsDefault() != null ? entity.getIsDefault() : 0;
				
				// 如果新设置为默认语言，且旧的不是默认语言
				if (newIsDefault == 1 && oldIsDefault != 1) {
					// 查找旧的默认语言
					ShopI18nLocales oldDefaultLocale = shopI18nLocalesService.getDefaultLocale();
					String oldDefaultLocaleCode = oldDefaultLocale != null ? oldDefaultLocale.getLocaleCode() : null;
					String newDefaultLocaleCode = entity.getLocaleCode();
					
					// 先取消其他语言的默认状态
					if (oldDefaultLocale != null && !oldDefaultLocale.getLocaleCode().equals(newDefaultLocaleCode)) {
						oldDefaultLocale.setIsDefault(0);
						getEntityService().update(oldDefaultLocale);
						log.info("取消旧默认语言 - 语言代码: {}", oldDefaultLocaleCode);
					}
					
					// 执行数据迁移
					if (oldDefaultLocaleCode != null && !oldDefaultLocaleCode.equals(newDefaultLocaleCode)) {
						log.info("检测到默认语言变更 - 从 {} 到 {}", oldDefaultLocaleCode, newDefaultLocaleCode);
						productTranslationSyncService.migrateDefaultLocale(oldDefaultLocaleCode, newDefaultLocaleCode);
					}
				}
			}
		}
		redisShopUtil.del("I18LocaleContext");
		return super.onSave(request, response, model, entity, isCreate);
	}

}
