/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 多语言翻译表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
public interface ShopI18nTranslationsDao extends EntityMybatisDao<ShopI18nTranslations> {

    /**
     * 唯一索引查询: uk_i18n_translation
     *
     * @param entityType
     * @param entityId
     * @param fieldName
     * @param locale
     * @return
     */
    @Select("select * from shop_i18n_translations where entity_type=#{entityType} AND entity_id=#{entityId} AND field_name=#{fieldName} AND locale=#{locale}")
    ShopI18nTranslations uniqueByEntityTypeAndEntityIdAndFieldNameAndLocale(@Param("entityType") String entityType, @Param("entityId") Long entityId, @Param("fieldName") String fieldName, @Param("locale") String locale);

    @Select("select field_name, `translation` from shop_i18n_translations where entity_type = #{entityType} AND entity_id = #{entityId} AND locale = #{locale}")
    List<Map<String, String>> findTranslationsByEntity(@Param("entityType") String entityType, @Param("entityId") Long entityId, @Param("locale") String locale);
}
