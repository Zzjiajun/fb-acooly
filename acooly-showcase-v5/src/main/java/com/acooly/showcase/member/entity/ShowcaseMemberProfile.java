/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-10-27
*/
package com.acooly.showcase.member.entity;


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
import com.acooly.showcase.member.common.enums.SmsSendStatusEnum;
import java.util.Date;
import com.acooly.showcase.member.common.enums.EmailStatusEnum;
import com.acooly.showcase.member.common.enums.RealNameStatusEnum;
import com.acooly.showcase.member.common.enums.MobileNoStatusEnum;

/**
 * 会员配置信息 Entity
 *
 * @author acooly
 * @date 2021-10-27 13:24:15
 */
@Entity
@Table(name = "ac_showcase_member_profile")
@Getter
@Setter
public class ShowcaseMemberProfile extends AbstractEntity {

    /**
     * 会员编码

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
     * 昵称

     */
	@Size(max = 32)
    private String nickname;

    /**
     * 个性签名

     */
	@Size(max = 64)
    private String dailyWords;

    /**
     * 头像

     */
	@Size(max = 128)
    private String profilePhoto;

    /**
     * 邮箱认证

     */
    @Enumerated(EnumType.STRING)
    private EmailStatusEnum emailStatus;

    /**
     * 手机认证

     */
    @Enumerated(EnumType.STRING)
    private MobileNoStatusEnum mobileNoStatus;

    /**
     * 实名认证

     */
    @Enumerated(EnumType.STRING)
    private RealNameStatusEnum realNameStatus;

    /**
     * 安全问题设置状态

     */
	@Size(max = 16)
    private String secretQaStatus;

    /**
     * 发送短信

     */
    @Enumerated(EnumType.STRING)
    private SmsSendStatusEnum smsSendStatus;

    /**
     * 客户经理

     */
	@Size(max = 16)
    private String manager;

    /**
     * 经纪人

     */
	@Size(max = 16)
    private String broker;

    /**
     * 邀请人

     */
	@Size(max = 16)
    private String inviter;

    /**
     * 备注

     */
	@Size(max = 255)
    private String comments;

}
