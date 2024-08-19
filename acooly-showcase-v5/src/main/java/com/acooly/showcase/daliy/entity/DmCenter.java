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
 * dm_center Entity
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
@Entity
@Table(name = "dm_center")
@Getter
@Setter
@ExportModel(name = "dm_center", border = true, headerShow = true)
public class DmCenter extends AbstractEntity {

    /**
     * 用户名
     */
	@Size(max = 255)
    @ExportColumn(header = "用户名", order = 1)
    private String userName;

    /**
     * 地区
     */
	@Size(max = 255)
    @ExportColumn(header = "地区", order = 2)
    private String region;

    /**
     * 像素代码
     */
    @ExportColumn(header = "像素代码", order = 3)
    private String pixel;

    /**
     * 链接地址
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "链接地址", order = 4)
    private String link;

    /**
     * 主域名
     */
	@Size(max = 255)
    @ExportColumn(header = "主域名", order = 5)
    private String domain;

    /**
     * 二级域名
     */
	@Size(max = 255)
    @ExportColumn(header = "二级域名", order = 6)
    private String secondaryDomain;

    /**
     * 模版地址
     */
	@Size(max = 255)
    @ExportColumn(header = "模版地址", order = 7)
    private String serialNumber;

    /**
     * 域名访问量
     */
    @ExportColumn(header = "域名访问量", order = 8)
    private Integer visitsNumber;

    /**
     * 按钮点击数量
     */
    @ExportColumn(header = "按钮点击数量", order = 9)
    private Integer clicksNumber;

    private Integer displayOption;

    private Integer diversion;

    private Integer trolls;

    private Integer protect;
}
