/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-04-26
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmServerService;
import com.acooly.showcase.link.dao.DmServerDao;
import com.acooly.showcase.link.entity.DmServer;

/**
 * dm_server Service实现
 *
 * @author acooly
 * @date 2024-04-26 04:43:12
 */
@Service("dmServerService")
public class DmServerServiceImpl extends EntityServiceImpl<DmServer, DmServerDao> implements DmServerService {

}
