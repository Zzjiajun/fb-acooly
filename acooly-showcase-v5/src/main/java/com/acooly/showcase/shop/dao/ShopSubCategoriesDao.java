/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopSubCategories;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 子级分类表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopSubCategoriesDao extends EntityMybatisDao<ShopSubCategories> {

    /**
     * 唯一索引查询: uk_parent_slug
     *
     * @param parentId
     * @param slug
     * @return
     */
    @Select("select * from shop_sub_categories where parent_id=#{parentId} AND slug=#{slug}")
    ShopSubCategories uniqueByParentIdAndSlug(@Param("parentId") Long parentId, @Param("slug") String slug);

}
