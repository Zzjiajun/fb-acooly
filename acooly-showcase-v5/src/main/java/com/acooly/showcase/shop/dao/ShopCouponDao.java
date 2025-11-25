/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-07
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopCoupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * shop_coupon Mybatis Dao
 *
 * @author acooly
 * @date 2025-11-07 17:01:53
 */
public interface ShopCouponDao extends EntityMybatisDao<ShopCoupon> {

    /**
     * 唯一索引查询: uk_code
     *
     * @param code
     * @return
     */
    @Select("select * from shop_coupon where code=#{code}")
    ShopCoupon uniqueByCode(@Param("code") String code);

}
