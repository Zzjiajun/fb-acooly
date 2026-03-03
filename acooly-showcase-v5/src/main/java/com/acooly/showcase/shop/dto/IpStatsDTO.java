package com.acooly.showcase.shop.dto;

import lombok.Data;

/**
 * IP分布统计DTO
 *
 * @author acooly
 * @date 2026-01-04
 */
@Data
public class IpStatsDTO {
    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 访问次数（PV）
     */
    private Long visitCount;

    /**
     * 独立访客数（UV）
     */
    private Long uniqueVisitorCount;

    /**
     * 总停留时间（毫秒）
     */
    private Long totalDuration;

    /**
     * 平均停留时间（毫秒）
     */
    private Integer avgDuration;

    /**
     * 访问日期
     */
    private String visitDate;
}

