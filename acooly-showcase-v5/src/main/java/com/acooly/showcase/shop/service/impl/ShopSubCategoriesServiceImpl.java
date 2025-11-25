/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopSubCategoriesService;
import com.acooly.showcase.shop.dao.ShopSubCategoriesDao;
import com.acooly.showcase.shop.entity.ShopSubCategories;

/**
 * 子级分类表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopSubCategoriesService")
public class ShopSubCategoriesServiceImpl extends EntityServiceImpl<ShopSubCategories, ShopSubCategoriesDao> implements ShopSubCategoriesService {

    /**
     * 唯一性查询
     * uk_parent_slug
     *
     * @param parentId
     * @param slug
     * @return
     */
    @Override
    public ShopSubCategories uniqueByParentIdAndSlug(Long parentId, String slug) {
        return getEntityDao().uniqueByParentIdAndSlug(parentId, slug);
    }

}
