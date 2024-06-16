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

import javax.persistence.Transient;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * dm_access Entity
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Entity
@Table(name = "dm_access")
@Getter
@Setter
@ExportModel(name = "dm_access", border = true, headerShow = true)
public class DmAccess extends AbstractEntity {

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
    private String accessPath;

    /**
     * 访问设备
     */
	@Size(max = 255)
    @ExportColumn(header = "访问设备", order = 5)
    private String accessDevice;

    /**
     * 访客类型
     */
	@Size(max = 255)
    @ExportColumn(header = "访客类型", order = 6)
    private String visitorType;

//    /**
//     * IP总和
//     */
//    @Transient
//    private Integer ipSum;

}
