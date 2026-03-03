/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopBrand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 品牌表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopBrandDao extends EntityMybatisDao<ShopBrand> {

    /**
     * 唯一索引查询: uk_shop_brand_name
     *
     * @param name
     * @return
     */
    @Select("select * from shop_brand where name=#{name}")
    ShopBrand uniqueByName(@Param("name") String name);

}
