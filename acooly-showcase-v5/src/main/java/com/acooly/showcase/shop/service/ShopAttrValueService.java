/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopAttrValue;
/**
 * 商品属性值表 Service接口
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopAttrValueService extends EntityService<ShopAttrValue> {

    /**
     * 唯一性查询
     * uk_shop_attr_value
     *
     * @param attrId
     * @param value
     * @return
     */
    ShopAttrValue uniqueByAttrIdAndValue(Long attrId, String value);

}
