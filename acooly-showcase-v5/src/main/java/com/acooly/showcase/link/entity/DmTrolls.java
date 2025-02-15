/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by jj
* date:2024-09-25
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
 * dm_trolls Entity
 *
 * @author jj
 * @date 2024-09-25 04:30:32
 */
@Entity
@Table(name = "dm_trolls")
@Getter
@Setter
@ExportModel(name = "dm_trolls", border = true, headerShow = true)
public class DmTrolls extends AbstractEntity {

    /**
     * 数据中心绑定的id父类
     */
	@NotNull
    @ExportColumn(header = "数据中心绑定的id父类", order = 1)
    private Integer centerId;

    /**
     * IP地址
     */
	@Size(max = 255)
    @ExportColumn(header = "IP地址", order = 2)
    private String ip;

    /**
     * 访客地区
     */
	@Size(max = 255)
    @ExportColumn(header = "访客地区", order = 3)
    private String region;

    /**
     * 访问路径
     */
	@Size(max = 255)
    @ExportColumn(header = "访问路径", order = 4)
    private String trollsPath;

    /**
     * 访问设备
     */
	@Size(max = 255)
    @ExportColumn(header = "访问设备", order = 5)
    private String trollsDevice;

    /**
     * 访客类型
     */
	@Size(max = 255)
    @ExportColumn(header = "访客类型", order = 6)
    private String visitorType;

    /**
     * 机型
     */
    @Size(max = 255)
    @ExportColumn(header = "机型", order = 7)
    private String models;

    /**
     * 访客来源
     */
    @Size(max = 255)
    @ExportColumn(header = "访客来源", order = 8)
    private String source;
    /**
     * 失败明细
     */
    @ExportColumn(header = "失败明细", order = 6)
    private String details;
}
