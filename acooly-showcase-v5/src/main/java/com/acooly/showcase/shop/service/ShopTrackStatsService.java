/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopTrackStats;
import java.util.Date;
import java.util.List;
import com.acooly.showcase.shop.dto.SiteStatsDTO;
import com.acooly.showcase.shop.dto.PageStatsDTO;
import com.acooly.showcase.shop.dto.IpStatsDTO;
import com.acooly.showcase.shop.dto.ProductStatsDTO;

/**
 * 访问统计汇总表（提升查询性能） Service接口
 *
 * @author acooly
 * @date 2026-01-04 19:04:50
 */
public interface ShopTrackStatsService extends EntityService<ShopTrackStats> {

    /**
     * 唯一性查询
     * uk_stat
     *
     * @param statType
     * @param statDate
     * @param entityId
     * @param entityKey
     * @param locale
     * @return
     */
    ShopTrackStats uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(String statType, Date statDate, Long entityId, String entityKey, String locale);

    /**
     * 查询网站总体统计
     *
     * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
     * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
     * @return 网站统计信息
     */
    SiteStatsDTO getSiteStats(String date, String startDate, String endDate);

    /**
     * 查询页面统计列表
     *
     * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
     * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param pageType 页面类型（可选），如果为null则查询所有类型
     * @return 页面统计列表
     */
    List<PageStatsDTO> getPageStats(String date, String startDate, String endDate, String pageType);

    /**
     * 查询IP分布统计
     *
     * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
     * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param limit 返回数量限制，默认20
     * @return IP分布统计列表
     */
    List<IpStatsDTO> getIpStats(String date, String startDate, String endDate, Integer limit);

    /**
     * 查询商品浏览排行
     *
     * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
     * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
     * @param limit 返回数量限制，默认20
     * @param orderBy 排序字段：pv（访问量）或 avgDuration（平均停留时间），默认pv
     * @return 商品统计列表
     */
    List<ProductStatsDTO> getProductRanking(String date, String startDate, String endDate, Integer limit, String orderBy);

    /**
     * 每日统计聚合（每天凌晨2点执行）
     */
    void aggregateDailyStats();

    /**
     * 统计聚合（手动触发或定时任务调用）
     *
     * @param date 统计日期（格式：yyyy-MM-dd），如果为null则聚合昨天的数据
     */
    void aggregateStats(String date);

    /**
     * 清理统计缓存
     *
     * @param date 指定日期（格式：yyyy-MM-dd），如果为null则清理所有缓存
     * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围
     * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围
     */
    void clearCache(String date, String startDate, String endDate);

}
