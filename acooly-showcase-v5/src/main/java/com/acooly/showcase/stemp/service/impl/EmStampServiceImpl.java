/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-02-15
 */
package com.acooly.showcase.stemp.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.stemp.service.EmStampService;
import com.acooly.showcase.stemp.dao.EmStampDao;
import com.acooly.showcase.stemp.entity.EmStamp;

/**
 * em_stamp Service实现
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Service("emStampService")
public class EmStampServiceImpl extends EntityServiceImpl<EmStamp, EmStampDao> implements EmStampService {

}
