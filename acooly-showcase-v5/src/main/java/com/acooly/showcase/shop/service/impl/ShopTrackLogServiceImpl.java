/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopTrackLogService;
import com.acooly.showcase.shop.dao.ShopTrackLogDao;
import com.acooly.showcase.shop.entity.ShopTrackLog;
import java.util.Date;
import java.util.List;

/**
 * 访问行为日志表（按日期分区，建议按月归档） Service实现
 *
 * @author acooly
 * @date 2026-01-04 19:04:49
 */
@Service("shopTrackLogService")
public class ShopTrackLogServiceImpl extends EntityServiceImpl<ShopTrackLog, ShopTrackLogDao> implements ShopTrackLogService {

    @Override
    public List<ShopTrackLog> findByDateAndPageType(Date date, String pageType) {
        return getEntityDao().findByDateAndPageType(date, pageType);
    }

    @Override
    public List<ShopTrackLog> findByDateAndProductIdNotNull(Date date) {
        return getEntityDao().findByDateAndProductIdNotNull(date);
    }

    @Override
    public List<ShopTrackLog> findByDateAndProductId(Date date, Long productId) {
        return getEntityDao().findByDateAndProductId(date, productId);
    }

    @Override
    public List<ShopTrackLog> findByDateRange(Date startDate, Date endDate, String pageType) {
        return getEntityDao().findByDateRange(startDate, endDate, pageType);
    }

    @Override
    public List<ShopTrackLog> findByDateRangeAndProductIdNotNull(Date startDate, Date endDate) {
        return getEntityDao().findByDateRangeAndProductIdNotNull(startDate, endDate);
    }

}
