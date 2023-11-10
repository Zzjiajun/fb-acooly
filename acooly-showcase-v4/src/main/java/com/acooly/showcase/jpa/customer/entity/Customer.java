/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.entity;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.showcase.common.enums.CustomerType;
import com.acooly.showcase.common.enums.IdcardType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 客户信息表 Entity
 *
 * @author acooly Date: 2016-09-23 13:32:34
 */
@Getter
@Setter
@Entity
@Table(name = "dm_customer")
public class Customer extends AbstractEntity {

  public static final int GENDER_MAN = 1;
  public static final int GENDER_HUMAN = 2;

  public static final int STATUS_DISABLE = 0;
  public static final int STATUS_FROZEN = 1;
  public static final int STATUS_PAUSE = 2;
  public static final int STATUS_ENABLE = 10;
  public static final int STATUS_GOOD = 11;
  public static final int STATUS_EXCITED = 12;

  /** serialVersionUID */
  private static final long serialVersionUID = 1L;

  private Long id;
  /** 用户名 */
  private String username;
  /** 年龄 */
  private int age;
  /** 生日 */
  private Date birthday;
  /** 性别 */
  private int gender;
  /** 姓名 */
  private String realName;
  /** 证件类型 */
  @Enumerated(EnumType.STRING)
  private IdcardType idcardType;
  /** 证件号码 */
  private String idcardNo;
  /** 手机号码 */
  private String mobileNo;
  /** 邮件 */
  private String mail;
  /** 摘要 */
  private String subject;
  /** 客户类型 */
  @Enumerated(EnumType.STRING)
  private CustomerType customerType;
  /** 手续费 */
  private Double fee;

  /** 薪水 */
  private Long salary;

  /** 状态 */
  private int status = STATUS_ENABLE;
  /** 备注 */
  private String comments;
}
