/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopTrackLog;
import java.util.Date;
import java.util.List;

/**
 * 访问行为日志表（按日期分区，建议按月归档） Service接口
 *
 * @author acooly
 * @date 2026-01-04 19:04:49
 */
public interface ShopTrackLogService extends EntityService<ShopTrackLog> {

    /**
     * 根据日期和页面类型查询日志
     *
     * @param date 访问日期
     * @param pageType 页面类型（可选）
     * @return 日志列表
     */
    List<ShopTrackLog> findByDateAndPageType(Date date, String pageType);

    /**
     * 根据日期查询有商品ID的日志
     *
     * @param date 访问日期
     * @return 日志列表
     */
    List<ShopTrackLog> findByDateAndProductIdNotNull(Date date);

    /**
     * 根据日期和商品ID查询日志
     *
     * @param date 访问日期
     * @param productId 商品ID
     * @return 日志列表
     */
    List<ShopTrackLog> findByDateAndProductId(Date date, Long productId);

    /**
     * 根据日期范围查询日志
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageType 页面类型（可选）
     * @return 日志列表
     */
    List<ShopTrackLog> findByDateRange(Date startDate, Date endDate, String pageType);

    /**
     * 根据日期范围查询有商品ID的日志
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日志列表
     */
    List<ShopTrackLog> findByDateRangeAndProductIdNotNull(Date startDate, Date endDate);

}
