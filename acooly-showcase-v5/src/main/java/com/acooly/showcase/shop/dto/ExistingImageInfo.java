package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 已存在的图片信息DTO（编辑时）
 *
 * @author acooly
 * @date 2025-12-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingImageInfo {
    private Long imageId;               // 图片记录ID
    private String imageUrl;            // 图片URL
    private Integer sortOrder;         // 排序
    private Integer isPrimary;         // 是否主图
    private Boolean keep;              // 是否保留
}


