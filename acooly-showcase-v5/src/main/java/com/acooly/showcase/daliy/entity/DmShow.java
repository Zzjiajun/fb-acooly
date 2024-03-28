/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-25
*/
package com.acooly.showcase.daliy.entity;


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
 * dm_show Entity
 *
 * @author acooly
 * @date 2024-03-25 10:30:45
 */
@Entity
@Table(name = "dm_show")
@Getter
@Setter
@ExportModel(name = "dm_show", border = true, headerShow = true)
public class DmShow extends AbstractEntity {

    /**
     * 地区
     */
	@Size(max = 255)
    @ExportColumn(header = "地区", order = 1)
    private String region;

    /**
     * 模版编号
     */
	@Size(max = 255)
    @ExportColumn(header = "模版编号", order = 2)
    private String serialNumber;

    /**
     * 备注
     */
	@Size(max = 255)
    @ExportColumn(header = "备注", order = 3)
    private String remark;

    /**
     * 域名地址
     */
	@Size(max = 255)
    @ExportColumn(header = "域名地址", order = 6)
    private String domain;

}
