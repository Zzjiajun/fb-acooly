/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-19
 */
package com.acooly.showcase.member.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.member.entity.ShowcaseMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员信息 Mybatis Dao
 *
 * @author acooly
 * @date 2021-10-19 14:23:36
 */
public interface ShowcaseMemberDao extends EntityMybatisDao<ShowcaseMember> {

    /**
     * 根据用户名查询主体
     *
     * @param username
     * @return
     */
    @Select("select * from ac_showcase_member where username=#{username}")
    ShowcaseMember findByUsername(@Param("username") String username);

    /**
     * 根据会员编码查询
     *
     * @param memberNo
     * @return
     */
    @Select("select * from ac_showcase_member where member_no=#{memberNo}")
    ShowcaseMember findByMemberNo(@Param("memberNo") String memberNo);

}
