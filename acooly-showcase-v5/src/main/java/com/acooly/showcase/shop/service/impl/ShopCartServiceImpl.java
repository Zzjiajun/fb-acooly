/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-01
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopCartService;
import com.acooly.showcase.shop.dao.ShopCartDao;
import com.acooly.showcase.shop.entity.ShopCart;

/**
 * shop_cart Service实现
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Service("shopCartService")
public class ShopCartServiceImpl extends EntityServiceImpl<ShopCart, ShopCartDao> implements ShopCartService {

}
