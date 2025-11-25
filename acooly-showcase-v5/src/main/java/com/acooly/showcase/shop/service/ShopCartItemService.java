/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-01
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopCartItem;
/**
 * 购物车表 Service接口
 *
 * @author acooly
 * @date 2025-11-01 23:25:13
 */
public interface ShopCartItemService extends EntityService<ShopCartItem> {

    /**
     * 唯一性查询
     * uk_user_product
     *
     * @param userId
     * @param productId
     * @return
     */
    ShopCartItem uniqueByUserIdAndProductId(Long userId, Long productId);

}
