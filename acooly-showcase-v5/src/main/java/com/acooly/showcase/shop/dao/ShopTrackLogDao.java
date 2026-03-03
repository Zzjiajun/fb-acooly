/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopTrackLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.List;

/**
 * 访问行为日志表（按日期分区，建议按月归档） Mybatis Dao
 *
 * @author acooly
 * @date 2026-01-04 19:04:49
 */
public interface ShopTrackLogDao extends EntityMybatisDao<ShopTrackLog> {

    /**
     * 根据日期和页面类型查询日志
     *
     * @param date 访问日期
     * @param pageType 页面类型（可选）
     * @return 日志列表
     */
    @Select("<script>" +
            "SELECT * FROM shop_track_log WHERE DATE(visit_date) = DATE(#{date})" +
            "<if test='pageType != null and pageType != \"\"'> AND page_type = #{pageType}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<ShopTrackLog> findByDateAndPageType(@Param("date") Date date, @Param("pageType") String pageType);

    /**
     * 根据日期查询有商品ID的日志
     *
     * @param date 访问日期
     * @return 日志列表
     */
    @Select("SELECT * FROM shop_track_log WHERE DATE(visit_date) = DATE(#{date}) AND product_id IS NOT NULL ORDER BY create_time DESC")
    List<ShopTrackLog> findByDateAndProductIdNotNull(@Param("date") Date date);

    /**
     * 根据日期和商品ID查询日志
     *
     * @param date 访问日期
     * @param productId 商品ID
     * @return 日志列表
     */
    @Select("SELECT * FROM shop_track_log WHERE DATE(visit_date) = DATE(#{date}) AND product_id = #{productId} ORDER BY create_time DESC")
    List<ShopTrackLog> findByDateAndProductId(@Param("date") Date date, @Param("productId") Long productId);

    /**
     * 根据日期范围查询日志
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageType 页面类型（可选）
     * @return 日志列表
     */
    @Select("<script>" +
            "SELECT * FROM shop_track_log WHERE DATE(visit_date) BETWEEN DATE(#{startDate}) AND DATE(#{endDate})" +
            "<if test='pageType != null and pageType != \"\"'> AND page_type = #{pageType}</if>" +
            " ORDER BY visit_date DESC, create_time DESC" +
            "</script>")
    List<ShopTrackLog> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("pageType") String pageType);

    /**
     * 根据日期范围查询有商品ID的日志
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日志列表
     */
    @Select("SELECT * FROM shop_track_log WHERE DATE(visit_date) BETWEEN DATE(#{startDate}) AND DATE(#{endDate}) AND product_id IS NOT NULL ORDER BY visit_date DESC, create_time DESC")
    List<ShopTrackLog> findByDateRangeAndProductIdNotNull(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
