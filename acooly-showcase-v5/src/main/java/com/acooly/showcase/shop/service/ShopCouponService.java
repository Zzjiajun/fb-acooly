/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-07
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopCoupon;
/**
 * shop_coupon Service接口
 *
 * @author acooly
 * @date 2025-11-07 17:01:53
 */
public interface ShopCouponService extends EntityService<ShopCoupon> {

    /**
     * 唯一性查询
     * uk_code
     *
     * @param code
     * @return
     */
    ShopCoupon uniqueByCode(String code);

}
