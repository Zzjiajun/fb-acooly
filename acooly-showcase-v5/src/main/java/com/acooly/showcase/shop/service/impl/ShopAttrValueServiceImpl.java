/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopAttrValueService;
import com.acooly.showcase.shop.dao.ShopAttrValueDao;
import com.acooly.showcase.shop.entity.ShopAttrValue;

/**
 * 商品属性值表 Service实现
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Service("shopAttrValueService")
public class ShopAttrValueServiceImpl extends EntityServiceImpl<ShopAttrValue, ShopAttrValueDao> implements ShopAttrValueService {

    /**
     * 唯一性查询
     * uk_shop_attr_value
     *
     * @param attrId
     * @param value
     * @return
     */
    @Override
    public ShopAttrValue uniqueByAttrIdAndValue(Long attrId, String value) {
        return getEntityDao().uniqueByAttrIdAndValue(attrId, value);
    }

}
