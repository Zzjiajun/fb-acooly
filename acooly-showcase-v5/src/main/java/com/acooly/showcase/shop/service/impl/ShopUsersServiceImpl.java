/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopUsersService;
import com.acooly.showcase.shop.dao.ShopUsersDao;
import com.acooly.showcase.shop.entity.ShopUsers;

/**
 * 用户表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopUsersService")
public class ShopUsersServiceImpl extends EntityServiceImpl<ShopUsers, ShopUsersDao> implements ShopUsersService {

    /**
     * 唯一性查询
     * email
     *
     * @param email
     * @return
     */
    @Override
    public ShopUsers uniqueByEmail(String email) {
        return getEntityDao().uniqueByEmail(email);
    }

    @Override
    public int changePassword(ShopUsers shopUsers) {
        return this.getEntityDao().changePassword(shopUsers.getId(), shopUsers.getPassword());
    }

}
