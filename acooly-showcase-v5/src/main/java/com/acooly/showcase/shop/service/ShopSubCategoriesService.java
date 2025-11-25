/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopSubCategories;
/**
 * 子级分类表 Service接口
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopSubCategoriesService extends EntityService<ShopSubCategories> {

    /**
     * 唯一性查询
     * uk_parent_slug
     *
     * @param parentId
     * @param slug
     * @return
     */
    ShopSubCategories uniqueByParentIdAndSlug(Long parentId, String slug);

}
