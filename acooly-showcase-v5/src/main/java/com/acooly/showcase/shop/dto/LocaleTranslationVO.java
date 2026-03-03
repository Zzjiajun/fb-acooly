package com.acooly.showcase.shop.dto;

import lombok.Data;

import java.util.Map;

/**
 * 语言翻译视图对象
 * 
 * @author acooly
 * @date 2025-12-26
 */
@Data
public class LocaleTranslationVO {
    /**
     * 语言代码（如：zh_CN, en_US）
     */
    private String localeCode;
    
    /**
     * 语言名称（如：简体中文, English）
     */
    private String localeName;
    
    /**
     * 是否默认语言
     */
    private Boolean isDefault;
    
    /**
     * 翻译内容 Map<fieldName, translation>
     * 例如：{name: "商品名称", description: "商品描述"}
     */
    private Map<String, String> translations;
    
    /**
     * 是否有name字段的翻译
     */
    private Boolean hasName;
    
    /**
     * 是否有description字段的翻译
     */
    private Boolean hasDescription;
}

