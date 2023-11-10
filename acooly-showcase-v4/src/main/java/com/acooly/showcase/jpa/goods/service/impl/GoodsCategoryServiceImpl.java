/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.jpa.goods.dao.GoodsCategoryDao;
import com.acooly.showcase.jpa.goods.entity.GoodsCategory;
import com.acooly.showcase.jpa.goods.service.GoodsCategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 商品分类 Service实现
 *
 * <p>Date: 2017-09-12 22:42:01
 *
 * @author acooly
 */
@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends EntityServiceImpl<GoodsCategory, GoodsCategoryDao>
    implements GoodsCategoryService {

  @Override
  public GoodsCategory create(Long parentId, String name, String comments) {
    String path = null;
    String maxPath = getMaxPath(parentId);
    if (StringUtils.isNotBlank(maxPath)) {
      path = String.valueOf(Long.parseLong(maxPath) + 1);
    } else {
      if (parentId == null) {
        path = "100";
      } else {
        GoodsCategory parent = get(parentId);
        path = parent.getPath() + "100";
      }
    }
    GoodsCategory category = new GoodsCategory();
    category.setPath(path);
    category.setCode(path);
    category.setComments(comments);
    category.setName(name);
    category.setParentId(parentId);
    category.setSortTime(System.currentTimeMillis());
    save(category);
    return category;
  }

  @Override
  public List<GoodsCategory> loadTree(Long parentId) {
    List<GoodsCategory> types = null;
    if (parentId == null) {
      types = getEntityDao().getAll();
    } else {
      GoodsCategory parent = get(parentId);
      types = getEntityDao().findByPathStartingWith(parent.getPath());
    }
    List<GoodsCategory> result = Lists.newLinkedList();
    try {
      Map<Long, GoodsCategory> dtoMap = Maps.newHashMap();
      for (GoodsCategory type : types) {
        dtoMap.put(type.getId(), type);
      }
      for (Map.Entry<Long, GoodsCategory> entry : dtoMap.entrySet()) {
        GoodsCategory node = entry.getValue();
        if (node.getParentId() == null || node.getParentId() == 0) {
          result.add(node);
        } else {
          if (dtoMap.get(node.getParentId()) != null) {
            dtoMap.get(node.getParentId()).addChild(node);
            Collections.sort(
                dtoMap.get(node.getParentId()).getChildren(), new GoodsCategory.NodeComparator());
          }
        }
      }
      types.clear();
      dtoMap.clear();
      Collections.sort(result, new GoodsCategory.NodeComparator());
    } catch (Exception e) {
      throw new BusinessException("查询分类树：" + e.getMessage());
    }
    return result;
  }

  /**
   * 获取全局顶层节点
   *
   * @return
   */
  @Override
  public List<GoodsCategory> loadTops() {
    return getEntityDao().getTops();
  }

  /**
   * 获取当前层最大的Path值
   *
   * @param parentId
   * @return
   */
  protected String getMaxPath(Long parentId) {
    if (parentId == null) {
      return getEntityDao().getTopMaxPath();
    } else {
      return getEntityDao().getMaxPath(parentId);
    }
  }
}
