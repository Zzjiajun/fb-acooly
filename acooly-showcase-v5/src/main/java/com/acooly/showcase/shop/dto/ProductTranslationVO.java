package com.acooly.showcase.shop.dto;

import lombok.Data;

import java.util.List;

/**
 * 商品翻译视图对象
 * 
 * @author acooly
 * @date 2025-12-26
 */
@Data
public class ProductTranslationVO {
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 所有语言的翻译列表
     */
    private List<LocaleTranslationVO> locales;
}

