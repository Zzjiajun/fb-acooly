/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopOrders;
/**
 * 订单表 Service接口
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopOrdersService extends EntityService<ShopOrders> {

    /**
     * 唯一性查询
     * order_id
     *
     * @param orderId
     * @return
     */
    ShopOrders uniqueByOrderId(String orderId);

}
