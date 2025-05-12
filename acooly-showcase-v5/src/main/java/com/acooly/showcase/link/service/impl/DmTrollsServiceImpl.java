/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by jj
 * date:2024-09-25
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmTrollsService;
import com.acooly.showcase.link.dao.DmTrollsDao;
import com.acooly.showcase.link.entity.DmTrolls;

/**
 * dm_trolls Service实现
 *
 * @author jj
 * @date 2024-09-25 04:30:32
 */
@Service("dmTrollsService")
public class DmTrollsServiceImpl extends EntityServiceImpl<DmTrolls, DmTrollsDao> implements DmTrollsService {

    @Override
    public void deleteAll() {
        this.getEntityDao().truncateYourTable();
    }
}
