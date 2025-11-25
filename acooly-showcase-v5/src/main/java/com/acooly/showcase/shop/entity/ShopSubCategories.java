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
 * 子级分类表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_sub_categories")
@Getter
@Setter
@ExportModel(name = "子级分类表", border = true, headerShow = true)
public class ShopSubCategories extends AbstractEntity {

    /**
     * 父级分类ID
     */
	@NotNull
    @ExportColumn(header = "父级分类ID", order = 1)
    private Long parentId;

    /**
     * 子级分类名称
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "子级分类名称", order = 2)
    private String name;

    /**
     * 子级分类标识
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "子级分类标识", order = 3)
    private String slug;

    /**
     * 子级分类描述
     */
    @ExportColumn(header = "子级分类描述", order = 4)
    private String description;

    /**
     * 排序顺序
     */
    @ExportColumn(header = "排序顺序", order = 5)
    private Integer sortOrder;

    /**
     * 是否启用
     */
    @ExportColumn(header = "是否启用", order = 6)
    private Integer isActive;

    /**
     * 是否显示
     */
    @ExportColumn(header = "是否显示", order = 7)
    private Integer isShow;

    /**
     * 分类图标URL
     */
	@Size(max = 500)
    @ExportColumn(header = "分类图标URL", order = 8)
    private String icon;

    /**
     * 分类图片URL
     */
	@Size(max = 500)
    @ExportColumn(header = "分类图片URL", order = 9)
    private String image;

    /**
     * SEO标题
     */
	@Size(max = 255)
    @ExportColumn(header = "SEO标题", order = 10)
    private String seoTitle;

    /**
     * SEO关键词
     */
	@Size(max = 500)
    @ExportColumn(header = "SEO关键词", order = 11)
    private String seoKeywords;

    /**
     * SEO描述
     */
    @ExportColumn(header = "SEO描述", order = 12)
    private String seoDescription;

}
