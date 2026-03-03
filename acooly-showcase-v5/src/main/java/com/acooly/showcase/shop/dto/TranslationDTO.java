package com.acooly.showcase.shop.dto;

import lombok.Data;

/**
 * 翻译DTO（用于保存）
 * 
 * @author acooly
 * @date 2025-12-26
 */
@Data
public class TranslationDTO {
    /**
     * 字段名称（name 或 description）
     */
    private String fieldName;
    
    /**
     * 语言代码（zh_CN, en_US 等）
     */
    private String locale;
    
    /**
     * 翻译内容
     */
    private String translation;
}

