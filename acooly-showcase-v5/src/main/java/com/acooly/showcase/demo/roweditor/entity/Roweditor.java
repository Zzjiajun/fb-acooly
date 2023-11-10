/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-11-14
*/
package com.acooly.showcase.demo.roweditor.entity;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;

/**
 * 行编辑实例 Entity
 *
 * @author acooly
 * @date 2021-11-14 16:15:13
 */
@Entity
@Table(name = "ac_showcase_roweditor")
@Getter
@Setter
public class Roweditor extends AbstractEntity {

    /**
     * 会员编码

     */
	@NotBlank
	@Size(max = 32)
    private String memberNo;

    /**
     * 用户名

     */
	@NotBlank
	@Size(max = 32)
    private String username;

    /**
     * 姓名

     */
	@NotBlank
	@Size(max = 16)
    private String realName;

    /**
     * 身份证号码

     */
	@NotBlank
	@Size(max = 48)
    private String idcardNo;

    /**
     * 年龄

     */
    private Integer age;

    /**
     * 生日

     */
	@NotNull
    private Date birthday;

    /**
     * 性别

     */
    @Enumerated(EnumType.STRING)
	@NotNull
    private Gender gender;

    /**
     * 生肖

     */
    @Enumerated(EnumType.STRING)
    private AnimalSign animal;

    /**
     * 手机号码

     */
	@Size(max = 11)
    private String mobileNo;

    /**
     * 邮件

     */
	@Size(max = 64)
    private String mail;

    /**
     * 网址

     */
	@Size(max = 128)
    private String website;

    /**
     * 完成度

     */
    private Integer doneRatio;

    /**
     * 薪水

     */
    private Money salary;

    /**
     * 注册渠道

     */
    @Enumerated(EnumType.STRING)
    private ChannelEnum registryChannel;

    /**
     * 状态

     */
    @Enumerated(EnumType.STRING)
	@NotNull
    private SimpleStatus status;

    /**
     * 备注

     */
	@Size(max = 255)
    private String comments;

}
