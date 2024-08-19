/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmClickService;
import com.acooly.showcase.link.dao.DmClickDao;
import com.acooly.showcase.link.entity.DmClick;

/**
 * dm_click Service实现
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Service("dmClickService")
public class DmClickServiceImpl extends EntityServiceImpl<DmClick, DmClickDao> implements DmClickService {

    @Override
    public void deleteAll() {
        this.getEntityDao().deleteAll();
    }
}
