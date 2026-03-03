/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopBrandService;
import com.acooly.showcase.shop.dao.ShopBrandDao;
import com.acooly.showcase.shop.entity.ShopBrand;

/**
 * 品牌表 Service实现
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Service("shopBrandService")
public class ShopBrandServiceImpl extends EntityServiceImpl<ShopBrand, ShopBrandDao> implements ShopBrandService {

    /**
     * 唯一性查询
     * uk_shop_brand_name
     *
     * @param name
     * @return
     */
    @Override
    public ShopBrand uniqueByName(String name) {
        return getEntityDao().uniqueByName(name);
    }

}
