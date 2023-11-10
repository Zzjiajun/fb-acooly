/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-19
 */
package com.acooly.showcase.member.entity;


import javax.persistence.*;

import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.showcase.member.common.enums.CustomerTypeEnum;

import java.util.Date;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.showcase.member.common.enums.IdcardTypeEnum;
import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;

/**
 * 会员信息 Entity
 *
 * @author acooly
 * @date 2021-10-19 14:23:36
 */
@Entity
@Table(name = "ac_showcase_member")
@Getter
@Setter
public class ShowcaseMember extends AbstractEntity {


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
     * 姓名
     */
    @NotBlank
    @Size(max = 16)
    private String realName;

    /**
     * 证件类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private IdcardTypeEnum idcardType;

    /**
     * 身份证号码
     */
    @NotBlank
    @Size(max = 48)
    private String idcardNo;

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
     * 客户类型
     */
    @Enumerated(EnumType.STRING)
    private CustomerTypeEnum customerType;

    /**
     * 摘要
     */
    @Size(max = 128)
    private String subject;

    /**
     * 完成度
     * 支持小数点2为的百分数，即：万分位
     */
    @MoneyConstraint(nullable = true, min = 0, max = 10000)
    private Money doneRatio;

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
     * 推送广告
     */
    @Enumerated(EnumType.STRING)
    private WhetherStatus pushAdv;

    /**
     * 数字类型
     */
    private Integer numStatus;

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

    /**
     * 网址
     */
    @Size(max = 128)
    private String website;

    /**
     * 扩展富文本内容（子表管理）
     * 列表为空，详情填充
     */
    @Transient
    private String context;

}
