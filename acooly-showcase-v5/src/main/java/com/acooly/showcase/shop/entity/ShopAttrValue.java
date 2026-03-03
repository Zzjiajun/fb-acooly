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
 * 商品属性值表 Entity
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Entity
@Table(name = "shop_attr_value")
@Getter
@Setter
@ExportModel(name = "商品属性值表", border = true, headerShow = true)
public class ShopAttrValue extends AbstractEntity {

    /**
     * 属性ID
     */
	@NotNull
    @ExportColumn(header = "属性ID", order = 1)
    private Long attrId;

    /**
     * 属性值（男/女/中性）
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "属性值（男/女/中性）", order = 2)
    private String value;

    /**
     * 排序
     */
    @ExportColumn(header = "排序", order = 3)
    private Integer sort;

    /**
     * 状态
     */
    @ExportColumn(header = "状态", order = 4)
    private Integer status;

}
