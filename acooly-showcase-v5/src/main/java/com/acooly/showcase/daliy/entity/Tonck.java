/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-25
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
 * tonck Entity
 *
 * @author acooly
 * @date 2024-04-25 03:17:48
 */
@Entity
@Table(name = "tonck")
@Getter
@Setter
@ExportModel(name = "tonck", border = true, headerShow = true)
public class Tonck extends AbstractEntity {

    /**
     * 图片识别tonck
     */
	@Size(max = 255)
    @ExportColumn(header = "图片识别tonck", order = 1)
    private String tonck;

}
