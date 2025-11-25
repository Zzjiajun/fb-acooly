/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.entity;


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
 * 联系消息表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_contact_messages")
@Getter
@Setter
@ExportModel(name = "联系消息表", border = true, headerShow = true)
public class ShopContactMessages extends AbstractEntity {

    /**
     * 联系人姓名
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "联系人姓名", order = 1)
    private String name;

    /**
     * 联系人邮箱
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "联系人邮箱", order = 2)
    private String email;

    /**
     * 消息主题
     */
	@Size(max = 255)
    @ExportColumn(header = "消息主题", order = 3)
    private String subject;

    /**
     * 消息内容
     */
	@NotBlank
    @ExportColumn(header = "消息内容", order = 4)
    private String message;

    /**
     * 创建时间
     */
    @ExportColumn(header = "创建时间", order = 5)
    private Date createdAt;

}
