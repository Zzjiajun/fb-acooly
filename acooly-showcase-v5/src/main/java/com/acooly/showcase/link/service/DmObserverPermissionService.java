/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-06-20
 *
 */
package com.acooly.showcase.link.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.link.entity.DmObserverPermission;

import java.util.List;

/**
 * dm_observer_permission Service接口
 *
 * @author acooly
 * @date 2025-06-20 21:45:07
 */
public interface DmObserverPermissionService extends EntityService<DmObserverPermission> {

    /**
     * 唯一性查询
     * uk_user_center
     *
     * @param userId
     * @param dmCenterId
     * @return
     */
    DmObserverPermission uniqueByUserIdAndDmCenterId(Long userId, Long dmCenterId);

    /**
     * 检查是否是观察者
     * @param userId
     * @return
     */
    boolean isObserver(Long userId);

    /**
     * 为观察者分配dmCenter记录查看权限
     * @param userId
     * @param dmCenterId
     * @param grantBy
     */
    void grantPermission(Long userId, Long dmCenterId, String grantBy);

    /**
     * 检查是否有权限访问dmCenter记录
     * @param userId
     * @param dmCenterId
     * @return
     */
    boolean hasPermission(Long userId, Long dmCenterId);


    /**
     * 获取观察者可访问的dmCenterId列表
     * @param userId
     * @return
     */
    List<Long> getAccessibleDmCenterIds(Long userId);


    /**
     * 获取所有观察者用户
     */
    List<User> getAllObservers();

}
