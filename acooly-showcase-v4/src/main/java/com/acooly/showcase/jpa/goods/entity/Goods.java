/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-14
 */
package com.acooly.showcase.jpa.goods.entity;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.showcase.jpa.goods.enums.GoodsUnitEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 商品信息 Entity
 *
 * @author acooly Date: 2017-09-14 02:11:41
 */
@Getter
@Setter
@Entity
@Table(name = "dm_goods")
public class Goods extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  /** 分类编号 */
  @NotNull private Long categoryId;

  /** 分类名称 */
  @Size(max = 32)
  private String categoryName;

  /** 商品编码 */
  @NotEmpty
  @Size(max = 32)
  private String code = Ids.getDid("GD");

  /** 商品型号 */
  @Size(max = 32)
  private String model;

  /** 商品名称 */
  @NotEmpty
  @Size(max = 32)
  private String name;

  /** 商品单位 */
  @Enumerated(EnumType.STRING)
  private GoodsUnitEnum unit;

  /** 商品品牌 */
  @Size(max = 16)
  private String brand;

  /** 商品描述 */
  @Size(max = 255)
  private String descn;

  /** 商品详情 */
  @Size(max = 128)
  private String detailUrl;

  /** 商品价格 */
  private Money price;

  /** 商品库存 */
  private Integer stock = 0;

  /** 提供商 */
  @Size(max = 32)
  private String supplier;

  /** 备注 */
  @Size(max = 255)
  private String comments;
}
