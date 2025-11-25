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
 * 用户表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_users")
@Getter
@Setter
@ExportModel(name = "用户表", border = true, headerShow = true)
public class ShopUsers extends AbstractEntity {

    /**
     * 邮箱地址
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "邮箱地址", order = 1)
    private String email;

    /**
     * 密码（加密存储）
     */
	@Size(max = 255)
    @ExportColumn(header = "密码（加密存储）", order = 2)
    private String password;

    /**
     * 用户姓名
     */
	@Size(max = 255)
    @ExportColumn(header = "用户姓名", order = 3)
    private String name;

    /**
     * 头像URL
     */
	@Size(max = 500)
    @ExportColumn(header = "头像URL", order = 4)
    private String avatar;

    /**
     * 第三方平台用户ID
     */
	@Size(max = 255)
    @ExportColumn(header = "第三方平台用户ID", order = 5)
    private String thirdPartyId;

    /**
     * 登录方式：GOOGLE, FACEBOOK, TIKTOK, LOCAL
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "登录方式：GOOGLE, FACEBOOK, TIKTOK, LOCAL", order = 6)
    private String provider;



}
