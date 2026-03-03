/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-26
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopI18nLocalesService;
import com.acooly.showcase.shop.dao.ShopI18nLocalesDao;
import com.acooly.showcase.shop.entity.ShopI18nLocales;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支持的语言列表 Service实现
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Service("shopI18nLocalesService")
public class ShopI18nLocalesServiceImpl extends EntityServiceImpl<ShopI18nLocales, ShopI18nLocalesDao> implements ShopI18nLocalesService {

    /**
     * 唯一性查询
     * uk_locale_code
     *
     * @param localeCode
     * @return
     */
    @Override
    public ShopI18nLocales uniqueByLocaleCode(String localeCode) {
        return getEntityDao().uniqueByLocaleCode(localeCode);
    }

    /**
     * 获取所有启用的语言列表（按排序顺序）
     * 优化：使用数据库排序，充分利用 idx_is_active_sort 索引
     */
    @Override
    public List<ShopI18nLocales> getActiveLocales() {
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_isActive", 1); // 只查询启用的语言
        
        // 使用数据库排序，充分利用 idx_is_active_sort (is_active, sort_order) 索引
        Map<String, Boolean> sortMap = new HashMap<>();
        sortMap.put("sortOrder", true); // true 表示升序
        
        return query(params, sortMap);
    }

    /**
     * 获取默认语言
     */
    @Override
    public ShopI18nLocales getDefaultLocale() {
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_isDefault", 1); // 查询默认语言
        params.put("EQ_isActive", 1);   // 且必须是启用的
        List<ShopI18nLocales> locales = query(params, null);
        
        if (locales != null && !locales.isEmpty()) {
            return locales.get(0);
        }
        
        return null;
    }

}
