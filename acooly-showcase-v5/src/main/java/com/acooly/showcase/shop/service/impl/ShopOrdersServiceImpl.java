/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopOrdersService;
import com.acooly.showcase.shop.dao.ShopOrdersDao;
import com.acooly.showcase.shop.entity.ShopOrders;

/**
 * 订单表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopOrdersService")
public class ShopOrdersServiceImpl extends EntityServiceImpl<ShopOrders, ShopOrdersDao> implements ShopOrdersService {

    /**
     * 唯一性查询
     * order_id
     *
     * @param orderId
     * @return
     */
    @Override
    public ShopOrders uniqueByOrderId(String orderId) {
        return getEntityDao().uniqueByOrderId(orderId);
    }

}
