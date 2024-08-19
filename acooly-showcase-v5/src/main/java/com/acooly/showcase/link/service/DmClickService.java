/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 *
 */
package com.acooly.showcase.link.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.link.entity.DmClick;
/**
 * dm_click Service接口
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
public interface DmClickService extends EntityService<DmClick> {

    void deleteAll();
}
