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
 * 支持的语言列表 Entity
 *
 * @author acooly
 * @date 2025-12-26 20:50:12
 */
@Entity
@Table(name = "shop_i18n_locales")
@Getter
@Setter
@ExportModel(name = "支持的语言列表", border = true, headerShow = true)
public class ShopI18nLocales extends AbstractEntity {

    /**
     * 语言代码：zh_CN, en_US, it_IT, ja_JP, zh_HK
     */
	@NotBlank
	@Size(max = 10)
    @ExportColumn(header = "语言代码：zh_CN, en_US, it_IT, ja_JP, zh_HK", order = 1)
    private String localeCode;

    /**
     * 语言名称：简体中文, English, Italiano, 日本語, 繁體中文
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "语言名称：简体中文, English, Italiano, 日本語, 繁體中文", order = 2)
    private String localeName;

    /**
     * 是否默认语言（1是 0否）
     */
    @ExportColumn(header = "是否默认语言（1是 0否）", order = 3)
    private Integer isDefault;

    /**
     * 是否启用（1启用 0禁用）
     */
    @ExportColumn(header = "是否启用（1启用 0禁用）", order = 4)
    private Integer isActive;

    /**
     * 排序顺序
     */
    @ExportColumn(header = "排序顺序", order = 5)
    private Integer sortOrder;

}
