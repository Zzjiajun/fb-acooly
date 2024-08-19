/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-06
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmCountryService;
import com.acooly.showcase.link.dao.DmCountryDao;
import com.acooly.showcase.link.entity.DmCountry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * dm_country Service实现
 *
 * @author acooly
 * @date 2024-06-06 04:06:20
 */
@Service("dmCountryService")
public class DmCountryServiceImpl extends EntityServiceImpl<DmCountry, DmCountryDao> implements DmCountryService {

    @Override
    @Async
    public void dmCenterRedis() throws IOException {
        URL url = new URL("https://xunpro.world/hotel/dmCenterRedis");
//        URL url = new URL("https://xunwor.top/hotel/dmCenterRedis");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    @Override
    @Async
    public void dmConditionRedis() throws IOException {
        URL url = new URL("https://xunpro.world/hotel/dmConditionRedis");
//        URL url = new URL("https://xunwor.top/hotel/dmConditionRedis");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }


}
