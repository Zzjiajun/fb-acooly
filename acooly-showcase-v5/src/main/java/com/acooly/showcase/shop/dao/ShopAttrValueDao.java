/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-22
 */
 package com.acooly.showcase.shop.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.shop.entity.ShopAttrValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品属性值表 Mybatis Dao
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
public interface ShopAttrValueDao extends EntityMybatisDao<ShopAttrValue> {

    /**
     * 唯一索引查询: uk_shop_attr_value
     *
     * @param attrId
     * @param value
     * @return
     */
    @Select("select * from shop_attr_value where attr_id=#{attrId} AND value=#{value}")
    ShopAttrValue uniqueByAttrIdAndValue(@Param("attrId") Long attrId, @Param("value") String value);

}
