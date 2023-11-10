/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-14
 */
package com.acooly.showcase.jpa.goods.entity;

import com.acooly.core.common.domain.AbstractEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.List;

/**
 * 商品分类 Entity
 *
 * @author acooly Date: 2017-09-14 02:11:41
 */
@Getter
@Setter
@Entity
@Table(name = "dm_goods_category")
public class GoodsCategory extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  /** 父编号 */
  private Long parentId;

  /** 搜索路径 */
  @Size(max = 128)
  private String path;

  /** 类型编码 */
  @NotEmpty
  @Size(max = 32)
  private String code;

  /** 类型名称 */
  @NotEmpty
  @Size(max = 32)
  private String name;

  /** 排序时间 */
  @NotNull private Long sortTime;

  /** 备注 */
  @Size(max = 255)
  private String comments;

  @Transient private List<GoodsCategory> children = Lists.newArrayList();

  public void addChild(GoodsCategory goodsCategory) {
    this.children.add(goodsCategory);
  }

  public static class NodeComparator implements Comparator<GoodsCategory> {
    @Override
    public int compare(GoodsCategory node1, GoodsCategory node2) {
      long orderTime1 = node1.getSortTime();
      long orderTime2 = node2.getSortTime();
      return orderTime1 > orderTime2 ? -1 : (orderTime1 == orderTime2 ? 0 : 1);
    }
  }
}
