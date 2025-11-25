/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-07
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopCouponService;
import com.acooly.showcase.shop.dao.ShopCouponDao;
import com.acooly.showcase.shop.entity.ShopCoupon;

/**
 * shop_coupon Service实现
 *
 * @author acooly
 * @date 2025-11-07 17:01:53
 */
@Service("shopCouponService")
public class ShopCouponServiceImpl extends EntityServiceImpl<ShopCoupon, ShopCouponDao> implements ShopCouponService {

    /**
     * 唯一性查询
     * uk_code
     *
     * @param code
     * @return
     */
    @Override
    public ShopCoupon uniqueByCode(String code) {
        return getEntityDao().uniqueByCode(code);
    }

}
