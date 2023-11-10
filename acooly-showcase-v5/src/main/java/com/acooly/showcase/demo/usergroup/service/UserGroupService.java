/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by zhangpu@acooly.cn
 * date:2021-12-19
 *
 */
package com.acooly.showcase.demo.usergroup.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.demo.usergroup.entity.UserGroup;

import java.util.List;

/**
 * 用户动态分组 Service接口
 *
 * @author zhangpu@acooly.cn
 * @date 2021-12-19 16:45:03
 */
public interface UserGroupService extends EntityService<UserGroup> {

    /**
     * 批量merge保存
     *
     * @param userGroups
     */
    void batchSave(List<UserGroup> userGroups);

}
