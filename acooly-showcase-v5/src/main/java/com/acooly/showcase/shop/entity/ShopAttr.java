/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-22
*/
package com.acooly.showcase.shop.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * 商品属性表 Entity
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Entity
@Table(name = "shop_attr")
@Getter
@Setter
@ExportModel(name = "商品属性表", border = true, headerShow = true)
public class ShopAttr extends AbstractEntity {

    /**
     * 属性名称（性别/风格/材质）
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "属性名称（性别/风格/材质）", order = 1)
    private String name;

    /**
     * 状态
     */
    @ExportColumn(header = "状态", order = 2)
    private Integer status;

}
