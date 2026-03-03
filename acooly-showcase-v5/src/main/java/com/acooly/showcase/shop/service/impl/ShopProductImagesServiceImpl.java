/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.dao.ShopProductImagesDao;
import com.acooly.showcase.shop.entity.ShopProductImages;
import com.acooly.showcase.shop.service.ShopProductImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品图片表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Slf4j
@Service("shopProductImagesService")
public class ShopProductImagesServiceImpl extends EntityServiceImpl<ShopProductImages, ShopProductImagesDao> implements ShopProductImagesService {

    @Autowired
    private ShopProductImagesService shopProductImagesService;
    @Override
    public List<ShopProductImages> findByProductId(Long productId) {
        if (productId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_productId", productId);
        List<ShopProductImages> productImagesList = shopProductImagesService.query(params, null);
        return productImagesList;
    }

    @Override
    public ShopProductImages findPrimaryByProductId(Long productId) {
        if (productId == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("EQ_productId", productId);
        params.put("EQ_isPrimary", 1);
        List<ShopProductImages> images = shopProductImagesService.query(params, null);
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }

    @Override
    public void deleteByProductId(Long productId) {
        if (productId == null) {
            return;
        }
        List<ShopProductImages> images = findByProductId(productId);
        if (images != null && !images.isEmpty()) {
            for (ShopProductImages image : images) {
                removeById(Long.valueOf(image.getId()));
            }
            log.info("删除商品所有图片 - 商品ID: {}, 图片数量: {}", productId, images.size());
        }
    }

    @Override
    public void setPrimaryImage(Long imageId, Long productId) {
        if (imageId == null || productId == null) {
            return;
        }
        
        // 1. 将该商品的所有图片设为非主图
        List<ShopProductImages> allImages = findByProductId(productId);
        if (allImages != null) {
            for (ShopProductImages img : allImages) {
                if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                    img.setIsPrimary(0);
                    update(img);
                }
            }
        }
        
        // 2. 设置指定图片为主图
        ShopProductImages targetImage = get(imageId);
        if (targetImage != null && targetImage.getProductId().equals(productId)) {
            targetImage.setIsPrimary(1);
            update(targetImage);
            log.info("设置主图成功 - 图片ID: {}, 商品ID: {}", imageId, productId);
        }
    }

    @Override
    public ShopProductImages selectByProductIdAndIsPrimaryProductImages(Long id) {
        return null;
    }
}
