/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-26
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
 * dm_stencil Entity
 *
 * @author acooly
 * @date 2024-04-26 04:43:38
 */
@Entity
@Table(name = "dm_stencil")
@Getter
@Setter
@ExportModel(name = "dm_stencil", border = true, headerShow = true)
public class DmStencil extends AbstractEntity {

    /**
     * 地址
     */
	@Size(max = 255)
    @ExportColumn(header = "地址", order = 1)
    private String pathName;

    /**
     * 备注
     */
	@Size(max = 255)
    @ExportColumn(header = "备注", order = 2)
    private String reg;

    private Integer versiond;
}
