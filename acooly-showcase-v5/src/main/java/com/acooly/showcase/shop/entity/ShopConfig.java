/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-28
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
 * shop_config Entity
 *
 * @author acooly
 * @date 2025-11-28 23:22:47
 */
@Entity
@Table(name = "shop_config")
@Getter
@Setter
@ExportModel(name = "shop_config", border = true, headerShow = true)
public class ShopConfig extends AbstractEntity {

    /**
     * 轮播商品
     */
	@Size(max = 255)
    @ExportColumn(header = "轮播商品idS", order = 1)
    private String carouselProducts;

    /**
     * 展示商品
     */
	@Size(max = 255)
    @ExportColumn(header = "展示商品Ids", order = 2)
    private String displayProducts;

    /**
     * 展示评论
     */
	@Size(max = 255)
    @ExportColumn(header = "展示评论Ids", order = 3)
    private String showComments;

    /**
     * 头部展示
     */
	@Size(max = 255)
    @ExportColumn(header = "头部展示信息", order = 4)
    private String headDisplay;

    private String logoUrl;

    private String productDescription;

    private String productDescriptionUrl;
}
