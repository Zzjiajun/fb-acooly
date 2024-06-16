/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-26
 */
package com.acooly.showcase.daliy.service.Impl;

import com.acooly.showcase.daliy.system.dto.DmCenterDto;
import com.acooly.showcase.daliy.system.dto.ParameterStatusDto;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.dao.DmCenterDao;
import com.acooly.showcase.daliy.entity.DmCenter;

import java.util.List;

/**
 * dm_center Service实现
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
@Service("dmCenterService")
public class DmCenterServiceImpl extends EntityServiceImpl<DmCenter, DmCenterDao> implements DmCenterService {

    @Override
    public List<ParameterStatusDto> countParameterStatus() {
        return this.getEntityDao().countParameterStatus();
    }

    @Override
    public List<DmCenterDto> countUserRegionType() {
        return this.getEntityDao().countUserRegionType();
    }
}
