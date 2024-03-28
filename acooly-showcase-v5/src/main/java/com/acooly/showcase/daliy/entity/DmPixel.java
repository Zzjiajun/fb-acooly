/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-26
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
 * dm_pixel Entity
 *
 * @author acooly
 * @date 2024-03-26 15:09:00
 */
@Entity
@Table(name = "dm_pixel")
@Getter
@Setter
@ExportModel(name = "dm_pixel", border = true, headerShow = true)
public class DmPixel extends AbstractEntity {

    /**
     * domain
     */
	@Size(max = 255)
    @ExportColumn(header = "domain", order = 1)
    private String domain;

    /**
     * pixel_id
     */
	@Size(max = 255)
    @ExportColumn(header = "pixel_id", order = 2)
    private String pixelId;

    private String userName;
}
