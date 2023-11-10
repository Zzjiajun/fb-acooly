/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.showcase.jpa.goods.entity.Goods;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品信息 Mybatis Dao
 *
 * <p>Date: 2017-09-12 22:42:00
 *
 * @author acooly
 */
public interface GoodsDao extends EntityJpaDao<Goods, Long> {

  @Query(value = "select * from dm_goods where name like ?1", nativeQuery = true)
  List<Goods> search(String key);
}
