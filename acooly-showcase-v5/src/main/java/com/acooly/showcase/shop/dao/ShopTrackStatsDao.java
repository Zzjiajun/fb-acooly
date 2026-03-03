/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopTrackStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.List;

/**
 * 访问统计汇总表（提升查询性能） Mybatis Dao
 *
 * @author acooly
 * @date 2026-01-04 19:04:50
 */
public interface ShopTrackStatsDao extends EntityMybatisDao<ShopTrackStats> {

    /**
     * 唯一索引查询: uk_stat
     *
     * @param statType
     * @param statDate
     * @param entityId
     * @param entityKey
     * @param locale
     * @return
     */
    @Select("select * from shop_track_stats where stat_type=#{statType} AND stat_date=#{statDate} AND entity_id=#{entityId} AND entity_key=#{entityKey} AND locale=#{locale}")
    ShopTrackStats uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(@Param("statType") String statType, @Param("statDate") Date statDate, @Param("entityId") Long entityId, @Param("entityKey") String entityKey, @Param("locale") String locale);

    /**
     * 查询网站统计（daily_site类型）
     *
     * @param statDate 统计日期
     * @return 统计信息
     */
    @Select("SELECT * FROM shop_track_stats WHERE stat_type = 'daily_site' AND DATE(stat_date) = DATE(#{statDate}) LIMIT 1")
    ShopTrackStats findSiteStats(@Param("statDate") Date statDate);

}
