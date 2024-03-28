/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-25
 */
package com.acooly.showcase.daliy.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.DmShowService;
import com.acooly.showcase.daliy.dao.DmShowDao;
import com.acooly.showcase.daliy.entity.DmShow;

/**
 * dm_show Service实现
 *
 * @author acooly
 * @date 2024-03-25 10:30:45
 */
@Service("dmShowService")
public class DmShowServiceImpl extends EntityServiceImpl<DmShow, DmShowDao> implements DmShowService {

}
