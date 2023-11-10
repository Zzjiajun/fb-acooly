/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-12
 *
 */
package com.acooly.showcase.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户基本信息 IncomeMonth 枚举定义
 *
 * @author acooly Date: 2016-10-12 18:00:43
 */
public enum IncomeMonth implements Messageable {
  k3_below("k3_below", "3000或以下"),
  k5_below(" k5_below", "3000-5000(含)"),
  k10_below("k10_below", "5000-10000(含)"),
  k20_below("k20_below", "10000-20000(含)"),
  k50_below("k50_below", "20000-50000(含)"),
  k51_above("k51_above", "50000以上");

  private final String code;
  private final String message;

  private IncomeMonth(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (IncomeMonth type : values()) {
      map.put(type.getCode(), type.getMessage());
    }
    return map;
  }

  /**
   * 通过枚举值码查找枚举值。
   *
   * @param code 查找枚举值的枚举值码。
   * @return 枚举值码对应的枚举值。
   * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
   */
  public static IncomeMonth find(String code) {
    for (IncomeMonth status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("IncomeMonth not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<IncomeMonth> getAll() {
    List<IncomeMonth> list = new ArrayList<IncomeMonth>();
    for (IncomeMonth status : values()) {
      list.add(status);
    }
    return list;
  }

  /**
   * 获取全部枚举值码。
   *
   * @return 全部枚举值码。
   */
  public static List<String> getAllCode() {
    List<String> list = new ArrayList<String>();
    for (IncomeMonth status : values()) {
      list.add(status.code());
    }
    return list;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String code() {
    return code;
  }

  public String message() {
    return message;
  }
}
