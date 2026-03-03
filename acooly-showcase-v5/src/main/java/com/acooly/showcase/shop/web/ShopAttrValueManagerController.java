/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-22
*/
package com.acooly.showcase.shop.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.event.EventBus;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.showcase.shop.entity.ShopAttr;
import com.acooly.showcase.shop.entity.ShopAttrValue;
import com.acooly.showcase.shop.service.ShopAttrService;
import com.acooly.showcase.shop.service.ShopAttrValueService;
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
 * 商品属性值表 管理控制器
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopAttrValue")
public class ShopAttrValueManagerController extends AbstractJsonEntityController<ShopAttrValue, ShopAttrValueService> {

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopAttrValueService shopAttrValueService;
	@Autowired
	private EventBus eventBus;

	@Autowired
	private ShopAttrService shopAttrService;

	@Autowired
	private ShopI18nLocalesService shopI18nLocalesService;

	@Autowired
	private ShopI18nTranslationsService shopI18nTranslationsService;

	@Override
	public JsonEntityResult<ShopAttrValue> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopAttrValue> result = super.saveJson(request, response);
		ShopAttrValue entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setAttrId(entity.getAttrId());
			event.setAttrValueId(entity.getId());
			event.setAction("Attr");
			eventBus.publish(event);
		}
		return result;
	}

	@Override
	public JsonEntityResult<ShopAttrValue> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopAttrValue> result = super.updateJson(request, response);
		ShopAttrValue entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setAttrId(entity.getAttrId());
			event.setAttrValueId(entity.getId());
			event.setAction("Attr");
			eventBus.publish(event);
		}
		return result;
	}

	@Override
	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		for (Serializable id : ids) {
			if (id instanceof Long) {
				ShopAttrValue shopAttrValue = this.getEntityService().get((Long) id);
				if (shopAttrValue != null) {
					CacheShopRedisEvent event = new CacheShopRedisEvent();
					event.setAttrId(shopAttrValue.getAttrId());
					event.setAttrValueId((Long) id);
					event.setAction("Attr");
					eventBus.publish(event);
				}
			}
		}
		super.onRemove(request, response, model, ids);
	}

	/**
	 * 为编辑页面提供参考数据：属性列表
	 */
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		// 查询所有属性，构建下拉选择数据
		List<ShopAttr> attrs = shopAttrService.getAll();
		// 构建属性Map: attrId -> attrName
		Map<String, String> attrMap = attrs.stream()
				.collect(Collectors.toMap(
						attr -> String.valueOf(attr.getId()),
						ShopAttr::getName,
						(a, b) -> a,
						LinkedHashMap::new));
		model.put("attrMap", attrMap);
	}

	// ========== 多语言翻译管理接口 ==========

	/**
	 * 翻译管理弹窗
	 */
	@RequestMapping("translationDialog")
	public String translationDialog(HttpServletRequest request, Model model) {
		String attrValueIdStr = request.getParameter("attrValueId");
		if (attrValueIdStr != null && !attrValueIdStr.trim().isEmpty()) {
			try {
				Long attrValueId = Long.parseLong(attrValueIdStr.trim());
				ShopAttrValue attrValue = getEntityService().get(attrValueId);
				if (attrValue != null) {
					model.addAttribute("attrValueId", attrValueId);
					model.addAttribute("attrValueName", attrValue.getValue());
				}
			} catch (NumberFormatException e) {
				log.warn("无效的属性值ID: {}", attrValueIdStr);
			}
		}
		return "manage/shop/shopAttrValueTranslationDialog";
	}

	/**
	 * 获取属性值的多语言翻译
	 */
	@RequestMapping("getAttrValueTranslations")
	@ResponseBody
	public JsonEntityResult<Map<String, Object>> getAttrValueTranslations(@RequestParam Long attrValueId) {
		JsonEntityResult<Map<String, Object>> result = new JsonEntityResult<>();
		try {
			if (attrValueId == null) {
				result.setSuccess(false);
				result.setMessage("属性值ID不能为空");
				return result;
			}

			ShopAttrValue attrValue = getEntityService().get(attrValueId);
			if (attrValue == null) {
				result.setSuccess(false);
				result.setMessage("属性值不存在");
				return result;
			}

			// 获取所有启用的语言
			List<ShopI18nLocales> activeLocales = shopI18nLocalesService.getActiveLocales();
			if (activeLocales == null || activeLocales.isEmpty()) {
				result.setSuccess(false);
				result.setMessage("没有启用的语言");
				return result;
			}

			// 获取属性值的所有翻译（只查询 value 字段）
			List<ShopI18nTranslations> translations = shopI18nTranslationsService.findByEntityTypeAndEntityId("attr_value", attrValueId);
			
			// 构建翻译Map：locale -> translation
			Map<String, String> translationMap = new HashMap<>();
			if (translations != null) {
				for (ShopI18nTranslations t : translations) {
					if ("value".equals(t.getFieldName())) {
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
					localeTranslations.put("value", translation);
				}
				localeVO.setTranslations(localeTranslations);

				// 判断是否有翻译（使用 hasName 字段来存储是否有 value 翻译）
				localeVO.setHasName(translation != null && !translation.trim().isEmpty());

				localeVOs.add(localeVO);
			}

			// 构建返回数据
			Map<String, Object> data = new HashMap<>();
			data.put("attrValueId", attrValueId);
			data.put("attrValueName", attrValue.getValue());
			data.put("locales", localeVOs);

			result.setEntity(data);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取属性值翻译失败：" + e.getMessage());
			log.error("获取属性值翻译失败", e);
		}
		return result;
	}

	/**
	 * 批量保存属性值翻译
	 */
	@RequestMapping(value = "batchSaveAttrValueTranslations", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntityResult<String> batchSaveAttrValueTranslations(
			@RequestBody Map<String, Object> requestData
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			Long attrValueId = null;
			if (requestData.get("attrValueId") != null) {
				if (requestData.get("attrValueId") instanceof Number) {
					attrValueId = ((Number) requestData.get("attrValueId")).longValue();
				} else {
					attrValueId = Long.parseLong(requestData.get("attrValueId").toString());
				}
			}

			if (attrValueId == null) {
				result.setSuccess(false);
				result.setMessage("属性值ID不能为空");
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
				dto.setFieldName("value"); // 固定为 value
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
			shopI18nTranslationsService.batchSaveTranslations("attr_value", attrValueId, translations);

			// 【数据同步】如果保存的是默认语言的翻译，需要同步更新属性值表的 value
			if (defaultLocaleCode != null) {
				ShopAttrValue attrValue = getEntityService().get(attrValueId);
				if (attrValue != null) {
					for (TranslationDTO dto : translations) {
						if (defaultLocaleCode.equals(dto.getLocale()) && "value".equals(dto.getFieldName())) {
							String newValue = dto.getTranslation().trim();
							if (attrValue.getValue() == null || !newValue.equals(attrValue.getValue())) {
								attrValue.setValue(newValue);
								getEntityService().update(attrValue);
								log.info("同步属性值 - 属性值ID: {}, 新值: {}", attrValueId, newValue);
							}
							break;
						}
					}
				}
			}

			result.setEntity("批量保存成功");
			result.setSuccess(true);
			result.setMessage("成功保存 " + translations.size() + " 条翻译");
			log.info("批量保存属性值翻译成功 - 属性值ID: {}, 数量: {}", attrValueId, translations.size());
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("批量保存翻译失败：" + e.getMessage());
			log.error("批量保存属性值翻译失败", e);
		}
		return result;
	}

	/**
	 * 删除属性值翻译
	 */
	@RequestMapping("deleteAttrValueTranslation")
	@ResponseBody
	public JsonEntityResult<String> deleteAttrValueTranslation(
			@RequestParam Long attrValueId,
			@RequestParam String locale
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			if (attrValueId == null) {
				result.setSuccess(false);
				result.setMessage("属性值ID不能为空");
				return result;
			}
			if (locale == null || locale.trim().isEmpty()) {
				result.setSuccess(false);
				result.setMessage("语言代码不能为空");
				return result;
			}

			// 删除翻译
			shopI18nTranslationsService.deleteTranslation("attr_value", attrValueId, "value", locale);

			result.setEntity("删除成功");
			result.setSuccess(true);
			result.setMessage("删除成功");
			log.info("删除属性值翻译成功 - 属性值ID: {}, 语言: {}", attrValueId, locale);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("删除翻译失败：" + e.getMessage());
			log.error("删除属性值翻译失败", e);
		}
		return result;
	}
}
