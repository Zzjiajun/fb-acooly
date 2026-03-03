/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-12
 */
 package com.acooly.showcase.shop.dao;

import java.util.List;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopTeamUserMapping;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * shop_team_user_mapping Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
public interface ShopTeamUserMappingDao extends EntityMybatisDao<ShopTeamUserMapping> {

    /**
     * 唯一索引查询: user_id
     *
     * @param userId
     * @return
     */
    @Select("select * from shop_team_user_mapping where user_id=#{userId}")
    ShopTeamUserMapping uniqueByUserId(@Param("userId") Long userId);

    /**
     * 根据teamId查询所有映射关系
     *
     * @param teamId
     * @return
     */
    @Select("select * from shop_team_user_mapping where team_id=#{teamId}")
    List<ShopTeamUserMapping> findByTeamId(@Param("teamId") Long teamId);

}
