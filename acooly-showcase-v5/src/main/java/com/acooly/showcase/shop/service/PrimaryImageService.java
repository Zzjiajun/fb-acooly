package com.acooly.showcase.shop.service;

import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopProductImages;

import javax.servlet.http.HttpServletRequest;

/**
 * 主图URL处理服务接口
 * 负责：确定主图URL、同步主图URL到商品实体
 *
 * @author acooly
 * @date 2025-12-18
 */
public interface PrimaryImageService {

    /**
     * 确定主图URL（优先OSS WebP，失败降级到本地原图）
     * 
     * @param primaryImageUrl 主图URL（可能是本地路径）
     * @param request HTTP请求
     * @return 最终主图URL（优先OSS WebP，失败降级到本地原图）
     */
    String determinePrimaryImageUrl(String primaryImageUrl, HttpServletRequest request);

    /**
     * 确定最终图片URL：优先OSS WebP，失败降级到本地原图
     * 
     * @param compressResult 压缩结果
     * @param originalUrl 原图URL（降级使用）
     * @param relative 相对路径
     * @param serverRoot 服务器根路径
     * @param storageRoot 存储根路径
     * @return 最终图片URL
     */
    String determineFinalImageUrl(ImageProcessResult compressResult,
                                 String originalUrl, String relative,
                                 String serverRoot, String storageRoot);

    /**
     * 同步主图URL到商品实体
     * 
     * @param product 商品实体
     * @param processResult 图片处理结果
     * @param originalImageUrl 原始imageUrl
     * @param isCreate 是否新增
     * @param request HTTP请求
     */
    void syncPrimaryImageToProduct(ShopProducts product,
                                   ProductImageService.ProductImageProcessResult processResult,
                                   String originalImageUrl,
                                   boolean isCreate,
                                   HttpServletRequest request);
}


