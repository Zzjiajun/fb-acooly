/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopParentCategories;
/**
 * 父级分类表 Service接口
 *
 * @author acooly
 * @date 2025-10-28 00:50:13
 */
public interface ShopParentCategoriesService extends EntityService<ShopParentCategories> {

    /**
     * 唯一性查询
     * slug
     *
     * @param slug
     * @return
     */
    ShopParentCategories uniqueBySlug(String slug);

}
