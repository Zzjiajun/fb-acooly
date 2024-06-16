/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-04-25
 */
package com.acooly.showcase.daliy.service.Impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.TonckService;
import com.acooly.showcase.daliy.dao.TonckDao;
import com.acooly.showcase.daliy.entity.Tonck;

/**
 * tonck Service实现
 *
 * @author acooly
 * @date 2024-04-25 03:17:48
 */
@Service("tonckService")
public class TonckServiceImpl extends EntityServiceImpl<Tonck, TonckDao> implements TonckService {

}
