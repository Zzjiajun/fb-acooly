package com.acooly.showcase.shop.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private Double originalPrice;
    private Double rating;
    private Integer reviewCount;
    private String imageUrl;
    private List<String> images;
    private Boolean featured;
    private Boolean freeShipping;
    private String description;
    private List<String> features;
    private Map<String, List<String>> specifications;
}