/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmAccessService;
import com.acooly.showcase.link.dao.DmAccessDao;
import com.acooly.showcase.link.entity.DmAccess;

/**
 * dm_access Service实现
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Service("dmAccessService")
public class DmAccessServiceImpl extends EntityServiceImpl<DmAccess, DmAccessDao> implements DmAccessService {

    @Override
    public Integer countDistinctIp() {
        return this.getEntityDao().countDistinctIp();
    }
}
