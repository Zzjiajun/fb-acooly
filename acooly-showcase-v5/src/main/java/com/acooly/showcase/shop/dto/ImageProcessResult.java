package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageProcessResult {
    private String originalPath;
    private Long originalSize;
    private Integer originalWidth;  // 原图宽度（本地原图）
    private Integer originalHeight; // 原图高度（本地原图）

    private String webpPath;
    private Long webpSize;
    private Integer webpWidth;  // WebP原图宽度（OSS WebP实际尺寸）
    private Integer webpHeight; // WebP原图高度（OSS WebP实际尺寸）

    private String thumbWebpPath;
    private Long thumbWebpSize;
    private Integer thumbWidth;
    private Integer thumbHeight;

    // 【新增】OSS URL
    private String webpOssUrl;  // WebP版本的OSS URL
    private String thumbWebpOssUrl;  // 缩略图WebP版本的OSS URL（优先使用）
    private String originalOssUrl;  // 原图的OSS URL（可选）

    private String format;
    private boolean success;
    private String errorMessage;
}
