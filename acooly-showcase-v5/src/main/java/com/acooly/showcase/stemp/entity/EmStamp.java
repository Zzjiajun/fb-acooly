/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.entity;


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
 * em_stamp Entity
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Entity
@Table(name = "em_stamp")
@Getter
@Setter
@ExportModel(name = "em_stamp", border = true, headerShow = true)
public class EmStamp extends AbstractEntity {

    /**
     * 类型数据名
     */
	@Size(max = 255)
    @ExportColumn(header = "类型数据名", order = 1)
    private String name;

    /**
     * 字段名集合
     */
	@Size(max = 255)
    @ExportColumn(header = "字段名集合", order = 2)
    private String gather;

    @Size(max = 255)
    @ExportColumn(header = "创建者", order = 3)
    private String userName;


}
