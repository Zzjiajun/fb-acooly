package com.acooly.showcase.shop.dto;

import lombok.Data;

/**
 * 商品统计DTO
 *
 * @author acooly
 * @date 2026-01-04
 */
@Data
public class ProductStatsDTO {
    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * PV（页面浏览量）
     */
    private Long pv;

    /**
     * UV（独立访客数）
     */
    private Long uv;

    /**
     * 总停留时间（毫秒）
     */
    private Long totalDuration;

    /**
     * 平均停留时间（毫秒）
     */
    private Integer avgDuration;

    /**
     * 最大停留时间（毫秒）
     */
    private Integer maxDuration;

    /**
     * 最小停留时间（毫秒）
     */
    private Integer minDuration;
}

