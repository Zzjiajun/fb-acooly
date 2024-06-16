/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-06
*/
package com.acooly.showcase.link.entity;


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
 * dm_country Entity
 *
 * @author acooly
 * @date 2024-06-06 04:06:20
 */
@Entity
@Table(name = "dm_country")
@Getter
@Setter
@ExportModel(name = "dm_country", border = true, headerShow = true)
public class DmCountry extends AbstractEntity {

    /**
     * 国家
     */
	@Size(max = 255)
    @ExportColumn(header = "国家", order = 1)
    private String country;

    /**
     * 中文名称
     */
	@Size(max = 255)
    @ExportColumn(header = "中文名称", order = 4)
    private String name;

}
