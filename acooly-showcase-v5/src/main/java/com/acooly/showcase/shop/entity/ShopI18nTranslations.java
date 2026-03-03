/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-26
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
 * 多语言翻译表 Entity
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Entity
@Table(name = "shop_i18n_translations")
@Getter
@Setter
@ExportModel(name = "多语言翻译表", border = true, headerShow = true)
public class ShopI18nTranslations extends AbstractEntity {

    /**
     * 实体类型
     * 可选值：product, parent_category, sub_category, brand, attr, attr_value
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "实体类型：product", order = 1)
    private String entityType;

    /**
     * 实体ID，对应原表的id字段
     */
	@NotNull
    @ExportColumn(header = "实体ID，对应原表的id字段", order = 2)
    private Long entityId;

    /**
     * 字段名称
     * 可选值：name, description, value
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "字段名称：name", order = 3)
    private String fieldName;

    /**
     * 语言代码
     * 可选值：zh_CN, en_US, it_IT, ja_JP, zh_HK
     */
	@NotBlank
	@Size(max = 10)
    @ExportColumn(header = "语言代码：zh_CN", order = 4)
    private String locale;

    /**
     * 翻译内容
     */
	@NotBlank
    @ExportColumn(header = "翻译内容", order = 5)
    private String translation;

}
