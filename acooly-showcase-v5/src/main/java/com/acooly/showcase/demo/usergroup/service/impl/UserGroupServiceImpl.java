/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by zhangpu@acooly.cn
 * date:2021-12-19
 */
package com.acooly.showcase.demo.usergroup.service.impl;

import com.acooly.core.utils.Collections3;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.demo.usergroup.service.UserGroupService;
import com.acooly.showcase.demo.usergroup.dao.UserGroupDao;
import com.acooly.showcase.demo.usergroup.entity.UserGroup;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 用户动态分组 Service实现
 *
 * @author zhangpu@acooly.cn
 * @date 2021-12-19 16:45:03
 */
@Service("userGroupService")
public class UserGroupServiceImpl extends EntityServiceImpl<UserGroup, UserGroupDao> implements UserGroupService {


    @Override
    @Transactional(rollbackOn = Throwable.class)
    public void batchSave(List<UserGroup> userGroups) {
        // merge delete
        UserGroup userGroup = Collections3.getFirst(userGroups);
        Map<Long, Long> requestIds = Maps.newHashMap();
        for (UserGroup ug : userGroups) {
            if (ug.getId() == null) {
                continue;
            }
            requestIds.put(ug.getId(), ug.getId());
        }
        List<Long> removeIds = Lists.newArrayList();
        List<UserGroup> exists = getAll();
        for (UserGroup exist : exists) {
            if (requestIds.get(exist.getId()) == null) {
                removeIds.add(exist.getId());
            }
        }

        saves(userGroups);
        if (Collections3.isNotEmpty(removeIds)) {
            removes(removeIds.toArray(new Long[]{}));
        }
    }
}
