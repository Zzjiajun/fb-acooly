/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopProductAttr;

import java.util.List;

/**
 * 商品属性关联表 Service接口
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopProductAttrService extends EntityService<ShopProductAttr> {

    /**
     * 根据商品ID查询所有属性关联
     */
    List<ShopProductAttr> findByProductId(Long productId);

    /**
     * 删除商品的所有属性关联
     */
    void deleteByProductId(Long productId);
}
