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
 * 品牌表 Entity
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Entity
@Table(name = "shop_brand")
@Getter
@Setter
@ExportModel(name = "品牌表", border = true, headerShow = true)
public class ShopBrand extends AbstractEntity {

    /**
     * 品牌名称
     */
	@NotBlank
	@Size(max = 100)
    @ExportColumn(header = "品牌名称", order = 1)
    private String name;

    /**
     * 品牌logo
     */
	@Size(max = 255)
    @ExportColumn(header = "品牌logo", order = 2)
    private String logo;

    /**
     * 状态 1启用 0禁用
     */
    @ExportColumn(header = "状态 1启用 0禁用", order = 3)
    private Integer status;

}
