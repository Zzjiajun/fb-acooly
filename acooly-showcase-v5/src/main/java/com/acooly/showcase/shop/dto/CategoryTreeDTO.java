package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分类树结构DTO
 * 用于返回完整的父子分类层级关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeDTO {
    
    /**
     * 父级分类列表
     * 每个父级分类包含其子分类列表
     */
    private List<ParentCategoryWithSubsDTO> parentCategories;
    
    /**
     * 父级分类DTO（包含子分类）
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentCategoryWithSubsDTO {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String icon;
        private String image;
        private Integer sortOrder;
        private Boolean isActive;
        private Boolean isShow;
        private Integer productCount; // 该父分类下的商品总数
        
        /**
         * 子分类列表
         */
        private List<SubCategorySimpleDTO> subCategories;
    }
    
    /**
     * 子分类简化DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategorySimpleDTO {
        private Long id;
        private String name;
        private String slug;
        private String icon;
        private String image;
        private Integer productCount; // 该子分类下的商品数量
    }
}

