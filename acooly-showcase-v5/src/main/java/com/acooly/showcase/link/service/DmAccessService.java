/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 *
 */
package com.acooly.showcase.link.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.link.entity.DmAccess;

import java.util.List;
import java.util.Map;

/**
 * dm_access Service接口
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
public interface DmAccessService extends EntityService<DmAccess> {

    Integer countDistinctIp();

    void deleteAll();

    List<Map<String, Object>> getWorldIpDistribution();

    List<Map<String, Object>> getSupplierStats();

    List<Map<String, Object>> getTrend();

    List<Map<String, Object>> getUserStats();
}
