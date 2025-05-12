/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by jj
 * date:2024-09-25
 */
 package com.acooly.showcase.link.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.link.entity.DmTrolls;
import org.apache.ibatis.annotations.Delete;

/**
 * dm_trolls Mybatis Dao
 *
 * @author jj
 * @date 2024-09-25 04:30:32
 */
public interface DmTrollsDao extends EntityMybatisDao<DmTrolls> {

    @Delete("TRUNCATE TABLE dm_trolls")
    void truncateYourTable();

}
