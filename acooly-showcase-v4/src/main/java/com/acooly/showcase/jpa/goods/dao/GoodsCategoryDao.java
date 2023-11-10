/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.showcase.jpa.goods.entity.GoodsCategory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品分类 Mybatis Dao
 *
 * <p>Date: 2017-09-12 22:42:01
 *
 * @author acooly
 */
public interface GoodsCategoryDao extends EntityJpaDao<GoodsCategory, Long> {

    @Query(nativeQuery = true, value = "select max(path) from dm_goods_category where parent_id = ?1")
    String getMaxPath(Long parentId);

    @Query(
            nativeQuery = true,
            value = "select max(path) from dm_goods_category where ISNULL(parent_id)"
    )
    String getTopMaxPath();

    List<GoodsCategory> findByPathStartingWith(String codePrefix);

    @Query("from GoodsCategory where parentId is null")
    List<GoodsCategory> getTops();
}
