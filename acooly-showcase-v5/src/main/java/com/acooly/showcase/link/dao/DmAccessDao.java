/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 */
 package com.acooly.showcase.link.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.link.entity.DmAccess;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * dm_access Mybatis Dao
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
public interface DmAccessDao extends EntityMybatisDao<DmAccess> {


   @Select("SELECT COUNT(DISTINCT ip) FROM dm_access")
   Integer countDistinctIp();
   @Delete("DELETE FROM dm_access")
   void deleteAll();

   @Delete("TRUNCATE TABLE dm_access")
   void truncateYourTable();
   //全球IP分布
   @Select("SELECT SUBSTRING_INDEX(region, '-', 1) AS name, COUNT(DISTINCT ip) AS value FROM dm_access WHERE region IS NOT NULL AND region != '' GROUP BY name ORDER BY value DESC")
   List<Map<String, Object>> selectWorldIpDistribution();
//   @Select("SELECT region AS name, COUNT(DISTINCT ip) AS value FROM dm_access WHERE region IS NOT NULL AND region != '' GROUP BY region ORDER BY value DESC")
//   List<Map<String, Object>> selectWorldIpDistribution();
   //供应商统计
   @Select("SELECT ip_details AS name, COUNT(*) AS value FROM dm_access WHERE ip_details IS NOT NULL AND ip_details != '' GROUP BY ip_details ORDER BY value DESC LIMIT 10")
   List<Map<String, Object>> selectSupplierStats();
   //趋势图
   @Select("SELECT DATE(create_time) AS date, COUNT(DISTINCT ip) AS ipCount, COUNT(*) AS visitCount FROM dm_access WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) GROUP BY DATE(create_time) ORDER BY date ASC")
   List<Map<String, Object>> selectTrend();

   @Select("SELECT region AS name, COUNT(*) AS value FROM dm_access WHERE region IS NOT NULL AND region != '' GROUP BY region ORDER BY value DESC LIMIT 10")
   List<Map<String, Object>> selectUserStats();
}
