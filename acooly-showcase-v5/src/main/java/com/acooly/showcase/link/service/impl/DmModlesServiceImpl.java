/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-10-31
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmModlesService;
import com.acooly.showcase.link.dao.DmModlesDao;
import com.acooly.showcase.link.entity.DmModles;

/**
 * dm_modles Service实现
 *
 * @author acooly
 * @date 2024-10-31 23:52:11
 */
@Service("dmModlesService")
public class DmModlesServiceImpl extends EntityServiceImpl<DmModles, DmModlesDao> implements DmModlesService {

}
