package com.acooly.showcase.shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Double originalPrice;
    private Double rating;
    private Integer reviewCount;
    private String imageUrl;
    private String thumbWebpUrl;
    private String webpUrl;
    private Integer featured;
    private Integer freeShipping;
    private String description;
    private String serialNumber;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private List<AttrValueTagDTO> attrValueTags;

    // 多语言字段（用于ES存储，只存储非英文的其他语言）
    private Map<String, String> name_i18n_other;
    private Map<String, String> description_i18n_other;
}
