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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表（关联子级分类） Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_products")
@Getter
@Setter
@ExportModel(name = "商品表（关联子级分类）", border = true, headerShow = true)
public class ShopProducts extends AbstractEntity {

    /**
     * 商品名称
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "商品名称", order = 1)
    private String name;

    /**
     * 子级分类ID
     */
    @ExportColumn(header = "子级分类ID", order = 2)
    private Long subCategoryId;

    /**
     * 商品价格
     */
	@NotNull
    @ExportColumn(header = "商品价格", order = 3)
    private BigDecimal price;

    /**
     * 商品原价
     */
    @ExportColumn(header = "商品原价", order = 4)
    private BigDecimal originalPrice;

    /**
     * 商品评分
     */
    @ExportColumn(header = "商品评分", order = 5)
    private Long rating;

    /**
     * 评论数量
     */
    @ExportColumn(header = "评论数量", order = 6)
    private Integer reviewCount;

    /**
     * 商品主图URL
     */
	@NotBlank
	@Size(max = 500)
    @ExportColumn(header = "商品主图URL", order = 7)
    private String imageUrl;

    /**
     * 是否推荐
     */
    @ExportColumn(header = "是否推荐", order = 8)
    private Integer featured;

    /**
     * 是否包邮
     */
    @ExportColumn(header = "是否包邮", order = 9)
    private Integer freeShipping;

    /**
     * 商品描述
     */
    @ExportColumn(header = "商品描述", order = 10)
    private String description;

    private String serialNumber;

    private Long brandId;


}
