/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-01
*/
package com.acooly.showcase.shop.entity;


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
 * shop_contact_info Entity
 *
 * @author acooly
 * @date 2025-12-01 20:10:49
 */
@Entity
@Table(name = "shop_contact_info")
@Getter
@Setter
@ExportModel(name = "shop_contact_info", border = true, headerShow = true)
public class ShopContactInfo extends AbstractEntity {

    /**
     * -- address, phone, email, line, whatsapp, instagram, ...
     */
	@Size(max = 255)
    @ExportColumn(header = "-- address, phone, email, line, whatsapp, instagram, ...", order = 1)
    private String type;

    /**
     * 可选显示名字，例如 "Line"
     */
	@Size(max = 255)
    @ExportColumn(header = "可选显示名字，例如 Line", order = 2)
    private String name;

    /**
     * 实际内容
     */
	@Size(max = 255)
    @ExportColumn(header = "实际内容", order = 3)
    private String value;

    /**
     * 顺序
     */
    @ExportColumn(header = "顺序", order = 4)
    private Integer sortTime;

    /**
     * 是否显示
     */
    @ExportColumn(header = "是否显示", order = 5)
    private Integer isActive;

    private Integer teamId;
    @Transient
    private String teamName;

}
