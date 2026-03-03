/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.dto.TranslationDTO;

import java.util.List;
import java.util.Map;

/**
 * 多语言翻译表 Service接口
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
public interface ShopI18nTranslationsService extends EntityService<ShopI18nTranslations> {

    /**
     * 唯一性查询
     * uk_i18n_translation
     *
     * @param entityType
     * @param entityId
     * @param fieldName
     * @param locale
     * @return
     */
    ShopI18nTranslations uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(String entityType, Long entityId, String fieldName, String locale);

    /**
     * 查询商品的所有翻译
     * @param productId 商品ID
     * @return 翻译列表
     */
    List<ShopI18nTranslations> findByProduct(Long productId);

    /**
     * 查询特定字段的翻译
     * @param productId 商品ID
     * @param fieldName 字段名称（name 或 description）
     * @param locale 语言代码
     * @return 翻译对象，如果不存在则返回null
     */
    ShopI18nTranslations findByProductAndFieldAndLocale(Long productId, String fieldName, String locale);

    /**
     * 保存或更新翻译（存在则更新，不存在则新增）
     * @param entityType 实体类型（product）
     * @param entityId 实体ID（商品ID）
     * @param fieldName 字段名称（name 或 description）
     * @param locale 语言代码
     * @param translation 翻译内容
     */
    void saveOrUpdateTranslation(String entityType, Long entityId, String fieldName, String locale, String translation);

    /**
     * 批量保存翻译
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param translations 翻译列表
     */
    void batchSaveTranslations(String entityType, Long entityId, List<TranslationDTO> translations);

    /**
     * 删除翻译
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param fieldName 字段名称
     * @param locale 语言代码
     */
    void deleteTranslation(String entityType, Long entityId, String fieldName, String locale);

    /**
     * 根据实体类型和实体ID查询所有翻译
     * @param entityType 实体类型（product, sub_category, parent_category, attr_value等）
     * @param entityId 实体ID
     * @return 翻译列表
     */
    List<ShopI18nTranslations> findByEntityTypeAndEntityId(String entityType, Long entityId);

    Map<String, String> getTranslations(String entityType, Long entityId, String locale);
}
