/*
* acooly.cn Inc.
* Copyright (c) 2026 All Rights Reserved.
* create by acooly
* date:2026-01-04
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
 * 访问行为日志表（按日期分区，建议按月归档） Entity
 *
 * @author acooly
 * @date 2026-01-04 19:04:49
 */
@Entity
@Table(name = "shop_track_log")
@Getter
@Setter
@ExportModel(name = "访问行为日志表（按日期分区，建议按月归档）", border = true, headerShow = true)
public class ShopTrackLog extends AbstractEntity {

    /**
     * 访客ID（浏览器唯一标识，localStorage）
     */
	@Size(max = 64)
    @ExportColumn(header = "访客ID（浏览器唯一标识，localStorage）", order = 1)
    private String visitorId;

    /**
     * 会话ID（单次访问会话）
     */
	@Size(max = 64)
    @ExportColumn(header = "会话ID（单次访问会话）", order = 2)
    private String sessionId;

    /**
     * 页面类型：home/category/product/other
     */
	@NotBlank
	@Size(max = 20)
    @ExportColumn(header = "页面类型：home/category/product/other", order = 3)
    private String pageType;

    /**
     * 页面标识：URL或页面唯一Key
     */
	@NotBlank
	@Size(max = 255)
    @ExportColumn(header = "页面标识：URL或页面唯一Key", order = 4)
    private String pageKey;

    /**
     * 商品ID（商品页时必填，其他页面为NULL）
     */
    @ExportColumn(header = "商品ID（商品页时必填，其他页面为NULL）", order = 5)
    private Long productId;

    /**
     * 停留时间（毫秒）
     */
	@NotNull
    @ExportColumn(header = "停留时间（毫秒）", order = 6)
    private Integer stayDuration;

    /**
     * 语言代码：zh_CN/en_US/it_IT/ja_JP/ko_KR/fr_FR
     */
	@Size(max = 10)
    @ExportColumn(header = "语言代码：zh_CN/en_US/it_IT/ja_JP/ko_KR/fr_FR", order = 7)
    private String locale;

    /**
     * 浏览器UA
     */
	@Size(max = 500)
    @ExportColumn(header = "浏览器UA", order = 8)
    private String userAgent;

    /**
     * IP地址
     */
	@Size(max = 50)
    @ExportColumn(header = "IP地址", order = 9)
    private String ipAddress;

    /**
     * 来源页面
     */
	@Size(max = 500)
    @ExportColumn(header = "来源页面", order = 10)
    private String referer;

    /**
     * 设备类型：desktop/mobile/tablet
     */
	@Size(max = 255)
    @ExportColumn(header = "设备类型：desktop/mobile/tablet", order = 11)
    private String deviceType;

    /**
     * 浏览器：Chrome/Firefox/Safari
     */
	@Size(max = 255)
    @ExportColumn(header = "浏览器：Chrome/Firefox/Safari", order = 12)
    private String browser;

    /**
     * 来源
     */
	@Size(max = 255)
    @ExportColumn(header = "来源", order = 13)
    private String source;

    /**
     * 访问日期（用于分区和统计，格式：YYYY-MM-DD）
     */
    @ExportColumn(header = "访问日期（用于分区和统计，格式：YYYY-MM-DD）", order = 14)
    private Date visitDate;

}
