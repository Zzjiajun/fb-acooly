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
 * dm_region Entity
 *
 * @author acooly
 * @date 2024-03-25 12:05:46
 */
@Entity
@Table(name = "dm_region")
@Getter
@Setter
@ExportModel(name = "dm_region", border = true, headerShow = true)
public class DmRegion extends AbstractEntity {

    /**
     * 地区
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "地区", order = 1)
    private String region;

}
