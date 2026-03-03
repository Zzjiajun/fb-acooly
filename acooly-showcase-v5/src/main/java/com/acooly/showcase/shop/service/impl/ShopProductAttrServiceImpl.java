/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
package com.acooly.showcase.shop.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.dao.ShopProductAttrDao;
import com.acooly.showcase.shop.entity.ShopProductAttr;
import com.acooly.showcase.shop.service.ShopProductAttrService;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品属性关联表 Service实现
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Slf4j
@Service("shopProductAttrService")
public class ShopProductAttrServiceImpl extends EntityServiceImpl<ShopProductAttr, ShopProductAttrDao> implements ShopProductAttrService {

    @Autowired
    private ShopProductAttrService shopProductAttrService;

    @Override
    public List<ShopProductAttr> findByProductId(Long productId) {
        if (productId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_productId", productId);
        List<ShopProductAttr> productAttrs = shopProductAttrService.query(params, null);
        return productAttrs;
    }

    @Override
    public void deleteByProductId(Long productId) {
        if (productId == null) {
            return;
        }
        List<ShopProductAttr> attrs = findByProductId(productId);
        if (attrs != null && !attrs.isEmpty()) {
            for (ShopProductAttr attr : attrs) {
                removeById(Long.valueOf(attr.getId()));
            }
            log.info("删除商品所有属性关联 - 商品ID: {}, 属性数量: {}", productId, attrs.size());
        }
    }
}
