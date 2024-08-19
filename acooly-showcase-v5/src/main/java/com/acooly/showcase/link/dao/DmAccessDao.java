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
}
