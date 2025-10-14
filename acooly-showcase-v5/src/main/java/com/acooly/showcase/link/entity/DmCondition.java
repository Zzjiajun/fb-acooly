/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-02
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
 * dm_condition Entity
 *
 * @author acooly
 * @date 2024-06-02 19:10:44
 */
@Entity
@Table(name = "dm_condition")
@Getter
@Setter
@ExportModel(name = "dm_condition", border = true, headerShow = true)
public class DmCondition extends AbstractEntity {

    /**
     * 用户名
     */
	@Size(max = 255)
    @ExportColumn(header = "用户名", order = 1)
    private String userName;

    /**
     * 二级域名
     */
	@Size(max = 255)
    @ExportColumn(header = "二级域名", order = 2)
    private String accessAddress;

    /**
     * 国家限制
     */
	@Size(max = 255)
    @ExportColumn(header = "国家限制", order = 3)
    private String ipCountry;

    /**
     * 是否添加时区
     */
    @ExportColumn(header = "是否添加时区", order = 4)
    private Integer timeZone;

    /**
     * 时区洲
     */
	@Size(max = 255)
    @ExportColumn(header = "时区洲", order = 5)
    private String timeContinent;

    /**
     * 是否开启识别中文
     */
    @ExportColumn(header = "设备语言", order = 6)
    private Integer isChinese;

    /**
     * 语言
     */
    @ExportColumn(header = "设备语言", order = 7)
    private String language;

    /**
     * 移动设备
     */
    @ExportColumn(header = "移动设备", order = 8)
    private Integer isMobile;

    /**
     * 指定设备
     */
    @ExportColumn(header = "指定设备", order = 9)
    private Integer isSpecificDevice;

    /**
     * 访问路径
     */
    @ExportColumn(header = "访问路径", order = 10)
    private Integer isFbclid;

    private Integer isIp;

    private Integer isVpn;

    private Integer vpnCode;
    /**
     * ip限制
     */
    private Integer ipLimits;
    /**
     * 白名单开关
     */
    private Integer ipWhite;
    /**
     * 白名单
     */
    private String whiteList;
    /**
     * 是否机器人
     */
    private Integer isRobot;
    /**
     * 无法识别设备
     */
    private Integer isIdentify;
    /**
     * ios系统
     */
    private String iosVersion;
    /**
     * 安卓系统
     */
    private String andVersion;
    /**
     * 是否商业网络
     */
    private Integer isBusiness;
    /**
     * 虚拟机
     */
    private Integer isVirtual;
    /**
     * id时区和设备时区是否一致
     */
    private Integer timeMatch;
//    /**
//     * 手机型号id集合
//     */
//    private String phoneModelIds;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 是否带参数
     */
    private Integer isParams;
}
