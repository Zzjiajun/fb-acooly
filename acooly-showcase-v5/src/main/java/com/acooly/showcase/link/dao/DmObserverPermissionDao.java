/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-06-20
 */
 package com.acooly.showcase.link.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.link.entity.DmObserverPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * dm_observer_permission Mybatis Dao
 *
 * @author acooly
 * @date 2025-06-20 21:45:07
 */
public interface DmObserverPermissionDao extends EntityMybatisDao<DmObserverPermission> {

    /**
     * 唯一索引查询: uk_user_center
     *
     * @param userId
     * @param dmCenterId
     * @return
     */
    @Select("select * from dm_observer_permission where user_id=#{userId} AND dm_center_id=#{dmCenterId}")
    DmObserverPermission uniqueByUserIdAndDmCenterId(@Param("userId") Long userId, @Param("dmCenterId") Long dmCenterId);

    @Select("select * from dm_observer_permission where user_id=#{userId} AND status=#{status}")
    List<DmObserverPermission> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") int status);


    @Select("select * from dm_observer_permission where user_id=#{userId} AND dm_center_id=#{dmCenterId} AND status=#{status}")
    int existsByUserIdAndDmCenterIdAndStatus(@Param("userId")Long userId, @Param("dmCenterId")Long dmCenterId, @Param("status")int i);
}
