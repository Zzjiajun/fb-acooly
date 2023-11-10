/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.member.entity.ShowcaseMemberFile;
import org.apache.ibatis.annotations.Select;

/**
 * 会员附件 Mybatis Dao
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
public interface ShowcaseMemberFileDao extends EntityMybatisDao<ShowcaseMemberFile> {

    /**
     * 全表count
     *
     * @return
     */
    @Select("select count(*) from ac_showcase_member_file")
    int count();


}
