/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-25
 */
package com.acooly.showcase.daliy.service.Impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.DmRegionService;
import com.acooly.showcase.daliy.dao.DmRegionDao;
import com.acooly.showcase.daliy.entity.DmRegion;

/**
 * dm_region Service实现
 *
 * @author acooly
 * @date 2024-03-25 12:05:46
 */
@Service("dmRegionService")
public class DmRegionServiceImpl extends EntityServiceImpl<DmRegion, DmRegionDao> implements DmRegionService {

}
