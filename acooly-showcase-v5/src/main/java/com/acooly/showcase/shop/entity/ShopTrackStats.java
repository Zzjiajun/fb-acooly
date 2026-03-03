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
 * 访问统计汇总表（提升查询性能） Entity
 *
 * @author acooly
 * @date 2026-01-04 19:04:50
 */
@Entity
@Table(name = "shop_track_stats")
@Getter
@Setter
@ExportModel(name = "访问统计汇总表（提升查询性能）", border = true, headerShow = true)
public class ShopTrackStats extends AbstractEntity {

    /**
     * 统计类型：daily_site/daily_page/daily_product
     */
	@NotBlank
	@Size(max = 20)
    @ExportColumn(header = "统计类型：daily_site/daily_page/daily_product", order = 1)
    private String statType;

    /**
     * 统计日期（格式：YYYY-MM-DD）
     */
	@NotNull
    @ExportColumn(header = "统计日期（格式：YYYY-MM-DD）", order = 2)
    private Date statDate;

    /**
     * 实体ID（商品ID，stat_type=daily_product时使用）
     */
    @ExportColumn(header = "实体ID（商品ID，stat_type=daily_product时使用）", order = 3)
    private Long entityId;

    /**
     * 实体标识（页面Key，stat_type=daily_page时使用）
     */
	@Size(max = 255)
    @ExportColumn(header = "实体标识（页面Key，stat_type=daily_page时使用）", order = 4)
    private String entityKey;

    /**
     * 语言代码（可为空表示全语言统计）
     */
	@Size(max = 10)
    @ExportColumn(header = "语言代码（可为空表示全语言统计）", order = 5)
    private String locale;

    /**
     * 页面浏览量（Page View）
     */
	@NotNull
    @ExportColumn(header = "页面浏览量（Page View）", order = 6)
    private Long pv;

    /**
     * 独立访客数（Unique Visitor）
     */
	@NotNull
    @ExportColumn(header = "独立访客数（Unique Visitor）", order = 7)
    private Long uv;

    /**
     * 总停留时间（毫秒）
     */
	@NotNull
    @ExportColumn(header = "总停留时间（毫秒）", order = 8)
    private Long totalDuration;

    /**
     * 平均停留时间（毫秒）
     */
	@NotNull
    @ExportColumn(header = "平均停留时间（毫秒）", order = 9)
    private Integer avgDuration;

    /**
     * 最大停留时间（毫秒）
     */
	@NotNull
    @ExportColumn(header = "最大停留时间（毫秒）", order = 10)
    private Integer maxDuration;

    /**
     * 最小停留时间（毫秒）
     */
	@NotNull
    @ExportColumn(header = "最小停留时间（毫秒）", order = 11)
    private Integer minDuration;

    /**
     * 跳出率（百分比，0-100）
     */
    @ExportColumn(header = "跳出率（百分比，0-100）", order = 12)
    private Long bounceRate;

}
