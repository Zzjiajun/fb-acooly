/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-26
 *
 */
package com.acooly.showcase.daliy.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.system.dto.DmCenterDto;
import com.acooly.showcase.daliy.system.dto.ParameterStatusDto;

import java.util.List;

/**
 * dm_center Service接口
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
public interface DmCenterService extends EntityService<DmCenter> {
    List<ParameterStatusDto> countParameterStatus();

    List<DmCenterDto> countUserRegionType();

}
