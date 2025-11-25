/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopParentCategories;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 父级分类表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-10-28 00:50:13
 */
public interface ShopParentCategoriesDao extends EntityMybatisDao<ShopParentCategories> {

    /**
     * 唯一索引查询: slug
     *
     * @param slug
     * @return
     */
    @Select("select * from shop_parent_categories where slug=#{slug}")
    ShopParentCategories uniqueBySlug(@Param("slug") String slug);

}
