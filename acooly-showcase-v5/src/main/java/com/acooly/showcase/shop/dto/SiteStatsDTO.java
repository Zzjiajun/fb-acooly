package com.acooly.showcase.shop.dto;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 网站统计DTO
 *
 * @author acooly
 * @date 2026-01-04
 */
@Data
public class SiteStatsDTO {
    /**
     * 统计日期（格式：yyyy-MM-dd 或 yyyy-MM-dd ~ yyyy-MM-dd）
     */
    private String date;

    /**
     * 总PV（页面浏览量）
     */
    private Long totalPv;

    /**
     * 总UV（独立访客数）
     */
    private Long totalUv;

    /**
     * 平均停留时间（毫秒）
     */
    private Integer avgDuration;

    /**
     * 商品访问率（百分比）
     */
    private Double productVisitRate;

    /**
     * 总IP数
     */
    private Long totalIp;

    /**
     * 跳出率（百分比）
     */
    private Double bounceRate;

    /**
     * 按页面类型统计
     * Key: 页面类型（如：home, product, category等）
     * Value: 该页面类型的统计信息
     */
    private Map<String, PageTypeStats> pageTypeStats = new HashMap<>();

    /**
     * 页面类型统计内部类
     */
    @Data
    public static class PageTypeStats {
        /**
         * 页面类型
         */
        private String pageType;

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
    }
}
