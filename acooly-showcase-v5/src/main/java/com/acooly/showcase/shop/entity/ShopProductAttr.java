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
 * 商品属性关联表 Entity
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Entity
@Table(name = "shop_product_attr")
@Getter
@Setter
@ExportModel(name = "商品属性关联表", border = true, headerShow = true)
public class ShopProductAttr extends AbstractEntity {

    /**
     * 商品ID
     */
	@NotNull
    @ExportColumn(header = "商品ID", order = 0)
    private Long productId;

    /**
     * 属性值ID
     */
	@NotNull
    @ExportColumn(header = "属性值ID", order = 1)
    private Long attrValueId;

}
