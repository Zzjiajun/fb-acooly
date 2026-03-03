package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传的图片信息DTO
 *
 * @author acooly
 * @date 2025-12-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadedImageInfo {
    private MultipartFile file;        // 文件对象
    private String imageUrl;            // 上传后的URL（上传后填充）
    private Integer sortOrder;         // 排序
    private Boolean isPrimary;         // 是否主图
}


