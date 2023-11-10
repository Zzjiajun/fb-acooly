/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-12
 *
 */
package com.acooly.showcase.jpa.customer.entity;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.showcase.common.enums.EducationLevel;
import com.acooly.showcase.common.enums.HouseStatue;
import com.acooly.showcase.common.enums.IncomeMonth;
import com.acooly.showcase.common.enums.MaritalStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 客户基本信息 Entity
 *
 * @author acooly Date: 2016-10-12 18:00:43
 */
@Getter
@Setter
@Entity
@Table(name = "dm_customer_basic")
public class CustomerBasic extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  /** 用户名 */
  private String username;
  /** 婚姻状况 */
  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;
  /** 子女情况 */
  private int childrenCount;
  /** 教育背景 */
  @Enumerated(EnumType.STRING)
  private EducationLevel educationLevel;
  /** 月收入 */
  @Enumerated(EnumType.STRING)
  private IncomeMonth incomeMonth;
  /** 社会保险 */
  @Enumerated(EnumType.STRING)
  private WhetherStatus socialInsurance;
  /** 公积金 */
  @Enumerated(EnumType.STRING)
  private WhetherStatus houseFund;
  /** 住房情况 */
  @Enumerated(EnumType.STRING)
  private HouseStatue houseStatue;
  /** 是否购车 */
  @Enumerated(EnumType.STRING)
  private WhetherStatus carStatus;

  /** 备注 */
  private String comments;
}
