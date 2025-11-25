/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopOrders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopOrdersDao extends EntityMybatisDao<ShopOrders> {

    /**
     * 唯一索引查询: order_id
     *
     * @param orderId
     * @return
     */
    @Select("select * from shop_orders where order_id=#{orderId}")
    ShopOrders uniqueByOrderId(@Param("orderId") String orderId);

}
