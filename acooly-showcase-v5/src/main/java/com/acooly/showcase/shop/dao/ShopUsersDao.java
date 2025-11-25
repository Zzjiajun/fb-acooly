/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopUsers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopUsersDao extends EntityMybatisDao<ShopUsers> {

    /**
     * 唯一索引查询: email
     *
     * @param email
     * @return
     */
    @Select("select * from shop_users where email=#{email}")
    ShopUsers uniqueByEmail(@Param("email") String email);

    @Update({
            "<script>",
            "UPDATE shop_users",
            "SET password = #{password}",
            "WHERE id = #{id}",
            "</script>"
    })
    int changePassword(@Param("id") Long id, @Param("password") String password);
}
