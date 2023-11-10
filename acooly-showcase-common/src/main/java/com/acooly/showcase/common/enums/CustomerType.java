/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户信息表 CustomerType 枚举定义
 *
 * @author acooly Date: 2016-09-23 13:32:34
 */
public enum CustomerType implements Messageable {

  normal("normal", "普通"),
  vip("vip", "重要"),
  vip_sepc("vip_sepc", "特别");

  private final String code;
  private final String message;

  private CustomerType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (CustomerType type : values()) {
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
  public static CustomerType find(String code) {
    for (CustomerType status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("CustomerType not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<CustomerType> getAll() {
    List<CustomerType> list = new ArrayList<CustomerType>();
    for (CustomerType status : values()) {
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
    for (CustomerType status : values()) {
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
