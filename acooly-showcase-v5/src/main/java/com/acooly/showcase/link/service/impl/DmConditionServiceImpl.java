/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-02
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmConditionService;
import com.acooly.showcase.link.dao.DmConditionDao;
import com.acooly.showcase.link.entity.DmCondition;

/**
 * dm_condition Service实现
 *
 * @author acooly
 * @date 2024-06-02 19:10:44
 */
@Service("dmConditionService")
public class DmConditionServiceImpl extends EntityServiceImpl<DmCondition, DmConditionDao> implements DmConditionService {

}
