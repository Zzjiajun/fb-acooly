/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-14
 *
 */
package com.acooly.showcase.jpa.goods.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品信息 GoodsUnitEnum 枚举定义
 *
 * @author acooly Date: 2017-09-14 02:11:41
 */
public enum GoodsUnitEnum implements Messageable {
  sets("sets", "套"),

  sheet("sheet", "张"),

  pc("pc", "只"),

  pkg("pkg", "件"),

  pair("pair", "双"),

  cases("cases", "箱"),

  bale("bale", "包"),

  bag("bag", "袋"),

  kg("kg", "公斤"),

  jin("jin", "斤"),

  liter("liter", "升"),
  ;

  private final String code;
  private final String message;

  private GoodsUnitEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (GoodsUnitEnum type : values()) {
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
  public static GoodsUnitEnum find(String code) {
    for (GoodsUnitEnum status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    return null;
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<GoodsUnitEnum> getAll() {
    List<GoodsUnitEnum> list = new ArrayList<GoodsUnitEnum>();
    for (GoodsUnitEnum status : values()) {
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
    for (GoodsUnitEnum status : values()) {
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
