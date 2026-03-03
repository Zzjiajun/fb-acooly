/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 *
 */
package com.acooly.showcase.shop.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.shop.entity.ShopProductImages;

import java.util.List;

/**
 * 商品图片表 Service接口
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
public interface ShopProductImagesService extends EntityService<ShopProductImages> {
    
    /**
     * 根据商品ID查询所有图片，按排序顺序
     */
    List<ShopProductImages> findByProductId(Long productId);
    
    /**
     * 查询商品的主图
     */
    ShopProductImages findPrimaryByProductId(Long productId);
    
    /**
     * 删除商品的所有图片
     */
    void deleteByProductId(Long productId);
    
    /**
     * 设置主图（将指定图片设为主图，其他设为非主图）
     */
    void setPrimaryImage(Long imageId, Long productId);

    ShopProductImages selectByProductIdAndIsPrimaryProductImages(Long id);
}
