/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-27
 */
package com.acooly.showcase.member.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.member.entity.ShowcaseMemberProfile;
import org.apache.ibatis.annotations.Select;

/**
 * 会员配置信息 Mybatis Dao
 *
 * @author acooly
 * @date 2021-10-27 13:24:15
 */
public interface ShowcaseMemberProfileDao extends EntityMybatisDao<ShowcaseMemberProfile> {
    /**
     * 全表count
     *
     * @return
     */
    @Select("select count(*) from ac_showcase_member_profile")
    int count();
}
