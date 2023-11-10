/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-13
 *
 */
package com.acooly.showcase.jpa.customer.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * dm_customer_extend Entity
 *
 * @author acooly Date: 2016-10-13 00:03:03
 */
@Getter
@Setter
@Entity
@Table(name = "dm_customer_extend")
public class CustomerExtend extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;
  /** 用户名 */
  private String username;
  /** 扩展信息 */
  private String context;
  /** 备注 */
  private String comments;
}
