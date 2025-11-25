/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-01
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopCartItemService;
import com.acooly.showcase.shop.dao.ShopCartItemDao;
import com.acooly.showcase.shop.entity.ShopCartItem;

/**
 * 购物车表 Service实现
 *
 * @author acooly
 * @date 2025-11-01 23:25:13
 */
@Service("shopCartItemService")
public class ShopCartItemServiceImpl extends EntityServiceImpl<ShopCartItem, ShopCartItemDao> implements ShopCartItemService {

    /**
     * 唯一性查询
     * uk_user_product
     *
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public ShopCartItem uniqueByUserIdAndProductId(Long userId, Long productId) {
        return getEntityDao().uniqueByUserIdAndProductId(userId, productId);
    }

}
