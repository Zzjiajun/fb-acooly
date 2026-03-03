/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-12
 */
package com.acooly.showcase.shop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopTeamUserMappingService;
import com.acooly.showcase.shop.dao.ShopTeamUserMappingDao;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;

/**
 * shop_team_user_mapping Service实现
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
@Service("shopTeamUserMappingService")
public class ShopTeamUserMappingServiceImpl extends EntityServiceImpl<ShopTeamUserMapping, ShopTeamUserMappingDao> implements ShopTeamUserMappingService {

    /**
     * 唯一性查询
     * user_id
     *
     * @param userId
     * @return
     */
    @Override
    public ShopTeamUserMapping uniqueByUserId(Long userId) {
        return getEntityDao().uniqueByUserId(userId);
    }

    /**
     * 根据teamId查询所有映射关系
     *
     * @param teamId
     * @return
     */
    @Override
    public List<ShopTeamUserMapping> findByTeamId(Long teamId) {
        return getEntityDao().findByTeamId(teamId);
    }

}
