package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性值标签DTO
 * 用于商品列表中展示属性值标签，如：[商务]、[真皮] 等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttrValueTagDTO {
    /**
     * 属性值ID
     */
    private Long id;
    
    /**
     * 属性值（如："商务"、"真皮"）
     */
    private String value;
    
    /**
     * 属性名称（如："风格"、"材质"）
     */
    private String attrName;
    
    /**
     * 展示文本（如："[商务]"），由前端或后端格式化
     */
    private String displayText;
}

