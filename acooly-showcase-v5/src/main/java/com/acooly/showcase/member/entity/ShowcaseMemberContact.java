/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-10-26
*/
package com.acooly.showcase.member.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * 会员联系信息 Entity
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Entity
@Table(name = "ac_showcase_member_contact")
@Getter
@Setter
public class ShowcaseMemberContact extends AbstractEntity {

    /**
     * 用户编码

     */
	@NotBlank
	@Size(max = 64)
    private String memberNo;

    /**
     * 用户名

     */
	@NotBlank
	@Size(max = 32)
    private String username;

    /**
     * 手机号码

     */
	@Size(max = 16)
    private String mobileNo;

    /**
     * 联系电话

     */
	@Size(max = 32)
    private String phoneNo;

    /**
     * 邮箱

     */
	@Size(max = 128)
    private String email;

    /**
     * QQ账号

     */
	@Size(max = 16)
    private String qq;

    /**
     * 旺旺账号

     */
	@Size(max = 32)
    private String wangwang;

    /**
     * 微信账号

     */
	@Size(max = 32)
    private String wechat;

    /**
     * 脸书账号

     */
	@Size(max = 32)
    private String facebeek;

    /**
     * 谷歌账号

     */
	@Size(max = 32)
    private String google;

    /**
     * 省

     */
	@Size(max = 32)
    private String province;

    /**
     * 市

     */
	@Size(max = 32)
    private String city;

    /**
     * 区

     */
	@Size(max = 32)
    private String district;

    /**
     * 邮政编码

     */
	@Size(max = 16)
    private String zip;

    /**
     * 地址

     */
	@Size(max = 128)
    private String address;

    /**
     * 公司名

     */
	@Size(max = 32)
    private String companyName;

    /**
     * 职业

     */
	@Size(max = 16)
    private String career;

    /**
     * 备注

     */
	@Size(max = 255)
    private String comments;

}
