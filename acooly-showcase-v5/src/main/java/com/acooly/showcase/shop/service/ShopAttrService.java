/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopAttr;
/**
 * 商品属性表 Service接口
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopAttrService extends EntityService<ShopAttr> {

    /**
     * 唯一性查询
     * uk_shop_attr_name
     *
     * @param name
     * @return
     */
    ShopAttr uniqueByName(String name);

}
