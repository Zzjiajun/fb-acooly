/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopI18nLocales;

import java.util.List;

/**
 * 支持的语言列表 Service接口
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
public interface ShopI18nLocalesService extends EntityService<ShopI18nLocales> {

    /**
     * 唯一性查询
     * uk_locale_code
     *
     * @param localeCode
     * @return
     */
    ShopI18nLocales uniqueByLocaleCode(String localeCode);

    /**
     * 获取所有启用的语言列表（按排序顺序）
     * @return 启用的语言列表
     */
    List<ShopI18nLocales> getActiveLocales();

    /**
     * 获取默认语言
     * @return 默认语言，如果没有则返回null
     */
    ShopI18nLocales getDefaultLocale();

}
