/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-06-20
 */
package com.acooly.showcase.link.service.impl;

import com.acooly.module.security.domain.Role;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.dto.UserRole;
import com.acooly.module.security.service.RoleService;
import com.acooly.module.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmObserverPermissionService;
import com.acooly.showcase.link.dao.DmObserverPermissionDao;
import com.acooly.showcase.link.entity.DmObserverPermission;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.ArrayList;

/**
 * dm_observer_permission Service实现
 *
 * @author acooly
 * @date 2025-06-20 21:45:07
 */
@Slf4j
@Service("dmObserverPermissionService")
public class DmObserverPermissionServiceImpl extends EntityServiceImpl<DmObserverPermission, DmObserverPermissionDao> implements DmObserverPermissionService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final String ROLE_OBSERVER = "ROLE_OBSERVER";
    
    /**
     * 唯一性查询
     * uk_user_center
     *
     * @param userId
     * @param dmCenterId
     * @return
     */
    @Override
    public DmObserverPermission uniqueByUserIdAndDmCenterId(Long userId, Long dmCenterId) {
        return getEntityDao().uniqueByUserIdAndDmCenterId(userId, dmCenterId);
    }

    @Override
    public boolean isObserver(Long userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            // 获取用户角色列表
            List<UserRole> userRoles = userService.getRoleIdsByUserId(userId);
            if (userRoles == null || userRoles.isEmpty()) {
                return false;
            }
            
            // 获取所有角色ID并去重，使用Stream API高效处理
            return userRoles.stream()
                    .map(UserRole::getRoleId)
                    .filter(Objects::nonNull)
                    .distinct() // 去重，避免重复查询相同角色
                    .map(roleService::get)
                    .filter(Objects::nonNull)
                    .anyMatch(role -> ROLE_OBSERVER.equals(role.getName()));
                    
        } catch (Exception e) {
            // 记录异常日志，但不抛出异常，返回false
            // 这里可以添加日志记录：log.warn("检查用户{}是否为观察者时发生异常", userId, e);
            return false;
        }
    }

    // 为观察者分配dmCenter记录查看权限
    @Override
    public void grantPermission(Long userId, Long dmCenterId, String grantBy) {
        if (!isObserver(userId)) {
            throw new IllegalArgumentException("用户不是观察者角色");
        }
        DmObserverPermission permission = new DmObserverPermission();
        permission.setUserId(userId);
        permission.setDmCenterId(dmCenterId);
        permission.setGrantTime(new Date());
        permission.setGrantBy(grantBy);
        permission.setStatus(1);
        this.getEntityDao().insert(permission);
    }

    @Override
    public boolean hasPermission(Long userId, Long dmCenterId) {
        // 如果不是观察者，直接返回true
        if (!isObserver(userId)) {
            return true;
        }
        return this.getEntityDao().existsByUserIdAndDmCenterIdAndStatus(userId, dmCenterId, 1)>0;
    }

    // 获取观察者可以查看的所有dmCenter记录ID列表
    @Override
    public List<Long> getAccessibleDmCenterIds(Long userId) {
        if (userId == null) {
            return null;
        }
        
        // 如果不是观察者，返回null表示无限制
        if (!isObserver(userId)) {
            return null;
        }
        
        try {
            // 查询用户的有效权限记录
            List<DmObserverPermission> permissions = this.getEntityDao().findByUserIdAndStatus(userId, 1);
            
            // 直接提取dmCenterId并返回列表
            return permissions.stream()
                    .map(DmObserverPermission::getDmCenterId)
                    .filter(Objects::nonNull)
                    .distinct() // 去重，避免重复的dmCenterId
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            // 记录异常日志，返回空列表
            // 这里可以添加日志记录：log.warn("获取用户{}可访问的dmCenter列表时发生异常", userId, e);
            log.warn("获取用户{}可访问的dmCenter列表时发生异常", userId, e);
            return new ArrayList<>();
        }
    }

    // 获取所有观察者用户
    @Override
    public List<User> getAllObservers() {
        return userService.getAll().stream()
                .filter(user -> isObserver(user.getId()))
                .collect(Collectors.toList());
    }

}
