/*
 * acooly.cn Inc.
 * Copyright (c) 2023 All Rights Reserved.
 * create by acooly
 * date:2023-12-14
 */
package com.acooly.showcase.daliy.service.Impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.RegnameService;
import com.acooly.showcase.daliy.dao.RegnameDao;
import com.acooly.showcase.daliy.entity.Regname;

/**
 * dm_regname Service实现
 *
 * @author acooly
 * @date 2023-12-14 17:39:27
 */
@Service("regnameService")
public class RegnameServiceImpl extends EntityServiceImpl<Regname, RegnameDao> implements RegnameService {

}
