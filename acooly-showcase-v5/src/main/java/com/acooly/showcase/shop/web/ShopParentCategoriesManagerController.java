/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.web;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.showcase.shop.entity.ShopSubCategories;
import com.acooly.module.event.EventBus;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.showcase.shop.entity.ShopParentCategories;
import com.acooly.showcase.shop.service.ShopParentCategoriesService;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.service.ShopI18nLocalesService;
import com.acooly.showcase.shop.service.ShopI18nTranslationsService;
import com.acooly.showcase.shop.dto.LocaleTranslationVO;
import com.acooly.showcase.shop.dto.TranslationDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 父级分类表 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:13
 */
@Slf4j
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
	private EventBus eventBus;

	@Autowired
	private ShopI18nLocalesService shopI18nLocalesService;

	@Autowired
	private ShopI18nTranslationsService shopI18nTranslationsService;

	@Override
	public JsonEntityResult<ShopParentCategories> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopParentCategories> result = super.updateJson(request, response);
		ShopParentCategories entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setCategoryId(entity.getId());
			event.setIsSubCategory(false);
			event.setAction("Category");
			eventBus.publish(event);
		}
		return result;
	}

	@Override
	public JsonEntityResult<ShopParentCategories> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopParentCategories> result = super.saveJson(request, response);
		ShopParentCategories entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setCategoryId(entity.getId());
			event.setIsSubCategory(false);
			event.setAction("Category");
			eventBus.publish(event);
		}
		return result;
	}
	@Override
	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		// 在删除前发布缓存清理事件
		for (Serializable id : ids) {
			if (id instanceof Long) {
				ShopParentCategories entity = this.getEntityService().get(id);
				if (entity != null && entity.getId() != null) {
					CacheShopRedisEvent event = new CacheShopRedisEvent();
					event.setCategoryId(entity.getId());
					event.setIsSubCategory(true);
					event.setAction("Category");
					eventBus.publish(event);
				}
			}
		}
		// 调用父类删除品牌
		super.onRemove(request, response, model, ids);
	}

	// ========== 多语言翻译管理接口 ==========

	/**
	 * 翻译管理弹窗
	 */
	@RequestMapping("translationDialog")
	public String translationDialog(HttpServletRequest request, Model model) {
		String categoryIdStr = request.getParameter("categoryId");
		if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
			try {
				Long categoryId = Long.parseLong(categoryIdStr.trim());
				ShopParentCategories category = getEntityService().get(categoryId);
				if (category != null) {
					model.addAttribute("categoryId", categoryId);
					model.addAttribute("categoryName", category.getName());
				}
			} catch (NumberFormatException e) {
				log.warn("无效的父分类ID: {}", categoryIdStr);
			}
		}
		return "manage/shop/shopParentCategoriesTranslationDialog";
	}

	/**
	 * 获取父分类的多语言翻译
	 */
	@RequestMapping("getParentCategoryTranslations")
	@ResponseBody
	public JsonEntityResult<Map<String, Object>> getParentCategoryTranslations(@RequestParam Long categoryId) {
		JsonEntityResult<Map<String, Object>> result = new JsonEntityResult<>();
		try {
			if (categoryId == null) {
				result.setSuccess(false);
				result.setMessage("父分类ID不能为空");
				return result;
			}

			ShopParentCategories category = getEntityService().get(categoryId);
			if (category == null) {
				result.setSuccess(false);
				result.setMessage("父分类不存在");
				return result;
			}

			// 获取所有启用的语言
			List<ShopI18nLocales> activeLocales = shopI18nLocalesService.getActiveLocales();
			if (activeLocales == null || activeLocales.isEmpty()) {
				result.setSuccess(false);
				result.setMessage("没有启用的语言");
				return result;
			}

			// 获取父分类的所有翻译（只查询 name 字段）
			List<ShopI18nTranslations> translations = shopI18nTranslationsService.findByEntityTypeAndEntityId("parent_category", categoryId);
			
			// 构建翻译Map：locale -> translation
			Map<String, String> translationMap = new HashMap<>();
			if (translations != null) {
				for (ShopI18nTranslations t : translations) {
					if ("name".equals(t.getFieldName())) {
						translationMap.put(t.getLocale(), t.getTranslation());
					}
				}
			}

			// 获取默认语言
			ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
			String defaultLocaleCode = defaultLocale != null ? defaultLocale.getLocaleCode() : null;

			// 构建 LocaleTranslationVO 列表
			List<LocaleTranslationVO> localeVOs = new ArrayList<>();
			for (ShopI18nLocales locale : activeLocales) {
				LocaleTranslationVO localeVO = new LocaleTranslationVO();
				localeVO.setLocaleCode(locale.getLocaleCode());
				localeVO.setLocaleName(locale.getLocaleName());
				localeVO.setIsDefault(locale.getLocaleCode().equals(defaultLocaleCode));

				// 获取该语言的翻译
				String translation = translationMap.getOrDefault(locale.getLocaleCode(), null);
				Map<String, String> localeTranslations = new HashMap<>();
				if (translation != null) {
					localeTranslations.put("name", translation);
				}
				localeVO.setTranslations(localeTranslations);

				// 判断是否有翻译
				localeVO.setHasName(translation != null && !translation.trim().isEmpty());

				localeVOs.add(localeVO);
			}

			// 构建返回数据
			Map<String, Object> data = new HashMap<>();
			data.put("categoryId", categoryId);
			data.put("categoryName", category.getName());
			data.put("locales", localeVOs);

			result.setEntity(data);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取父分类翻译失败：" + e.getMessage());
			log.error("获取父分类翻译失败", e);
		}
		return result;
	}

	/**
	 * 批量保存父分类翻译
	 */
	@RequestMapping(value = "batchSaveParentCategoryTranslations", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntityResult<String> batchSaveParentCategoryTranslations(
			@RequestBody Map<String, Object> requestData
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			Long categoryId = null;
			if (requestData.get("categoryId") != null) {
				if (requestData.get("categoryId") instanceof Number) {
					categoryId = ((Number) requestData.get("categoryId")).longValue();
				} else {
					categoryId = Long.parseLong(requestData.get("categoryId").toString());
				}
			}

			if (categoryId == null) {
				result.setSuccess(false);
				result.setMessage("父分类ID不能为空");
				return result;
			}

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> translationsData = (List<Map<String, Object>>) requestData.get("translations");
			if (translationsData == null || translationsData.isEmpty()) {
				result.setSuccess(false);
				result.setMessage("翻译数据不能为空");
				return result;
			}

			// 转换为 TranslationDTO 列表
			List<TranslationDTO> translations = new ArrayList<>();
			for (Map<String, Object> item : translationsData) {
				TranslationDTO dto = new TranslationDTO();
				dto.setFieldName("name"); // 固定为 name
				dto.setLocale((String) item.get("locale"));
				dto.setTranslation((String) item.get("translation"));

				if (dto.getLocale() != null && dto.getTranslation() != null) {
					translations.add(dto);
				}
			}

			if (translations.isEmpty()) {
				result.setSuccess(false);
				result.setMessage("没有有效的翻译数据");
				return result;
			}

			// 获取默认语言代码
			ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
			String defaultLocaleCode = defaultLocale != null ? defaultLocale.getLocaleCode() : null;

			// 批量保存翻译
			shopI18nTranslationsService.batchSaveTranslations("parent_category", categoryId, translations);

			// 【数据同步】如果保存的是默认语言的翻译，需要同步更新父分类表的 name
			if (defaultLocaleCode != null) {
				ShopParentCategories category = getEntityService().get(categoryId);
				if (category != null) {
					for (TranslationDTO dto : translations) {
						if (defaultLocaleCode.equals(dto.getLocale()) && "name".equals(dto.getFieldName())) {
							String newName = dto.getTranslation().trim();
							if (category.getName() == null || !newName.equals(category.getName())) {
								category.setName(newName);
								getEntityService().update(category);
								log.info("同步父分类名称 - 父分类ID: {}, 新名称: {}", categoryId, newName);
							}
							break;
						}
					}
				}
			}

			result.setEntity("批量保存成功");
			result.setSuccess(true);
			result.setMessage("成功保存 " + translations.size() + " 条翻译");
			log.info("批量保存父分类翻译成功 - 父分类ID: {}, 数量: {}", categoryId, translations.size());
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("批量保存翻译失败：" + e.getMessage());
			log.error("批量保存父分类翻译失败", e);
		}
		return result;
	}

	/**
	 * 删除父分类翻译
	 */
	@RequestMapping("deleteParentCategoryTranslation")
	@ResponseBody
	public JsonEntityResult<String> deleteParentCategoryTranslation(
			@RequestParam Long categoryId,
			@RequestParam String locale
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			if (categoryId == null) {
				result.setSuccess(false);
				result.setMessage("父分类ID不能为空");
				return result;
			}
			if (locale == null || locale.trim().isEmpty()) {
				result.setSuccess(false);
				result.setMessage("语言代码不能为空");
				return result;
			}

			// 删除翻译
			shopI18nTranslationsService.deleteTranslation("parent_category", categoryId, "name", locale);

			result.setEntity("删除成功");
			result.setSuccess(true);
			result.setMessage("删除成功");
			log.info("删除父分类翻译成功 - 父分类ID: {}, 语言: {}", categoryId, locale);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("删除翻译失败：" + e.getMessage());
			log.error("删除父分类翻译失败", e);
		}
		return result;
	}
}
