/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 *
 */
package com.acooly.showcase.jpa.goods.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.jpa.goods.entity.GoodsCategory;

import java.util.List;

/**
 * 商品分类 Service接口
 *
 * <p>Date: 2017-09-12 22:42:00
 *
 * @author acooly
 */
public interface GoodsCategoryService extends EntityService<GoodsCategory> {
  GoodsCategory create(Long parentId, String name, String comments);

  List<GoodsCategory> loadTree(Long parentId);

  List<GoodsCategory> loadTops();
}
