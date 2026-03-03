/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-22
*/
package com.acooly.showcase.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.showcase.shop.entity.ShopAttr;
import com.acooly.showcase.shop.service.ShopAttrService;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.service.ShopI18nLocalesService;
import com.acooly.showcase.shop.service.ShopI18nTranslationsService;
import com.acooly.showcase.shop.dto.LocaleTranslationVO;
import com.acooly.showcase.shop.dto.TranslationDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性表 管理控制器
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopAttr")
public class ShopAttrManagerController extends AbstractJsonEntityController<ShopAttr, ShopAttrService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopAttrService shopAttrService;

	@Autowired
	private ShopI18nLocalesService shopI18nLocalesService;

	@Autowired
	private ShopI18nTranslationsService shopI18nTranslationsService;

	// ========== 多语言翻译管理接口 ==========

	/**
	 * 翻译管理弹窗
	 */
	@RequestMapping("translationDialog")
	public String translationDialog(HttpServletRequest request, Model model) {
		String attrIdStr = request.getParameter("attrId");
		if (attrIdStr != null && !attrIdStr.trim().isEmpty()) {
			try {
				Long attrId = Long.parseLong(attrIdStr.trim());
				ShopAttr attr = getEntityService().get(attrId);
				if (attr != null) {
					model.addAttribute("attrId", attrId);
					model.addAttribute("attrName", attr.getName());
				}
			} catch (NumberFormatException e) {
				log.warn("无效的属性ID: {}", attrIdStr);
			}
		}
		return "manage/shop/shopAttrTranslationDialog";
	}

	/**
	 * 获取属性的多语言翻译
	 */
	@RequestMapping("getAttrTranslations")
	@ResponseBody
	public JsonEntityResult<Map<String, Object>> getAttrTranslations(@RequestParam Long attrId) {
		JsonEntityResult<Map<String, Object>> result = new JsonEntityResult<>();
		try {
			if (attrId == null) {
				result.setSuccess(false);
				result.setMessage("属性ID不能为空");
				return result;
			}

			ShopAttr attr = getEntityService().get(attrId);
			if (attr == null) {
				result.setSuccess(false);
				result.setMessage("属性不存在");
				return result;
			}

			// 获取所有启用的语言
			List<ShopI18nLocales> activeLocales = shopI18nLocalesService.getActiveLocales();
			if (activeLocales == null || activeLocales.isEmpty()) {
				result.setSuccess(false);
				result.setMessage("没有启用的语言");
				return result;
			}

			// 获取属性的所有翻译（只查询 name 字段）
			List<ShopI18nTranslations> translations = shopI18nTranslationsService.findByEntityTypeAndEntityId("attr", attrId);
			
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
			data.put("attrId", attrId);
			data.put("attrName", attr.getName());
			data.put("locales", localeVOs);

			result.setEntity(data);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("获取属性翻译失败：" + e.getMessage());
			log.error("获取属性翻译失败", e);
		}
		return result;
	}

	/**
	 * 批量保存属性翻译
	 */
	@RequestMapping(value = "batchSaveAttrTranslations", method = RequestMethod.POST)
	@ResponseBody
	public JsonEntityResult<String> batchSaveAttrTranslations(
			@RequestBody Map<String, Object> requestData
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			Long attrId = null;
			if (requestData.get("attrId") != null) {
				if (requestData.get("attrId") instanceof Number) {
					attrId = ((Number) requestData.get("attrId")).longValue();
				} else {
					attrId = Long.parseLong(requestData.get("attrId").toString());
				}
			}

			if (attrId == null) {
				result.setSuccess(false);
				result.setMessage("属性ID不能为空");
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
			shopI18nTranslationsService.batchSaveTranslations("attr", attrId, translations);

			// 【数据同步】如果保存的是默认语言的翻译，需要同步更新属性表的 name
			if (defaultLocaleCode != null) {
				ShopAttr attr = getEntityService().get(attrId);
				if (attr != null) {
					for (TranslationDTO dto : translations) {
						if (defaultLocaleCode.equals(dto.getLocale()) && "name".equals(dto.getFieldName())) {
							String newName = dto.getTranslation().trim();
							if (attr.getName() == null || !newName.equals(attr.getName())) {
								attr.setName(newName);
								getEntityService().update(attr);
								log.info("同步属性名称 - 属性ID: {}, 新名称: {}", attrId, newName);
							}
							break;
						}
					}
				}
			}

			result.setEntity("批量保存成功");
			result.setSuccess(true);
			result.setMessage("成功保存 " + translations.size() + " 条翻译");
			log.info("批量保存属性翻译成功 - 属性ID: {}, 数量: {}", attrId, translations.size());
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("批量保存翻译失败：" + e.getMessage());
			log.error("批量保存属性翻译失败", e);
		}
		return result;
	}

	/**
	 * 删除属性翻译
	 */
	@RequestMapping("deleteAttrTranslation")
	@ResponseBody
	public JsonEntityResult<String> deleteAttrTranslation(
			@RequestParam Long attrId,
			@RequestParam String locale
	) {
		JsonEntityResult<String> result = new JsonEntityResult<>();
		try {
			if (attrId == null) {
				result.setSuccess(false);
				result.setMessage("属性ID不能为空");
				return result;
			}
			if (locale == null || locale.trim().isEmpty()) {
				result.setSuccess(false);
				result.setMessage("语言代码不能为空");
				return result;
			}

			// 删除翻译
			shopI18nTranslationsService.deleteTranslation("attr", attrId, "name", locale);

			result.setEntity("删除成功");
			result.setSuccess(true);
			result.setMessage("删除成功");
			log.info("删除属性翻译成功 - 属性ID: {}, 语言: {}", attrId, locale);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("删除翻译失败：" + e.getMessage());
			log.error("删除属性翻译失败", e);
		}
		return result;
	}
}
