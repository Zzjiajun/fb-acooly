/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-12
 *
 */
package com.acooly.showcase.shop.service;

import java.util.List;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;
/**
 * shop_team_user_mapping Service接口
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
public interface ShopTeamUserMappingService extends EntityService<ShopTeamUserMapping> {

    /**
     * 唯一性查询
     * user_id
     *
     * @param userId
     * @return
     */
    ShopTeamUserMapping uniqueByUserId(Long userId);

    /**
     * 根据teamId查询所有映射关系
     *
     * @param teamId
     * @return
     */
    List<ShopTeamUserMapping> findByTeamId(Long teamId);

}
