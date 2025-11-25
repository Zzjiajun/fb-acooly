/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-01
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopCartItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 购物车表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-11-01 23:25:13
 */
public interface ShopCartItemDao extends EntityMybatisDao<ShopCartItem> {

    /**
     * 唯一索引查询: uk_user_product
     *
     * @param userId
     * @param productId
     * @return
     */
    @Select("select * from shop_cart_item where user_id=#{userId} AND product_id=#{productId}")
    ShopCartItem uniqueByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

}
