/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-06
 *
 */
package com.acooly.showcase.link.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.link.entity.DmCountry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * dm_country Service接口
 *
 * @author acooly
 * @date 2024-06-06 04:06:20
 */
public interface DmCountryService extends EntityService<DmCountry> {
    void dmCenterRedis() throws IOException;

    void dmConditionRedis() throws IOException;

}
