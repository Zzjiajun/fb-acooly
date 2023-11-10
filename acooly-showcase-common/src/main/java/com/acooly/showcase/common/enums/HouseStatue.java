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
 * 客户基本信息 HouseStatue 枚举定义
 *
 * @author acooly Date: 2016-10-12 18:00:43
 */
public enum HouseStatue implements Messageable {
  rental("rental", "租房"),
  own("own", "自有"),
  other("other", "其他"),
  ;

  private final String code;
  private final String message;

  private HouseStatue(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (HouseStatue type : values()) {
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
  public static HouseStatue find(String code) {
    for (HouseStatue status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("HouseStatue not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<HouseStatue> getAll() {
    List<HouseStatue> list = new ArrayList<HouseStatue>();
    for (HouseStatue status : values()) {
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
    for (HouseStatue status : values()) {
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
