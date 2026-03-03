package com.acooly.showcase.shop.dto;

import lombok.Data;

/**
 * 页面统计DTO
 *
 * @author acooly
 * @date 2026-01-04
 */
@Data
public class PageStatsDTO {
    /**
     * 页面类型
     */
    private String pageType;

    /**
     * 页面标识（URL或页面唯一Key）
     */
    private String pageKey;

    /**
     * PV（页面浏览量）
     */
    private Long pv;

    /**
     * UV（独立访客数）
     */
    private Long uv;

    /**
     * 平均停留时间（毫秒）
     */
    private Integer avgDuration;

    /**
     * 总停留时间（毫秒）
     */
    private Long totalDuration;
}

