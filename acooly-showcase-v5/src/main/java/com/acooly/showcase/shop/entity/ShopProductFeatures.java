/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
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
 * 商品特性表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_product_features")
@Getter
@Setter
@ExportModel(name = "商品特性表", border = true, headerShow = true)
public class ShopProductFeatures extends AbstractEntity {

    /**
     * 商品ID
     */
	@NotNull
    @ExportColumn(header = "商品ID", order = 1)
    private Long productId;

    /**
     * 特性名称
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "特性名称", order = 2)
    private String feature;

    /**
     * 特性值
     */
	@Size(max = 500)
    @ExportColumn(header = "特性值", order = 3)
    private String featureValue;

    /**
     * 排序顺序
     */
    @ExportColumn(header = "排序顺序", order = 4)
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @ExportColumn(header = "创建时间", order = 5)
    private Date createdAt;

}
