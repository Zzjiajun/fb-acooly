/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopParentCategoriesService;
import com.acooly.showcase.shop.dao.ShopParentCategoriesDao;
import com.acooly.showcase.shop.entity.ShopParentCategories;

/**
 * 父级分类表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:13
 */
@Service("shopParentCategoriesService")
public class ShopParentCategoriesServiceImpl extends EntityServiceImpl<ShopParentCategories, ShopParentCategoriesDao> implements ShopParentCategoriesService {

    /**
     * 唯一性查询
     * slug
     *
     * @param slug
     * @return
     */
    @Override
    public ShopParentCategories uniqueBySlug(String slug) {
        return getEntityDao().uniqueBySlug(slug);
    }

}
