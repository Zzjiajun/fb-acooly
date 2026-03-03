package com.acooly.showcase.shop.service.impl;

import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopProductImages;
import com.acooly.showcase.shop.service.PrimaryImageService;
import com.acooly.showcase.shop.service.ProductImageService;
import com.acooly.showcase.shop.service.ShopProductImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 主图URL处理服务实现
 * 
 * @author acooly
 * @date 2025-12-18
 */
@Slf4j
@Service
public class PrimaryImageServiceImpl implements PrimaryImageService {

    @Autowired
    private ShopProductImagesService shopProductImagesService;

    @Override
    public String determinePrimaryImageUrl(String primaryImageUrl, HttpServletRequest request) {
        if (primaryImageUrl == null || primaryImageUrl.trim().isEmpty()) {
            return primaryImageUrl;
        }
        
        // 从request attribute获取压缩结果
        ImageProcessResult compressResult = 
                (ImageProcessResult) request.getAttribute("_imageCompressResult_" + primaryImageUrl);
        
        if (compressResult != null && compressResult.isSuccess()) {
            // 优先使用OSS WebP原图URL
            if (compressResult.getWebpOssUrl() != null) {
                log.debug("【主图URL】使用OSS WebP原图URL");
                return compressResult.getWebpOssUrl();
            }
            
            // 降级使用OSS缩略图WebP URL
            if (compressResult.getThumbWebpOssUrl() != null) {
                log.debug("【主图URL】降级使用OSS缩略图WebP URL");
                return compressResult.getThumbWebpOssUrl();
            }
        }
        
        // OSS上传失败，使用本地原图URL
        log.debug("【主图URL】OSS上传失败，使用本地原图");
        return primaryImageUrl;
    }

    @Override
    public String determineFinalImageUrl(ImageProcessResult compressResult,
                                         String originalUrl, String relative,
                                         String serverRoot, String storageRoot) {
        // 1. 优先使用OSS WebP原图URL
        if (compressResult.getWebpOssUrl() != null) {
            log.debug("【最终URL】使用OSS WebP原图URL");
            return compressResult.getWebpOssUrl();
        }
        
        // 2. 降级使用OSS缩略图WebP URL
        if (compressResult.getThumbWebpOssUrl() != null) {
            log.debug("【最终URL】降级使用OSS缩略图WebP URL");
            return compressResult.getThumbWebpOssUrl();
        }
        
        // 3. OSS上传失败，降级使用本地原图URL
        log.debug("【最终URL】OSS上传失败，降级使用本地原图");
        return originalUrl;
    }

    @Override
    public void syncPrimaryImageToProduct(ShopProducts product,
                                          ProductImageService.ProductImageProcessResult processResult,
                                          String originalImageUrl,
                                          boolean isCreate,
                                          HttpServletRequest request) {
        
        if (product == null) {
            log.warn("【主图同步】product为null，无法同步");
            return;
        }
        
        log.debug("【主图同步】开始同步主图URL - 商品ID: {}, 是否新增: {}", product.getId(), isCreate);
        
        if (isCreate) {
            // 新增商品
            if (processResult.getPrimaryImageUrl() != null) {
                // 有主图，确定最终URL（优先OSS WebP，失败降级到本地原图完整URL）
                // processResult.getPrimaryImageUrl() 已经是完整URL（包含域名）
                String finalPrimaryUrl = determinePrimaryImageUrl(processResult.getPrimaryImageUrl(), request);
                // 确保是完整URL（包含域名）
                product.setImageUrl(finalPrimaryUrl);
                log.debug("【主图同步】新增商品，设置主图URL - 商品ID: {}", product.getId());
            } else {
                // 没有图片，检查是否有外部URL
                String imageUrlFromForm = request.getParameter("imageUrl");
                if (imageUrlFromForm != null && !imageUrlFromForm.trim().isEmpty()) {
                    // 外部URL应该已经是完整的，直接使用
                    product.setImageUrl(imageUrlFromForm.trim());
                    log.debug("【主图同步】新增商品，使用外部URL - 商品ID: {}", product.getId());
                }
            }
        } else {
            // 编辑商品
            if (product.getId() != null) {
                // 获取当前数据库中的主图记录
                ShopProductImages currentPrimary = shopProductImagesService.findPrimaryByProductId(product.getId());
                String currentPrimaryImageUrl = currentPrimary != null ? currentPrimary.getImageUrl() : null;

                // 判断主图是否发生变化
                String newPrimaryImageUrl = processResult.getPrimaryImageUrl();

                if (newPrimaryImageUrl != null) {
                    // 【重要】确定最终主图URL：优先使用ShopProductImages.webpUrl，如果为空则使用imageUrl
                    // 1. 先尝试从request attribute获取压缩结果（新上传的图片）
                    String finalPrimaryImageUrl = determinePrimaryImageUrl(newPrimaryImageUrl, request);
                    
                    // 2. 【新增】如果request中没有压缩结果，尝试从数据库查询主图记录，检查webpUrl字段
                    // 注意：此时主图可能还没有保存到数据库，所以需要查询最新的主图记录
                    // 但更可靠的方法是：在保存图片后，重新查询主图记录
                    // 由于此时图片可能还没有保存，我们先使用determinePrimaryImageUrl的结果
                    // 在Controller的saveJson/updateJson中，图片已经保存，可以重新查询
                    
                    // 有新的主图URL
                    if (currentPrimaryImageUrl == null || !currentPrimaryImageUrl.equals(finalPrimaryImageUrl)) {
                        // 主图发生了变化
                        // 【重要】此时图片可能还没有保存到数据库，所以使用determinePrimaryImageUrl的结果
                        // 在Controller的saveJson/updateJson中，图片保存后会重新查询并检查webpUrl字段
                        product.setImageUrl(finalPrimaryImageUrl);
                        processResult.setPrimaryImageChanged(true);
                        log.info("【主图同步】主图发生变化 - 商品ID: {}", product.getId());
                    } else {
                        // 主图没有变化，但需要检查webpUrl字段
                        // 【重要】重新查询主图记录，检查webpUrl字段
                        ShopProductImages latestPrimary = shopProductImagesService.findPrimaryByProductId(product.getId());
                        String finalUrl = null;
                        if (latestPrimary != null) {
                            // 优先使用webpUrl，如果为空则使用imageUrl
                            if (latestPrimary.getWebpUrl() != null && !latestPrimary.getWebpUrl().trim().isEmpty()) {
                                finalUrl = latestPrimary.getWebpUrl();
                                log.debug("【主图同步】使用主图记录的webpUrl - 商品ID: {}", product.getId());
                            } else {
                                finalUrl = latestPrimary.getImageUrl();
                                log.debug("【主图同步】主图记录的webpUrl为空，使用imageUrl - 商品ID: {}", product.getId());
                            }
                        }
                        
                        // 如果查询到最终URL，使用它；否则使用原始imageUrl
                        product.setImageUrl(finalUrl != null ? finalUrl : (originalImageUrl != null ? originalImageUrl : currentPrimaryImageUrl));
                        processResult.setPrimaryImageChanged(false);
                        log.debug("【主图同步】主图未变化 - 商品ID: {}", product.getId());
                    }
                } else {
                    // 没有主图（所有图片被删除的情况）
                    product.setImageUrl(originalImageUrl);
                    processResult.setPrimaryImageChanged(false);
                    log.debug("【主图同步】没有主图，保持原始imageUrl - 商品ID: {}", product.getId());
                }
            }
        }
    }
}

