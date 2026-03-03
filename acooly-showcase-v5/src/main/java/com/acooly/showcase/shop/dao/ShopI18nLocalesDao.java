/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 支持的语言列表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
public interface ShopI18nLocalesDao extends EntityMybatisDao<ShopI18nLocales> {

    /**
     * 唯一索引查询: uk_locale_code
     *
     * @param localeCode
     * @return
     */
    @Select("select * from shop_i18n_locales where locale_code=#{localeCode}")
    ShopI18nLocales uniqueByLocaleCode(@Param("localeCode") String localeCode);

}
