/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-08
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
 * dm_click Entity
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Entity
@Table(name = "dm_click")
@Getter
@Setter
@ExportModel(name = "dm_click", border = true, headerShow = true)
public class DmClick extends AbstractEntity {

    /**
     * 数据中心id
     */
    @ExportColumn(header = "数据中心id", order = 1)
    private Integer centerId;

    /**
     * 点击ip
     */
	@Size(max = 255)
    @ExportColumn(header = "点击ip", order = 2)
    private String ip;

    /**
     * region
     */
	@Size(max = 255)
    @ExportColumn(header = "region", order = 3)
    private String region;

    /**
     * 点击设备
     */
	@Size(max = 255)
    @ExportColumn(header = "点击设备", order = 4)
    private String clickDevice;

    /**
     * 点击类型
     */
	@Size(max = 255)
    @ExportColumn(header = "点击类型", order = 5)
    private String clickType;

}
