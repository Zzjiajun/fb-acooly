/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 */
package com.acooly.showcase.shop.service.impl;

import com.acooly.showcase.shop.utils.RedisShopUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopI18nTranslationsService;
import com.acooly.showcase.shop.dao.ShopI18nTranslationsDao;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.dto.TranslationDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 多语言翻译表 Service实现
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Slf4j
@Service("shopI18nTranslationsService")
public class ShopI18nTranslationsServiceImpl extends EntityServiceImpl<ShopI18nTranslations, ShopI18nTranslationsDao> implements ShopI18nTranslationsService {



    private static final String CACHE_PREFIX = "i18n:";
    @Autowired
    private RedisShopUtil redisUtil;
    /**
     * 缓存过期时间（小时）
     */
    private static final int CACHE_EXPIRE_HOURS = 24;
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
    @Override
    public ShopI18nTranslations uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(String entityType, Long entityId, String fieldName, String locale) {
        return getEntityDao().uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(entityType, entityId, fieldName, locale);
    }

    /**
     * 查询商品的所有翻译
     */
    @Override
    public List<ShopI18nTranslations> findByProduct(Long productId) {
        if (productId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_entityType", "product");
        params.put("EQ_entityId", productId);
        return query(params, null);
    }

    /**
     * 查询特定字段的翻译
     */
    @Override
    public ShopI18nTranslations findByProductAndFieldAndLocale(Long productId, String fieldName, String locale) {
        if (productId == null || fieldName == null || locale == null) {
            return null;
        }
        return uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale("product", productId, fieldName, locale);
    }

    /**
     * 保存或更新翻译（存在则更新，不存在则新增）
     */
    @Override
    @Transactional
    public void saveOrUpdateTranslation(String entityType, Long entityId, String fieldName, String locale, String translation) {
        if (entityType == null || entityId == null || fieldName == null || locale == null) {
            log.warn("保存翻译参数不完整 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
            return;
        }
        // translation 允许为空字符串，但不允许为 null
        if (translation == null) {
            log.warn("翻译内容为null，跳过保存 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
            return;
        }
        
        // 检查是否存在
        ShopI18nTranslations existing = uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(entityType, entityId, fieldName, locale);
        
        if (existing != null) {
            // 更新
            existing.setTranslation(translation);
            update(existing);
            log.debug("更新翻译 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
        } else {
            // 新增
            ShopI18nTranslations newTranslation = new ShopI18nTranslations();
            newTranslation.setEntityType(entityType);
            newTranslation.setEntityId(entityId);
            newTranslation.setFieldName(fieldName);
            newTranslation.setLocale(locale);
            newTranslation.setTranslation(translation);
            save(newTranslation);
            log.debug("新增翻译 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
        }
    }

    /**
     * 批量保存翻译
     */
    @Override
    @Transactional
    public void batchSaveTranslations(String entityType, Long entityId, List<TranslationDTO> translations) {
        if (entityType == null || entityId == null || translations == null || translations.isEmpty()) {
            log.warn("批量保存翻译参数不完整 - entityType: {}, entityId: {}, translations: {}", 
                    entityType, entityId, translations);
            return;
        }
        
        for (TranslationDTO dto : translations) {
            if (dto.getFieldName() != null && dto.getLocale() != null && dto.getTranslation() != null) {
                saveOrUpdateTranslation(entityType, entityId, dto.getFieldName(), dto.getLocale(), dto.getTranslation());
            }
        }
        
        log.info("批量保存翻译完成 - entityType: {}, entityId: {}, 数量: {}", entityType, entityId, translations.size());
    }

    /**
     * 删除翻译
     */
    @Override
    @Transactional
    public void deleteTranslation(String entityType, Long entityId, String fieldName, String locale) {
        if (entityType == null || entityId == null || fieldName == null || locale == null) {
            log.warn("删除翻译参数不完整 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
            return;
        }
        
        ShopI18nTranslations existing = uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(entityType, entityId, fieldName, locale);
        if (existing != null) {
            removeById(existing.getId());
            log.info("删除翻译 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
        } else {
            log.debug("翻译不存在，无需删除 - entityType: {}, entityId: {}, fieldName: {}, locale: {}", 
                    entityType, entityId, fieldName, locale);
        }
    }

    /**
     * 根据实体类型和实体ID查询所有翻译
     */
    @Override
    public List<ShopI18nTranslations> findByEntityTypeAndEntityId(String entityType, Long entityId) {
        if (entityType == null || entityId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_entityType", entityType);
        params.put("EQ_entityId", entityId);
        return query(params, null);
    }

    @Override
    public Map<String, String> getTranslations(String entityType, Long entityId, String locale) {
        if (entityType == null || entityId == null) {
            return Collections.emptyMap();
        }
        // 1. 尝试从缓存获取
        String cacheKey = buildEntityCacheKey(entityType, entityId, locale);
        Map<Object, Object> cached = redisUtil.get(cacheKey, Map.class);
        if (cached != null && !cached.isEmpty()) {
            // 安全转换 Map<Object,Object> -> Map<String,String>
            Map<String, String> safeMap = new HashMap<>();
            cached.forEach((k, v) -> {
                if (k != null && v != null) {
                    safeMap.put(k.toString(), v.toString());
                }
            });
            return safeMap;
        }

        // 2. 从数据库查询
        List<Map<String, String>> translationsByEntity = this.getEntityDao().findTranslationsByEntity(
                entityType, entityId, locale);
        Map<String, String> translations = new HashMap<>();
        for (Map<String, String> row : translationsByEntity) {
            translations.put(row.get("field_name"), row.get("translation"));
        }
        // 3. 缓存结果
        if (translations != null && !translations.isEmpty()) {
            redisUtil.set(cacheKey, translations, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        } else {
            // 缓存空Map
            redisUtil.set(cacheKey, Collections.emptyMap(), 5, TimeUnit.MINUTES);
        }

        return translations != null ? translations : Collections.emptyMap();
    }

    /**
     * 构建实体的缓存键（包含所有字段）
     * 格式：i18n:{entityType}:{entityId}:{locale}
     */
    private String buildEntityCacheKey(String entityType, Long entityId, String locale) {
        return String.format("%s%s:%d:%s",
                CACHE_PREFIX, entityType, entityId, locale);
    }

}
