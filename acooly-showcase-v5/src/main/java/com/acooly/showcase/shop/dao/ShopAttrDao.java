/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopAttr;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品属性表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopAttrDao extends EntityMybatisDao<ShopAttr> {

    /**
     * 唯一索引查询: uk_shop_attr_name
     *
     * @param name
     * @return
     */
    @Select("select * from shop_attr where name=#{name}")
    ShopAttr uniqueByName(@Param("name") String name);

}
