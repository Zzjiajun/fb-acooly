package com.acooly.showcase.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品列表响应DTO
 * 包含商品列表、分类树和分页信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDTO {
    
    /**
     * 商品列表
     */
    private List<ProductDTO> products;
    
    /**
     * 分类树（完整的父子分类结构）
     */
    private CategoryTreeDTO categoryTree;
    
    /**
     * 分页信息
     */
    private PaginationDTO pagination;
    
    /**
     * 分页信息DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationDTO {
        private Integer page;        // 当前页码
        private Integer size;        // 每页大小
        private Long total;          // 总记录数
        private Integer totalPages;  // 总页数
    }
}

