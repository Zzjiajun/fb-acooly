/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopUsers;
/**
 * 用户表 Service接口
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopUsersService extends EntityService<ShopUsers> {

    /**
     * 唯一性查询
     * email
     *
     * @param email
     * @return
     */
    ShopUsers uniqueByEmail(String email);

    int changePassword(ShopUsers shopUsers);


}
