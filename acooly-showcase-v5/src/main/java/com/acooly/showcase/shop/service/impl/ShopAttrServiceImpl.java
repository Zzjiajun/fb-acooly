/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopAttrService;
import com.acooly.showcase.shop.dao.ShopAttrDao;
import com.acooly.showcase.shop.entity.ShopAttr;

/**
 * 商品属性表 Service实现
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Service("shopAttrService")
public class ShopAttrServiceImpl extends EntityServiceImpl<ShopAttr, ShopAttrDao> implements ShopAttrService {

    /**
     * 唯一性查询
     * uk_shop_attr_name
     *
     * @param name
     * @return
     */
    @Override
    public ShopAttr uniqueByName(String name) {
        return getEntityDao().uniqueByName(name);
    }

}
