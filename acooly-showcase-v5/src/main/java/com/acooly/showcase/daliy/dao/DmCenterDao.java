/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-26
 */
 package com.acooly.showcase.daliy.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.system.dto.DmCenterDto;
import com.acooly.showcase.daliy.system.dto.ParameterStatusDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * dm_center Mybatis Dao
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
public interface DmCenterDao extends EntityMybatisDao<DmCenter> {


 @Select("SELECT " +
         "region, " +
         "COUNT(*) AS allCount, " +
         "SUM(CASE WHEN display_option = 2 THEN 1 ELSE 0 END) AS landingCount, " +
         "SUM(CASE WHEN display_option = 1 THEN 1 ELSE 0 END) AS formsCount " +
         "FROM dm_center " +
         "GROUP BY region")
 List<ParameterStatusDto> countParameterStatus();


 @Select("SELECT user_name, region, display_option," +
         " SUM(visits_number) AS totalVisits," +
         " SUM(clicks_number) AS totalClicks " +
         "FROM dm_center " +
         "GROUP BY user_name, region, display_option")
 List<DmCenterDto> countUserRegionType();
}
