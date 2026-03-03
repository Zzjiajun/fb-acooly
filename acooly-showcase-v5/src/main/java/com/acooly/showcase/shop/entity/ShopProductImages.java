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
 * 商品图片表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_product_images")
@Getter
@Setter
@ExportModel(name = "商品图片表", border = true, headerShow = true)
public class ShopProductImages extends AbstractEntity {

    /**
     * 商品ID
     */
	@NotNull
    @ExportColumn(header = "商品ID", order = 1)
    private Long productId;

    /**
     * 图片URL
     */
	@NotBlank
	@Size(max = 500)
    @ExportColumn(header = "图片URL", order = 2)
    private String imageUrl;

    /**
     * 排序顺序
     */
    @ExportColumn(header = "排序顺序", order = 3)
    private Integer sortOrder;

    /**
     * 是否为主图
     */
    @ExportColumn(header = "是否为主图", order = 4)
    private Integer isPrimary;
    @Size(max = 500)
    private String webpUrl;  // WebP格式路径

    @Size(max = 500)
    private String thumbWebpUrl;  // 缩略图WebP路径

    // 【新增】元数据字段
    private Long fileSize;  // 原图大小（字节）
    private Long webpSize;  // WebP大小（字节）
    private Long thumbSize;  // 缩略图大小（字节）

    private Integer width;  // 原图宽度
    private Integer height;  // 原图高度
    private Integer thumbWidth;  // 缩略图宽度
    private Integer thumbHeight;  // 缩略图高度

    @Size(max = 10)
    private String format;  // 原始格式（jpg/png/gif）
}
