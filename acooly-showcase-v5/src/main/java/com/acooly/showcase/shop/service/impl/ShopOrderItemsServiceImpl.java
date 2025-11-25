/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-01
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopOrderItemsService;
import com.acooly.showcase.shop.dao.ShopOrderItemsDao;
import com.acooly.showcase.shop.entity.ShopOrderItems;

/**
 * shop_order_items Service实现
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Service("shopOrderItemsService")
public class ShopOrderItemsServiceImpl extends EntityServiceImpl<ShopOrderItems, ShopOrderItemsDao> implements ShopOrderItemsService {

}
