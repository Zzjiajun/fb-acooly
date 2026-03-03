package com.acooly.showcase.shop.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.showcase.shop.dto.ExistingImageInfo;
import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.dto.UploadedImageInfo;
import com.acooly.showcase.shop.entity.ShopProductImages;
import com.acooly.showcase.shop.service.OssStorageService;
import com.acooly.showcase.shop.service.ProductImageService;
import com.acooly.showcase.shop.service.ShopProductImagesService;
import com.acooly.showcase.shop.utils.ImageCompressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 商品图片处理服务实现
 * 
 * @author acooly
 * @date 2025-12-18
 */
@Slf4j
@Service
public class ProductImageServiceImpl implements ProductImageService {

    // ========== 配置常量 ==========
    private static final String STORAGE_NAMESPACE = "shop/products";
    private static final String IMAGE_FILE_FIELD_NAME = "imageFile";
    private static final int MAX_RETRY_TIMES = 3;  // 最大重试次数
    private static final long RETRY_DELAY_MS = 1000;  // 重试延迟（毫秒）

    @Autowired(required = false)
    private ImageCompressUtil imageCompressUtil;

    @Autowired(required = false)
    private OssStorageService ossStorageService;

    @Autowired
    private ShopProductImagesService shopProductImagesService;

    @Autowired(required = false)
    private OFileProperties oFileProperties;
    
    // ========== 配置项 ==========
    @org.springframework.beans.factory.annotation.Value("${image.upload.thumbnail.enabled:false}")
    private boolean thumbnailEnabled;

    @Override
    public ProductImageProcessResult processProductImageUpload(
            HttpServletRequest request,
            Long productId,
            boolean isCreate) {
        
        long startTime = System.currentTimeMillis();
        log.info("【图片处理】开始处理商品图片上传 - 商品ID: {}, 是否新增: {}", productId, isCreate);
        
        ProductImageProcessResult result = new ProductImageProcessResult();
        
        try {
            // 1. 检查是否有图片变化
            if (!hasImageChanges(request, productId, isCreate)) {
                log.debug("【图片处理】没有图片变化，跳过处理");
                return result;
            }

            // 2. 上传文件到本地（需要在Controller中调用doUpload，这里返回空Map）
            // 注意：doUpload是Controller的父类方法，无法在Service中直接调用
            // 所以这个方法需要在Controller中先调用doUpload，然后传入结果
            Map<String, String> uploadedFileUrls = new HashMap<>();
            log.warn("【图片处理】uploadFilesToLocal需要在Controller中实现，当前返回空Map");

            // 3. 批量压缩并上传OSS（带重试）
            // 注意：需要从request attribute中获取本地文件路径
            Map<String, ImageProcessResult> compressResults = new HashMap<>();
            log.warn("【图片处理】compressAndUploadToOssBatch需要在Controller中实现，当前返回空Map");

            // 4. 处理图片逻辑（合并、删除、主图）
            // 注意：这个方法需要从Controller的processProductImages方法中提取
            log.warn("【图片处理】processImagesLogic需要在Controller中实现");

            long duration = System.currentTimeMillis() - startTime;
            log.info("【图片处理】处理完成 - 商品ID: {}, 耗时: {}ms", productId, duration);
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("【图片处理】处理失败 - 商品ID: {}, 耗时: {}ms", productId, duration, e);
            throw new BusinessException("IMAGE_PROCESS_ERROR", "图片处理失败：" + e.getMessage(), "");
        }
    }

    /**
     * 处理上传后的文件：压缩、转换、上传OSS（批量，带重试）
     * 这个方法在Controller中调用doUpload后使用
     * 
     * @param uploadedFileUrls Map，key为参数名，value为上传后的相对路径（如 /shop/products/2025/12/18/image.jpg）
     * @param request HTTP请求
     * @return 压缩结果Map，key为图片URL，value为处理结果
     */
    @Override
    public Map<String, ImageProcessResult> processUploadedFiles(
            Map<String, String> uploadedFileUrls,
            HttpServletRequest request) {
        
        long startTime = System.currentTimeMillis();
        log.info("【批量处理】开始批量处理上传文件 - 文件数量: {}", uploadedFileUrls.size());
        
        Map<String, ImageProcessResult> compressResults = new HashMap<>();
        List<File> localFiles = new ArrayList<>();
        Map<String, String> fileUrlMap = new HashMap<>();  // 文件路径 -> 图片URL的映射
        
        String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
                ? oFileProperties.getServerRoot().replaceAll("/+$", "") : "";
        String storageRoot = oFileProperties != null && oFileProperties.getStorageRoot() != null
                ? oFileProperties.getStorageRoot() : "";
        
        // 1. 收集所有本地文件路径
        for (Map.Entry<String, String> entry : uploadedFileUrls.entrySet()) {
            String relative = entry.getValue();
            if (relative != null && !relative.trim().isEmpty()) {
                String fullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;
                String fullPath = storageRoot + File.separator + relative.replace("/", File.separator);
                File localFile = new File(fullPath);
                
                if (localFile.exists() && localFile.isFile() && isImageFile(localFile)) {
                    localFiles.add(localFile);
                    fileUrlMap.put(localFile.getAbsolutePath(), fullUrl);
                    log.debug("【批量处理】收集文件 - 路径: {}, URL: {}", fullPath, fullUrl);
                }
            }
        }
        
        // 2. 批量压缩并上传OSS（带重试）
        if (!localFiles.isEmpty()) {
            Map<String, ImageProcessResult> batchResults = compressAndUploadToOssWithRetry(localFiles, STORAGE_NAMESPACE);
            
            // 3. 将结果映射到图片URL
            for (Map.Entry<String, ImageProcessResult> entry : batchResults.entrySet()) {
                String filePath = entry.getKey();
                ImageProcessResult result = entry.getValue();
                String imageUrl = fileUrlMap.get(filePath);
                if (imageUrl != null) {
                    compressResults.put(imageUrl, result);
                    
                    // 保存压缩结果到request attribute（使用图片URL作为key）
                    request.setAttribute("_imageCompressResult_" + imageUrl, result);
                    
                    // 确定最终URL：优先OSS WebP，失败降级到本地原图
                    String finalUrl = determineFinalImageUrl(result, imageUrl, 
                            extractRelativePath(imageUrl, serverRoot), serverRoot, storageRoot);
                    if (!finalUrl.equals(imageUrl)) {
                        // 如果最终URL不同，也保存一份
                        request.setAttribute("_imageCompressResult_" + finalUrl, result);
                    }
                }
            }
        }
        
        long duration = System.currentTimeMillis() - startTime;
        long successCount = compressResults.values().stream().filter(ImageProcessResult::isSuccess).count();
        log.info("【批量处理】批量处理完成 - 总数: {}, 成功: {}, 失败: {}, 耗时: {}ms", 
                localFiles.size(), successCount, localFiles.size() - successCount, duration);
        
        return compressResults;
    }

    /**
     * 处理图片逻辑：合并新上传的图片和已存在的图片
     * 从Controller的processProductImages方法提取
     */
    @Override
    public ProductImageProcessResult processProductImages(
            Long productId,
            List<UploadedImageInfo> uploadedImages,
            List<ExistingImageInfo> existingImages,
            Map<String, ImageProcessResult> compressResults,
            HttpServletRequest request) {
        
        long startTime = System.currentTimeMillis();
        log.info("【图片逻辑】开始处理图片逻辑 - 商品ID: {}, 新图片: {}张, 已存在: {}张", 
                productId, uploadedImages.size(), existingImages.size());
        
        ProductImageProcessResult result = new ProductImageProcessResult();
        List<ShopProductImages> toSave = new ArrayList<>();
        List<Long> toDelete = new ArrayList<>();
        String primaryImageUrl = null;
        result.setPrimaryImageChanged(false);
        
        // 【新增】检查前端是否指定了图片排序顺序
        String imageSortOrdersStr = request.getParameter("imageSortOrders");
        Map<Long, Integer> imageSortOrderMap = new HashMap<>();
        boolean hasCustomSortOrder = false;
        if (imageSortOrdersStr != null && !imageSortOrdersStr.trim().isEmpty()) {
            try {
                String[] pairs = imageSortOrdersStr.split(",");
                for (String pair : pairs) {
                    if (pair != null && !pair.trim().isEmpty()) {
                        String[] parts = pair.trim().split(":");
                        if (parts.length == 2) {
                            Long imageId = Long.parseLong(parts[0].trim());
                            Integer sortOrder = Integer.parseInt(parts[1].trim());
                            imageSortOrderMap.put(imageId, sortOrder);
                        }
                    }
                }
                hasCustomSortOrder = !imageSortOrderMap.isEmpty();
                log.info("【图片排序】检测到前端指定的排序顺序 - 排序映射: {}, 有自定义排序: {}", imageSortOrderMap, hasCustomSortOrder);
            } catch (Exception e) {
                log.warn("解析imageSortOrders失败: {}", imageSortOrdersStr, e);
            }
        }
        
        // 1. 处理已存在的图片
        String existingPrimaryImageUrl = null;
        Long originalPrimaryImageId = null;
        boolean isPrimaryDeleted = false;
        int maxSortOrder = 0;
        
        if (productId != null) {
            ShopProductImages originalPrimary = shopProductImagesService.findPrimaryByProductId(productId);
            if (originalPrimary != null) {
                originalPrimaryImageId = originalPrimary.getId();
            }
        }
        
        for (ExistingImageInfo existing : existingImages) {
            if (existing.getKeep()) {
                ShopProductImages imageEntity = new ShopProductImages();
                imageEntity.setId(existing.getImageId());
                imageEntity.setProductId(productId);
                imageEntity.setImageUrl(existing.getImageUrl());
                imageEntity.setSortOrder(existing.getSortOrder());
                imageEntity.setIsPrimary(existing.getIsPrimary());
                toSave.add(imageEntity);
                
                if (existing.getIsPrimary() == 1) {
                    existingPrimaryImageUrl = existing.getImageUrl();
                }
                
                if (existing.getSortOrder() != null && existing.getSortOrder() > maxSortOrder) {
                    maxSortOrder = existing.getSortOrder();
                }
            } else {
                toDelete.add(existing.getImageId());
                if (existing.getImageId() != null && existing.getImageId().equals(originalPrimaryImageId)) {
                    isPrimaryDeleted = true;
                }
            }
        }
        
        // 2. 处理新上传的图片
        int nextSortOrder = maxSortOrder + 1;
        boolean hasNewPrimary = false;
        
        for (UploadedImageInfo uploaded : uploadedImages) {
            String imageUrl = uploaded.getImageUrl();
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                continue;
            }
            
            ShopProductImages imageEntity = new ShopProductImages();
            imageEntity.setProductId(productId);
            // 【重要】imageUrl保存本地原图的完整URL（包含域名），不是WebP转换后的地址
            // imageUrl已经是完整URL（在Controller中已转换为完整URL）
            imageEntity.setImageUrl(imageUrl);
            imageEntity.setSortOrder(uploaded.getSortOrder() != null ? uploaded.getSortOrder() : nextSortOrder++);
            
            // 从compressResults获取压缩结果
            ImageProcessResult compressResult = compressResults.get(imageUrl);
            if (compressResult != null && compressResult.isSuccess()) {
                // 【简化方案】只保存WebP原图URL和尺寸，用于前端动态处理
                // webpUrl保存OSS WebP原图的完整URL（基础URL，用于动态处理）
                imageEntity.setWebpUrl(compressResult.getWebpOssUrl());
                
                // 【简化方案】只保存WebP原图的尺寸（width, height, fileSize）
                // 【重要】确保保存的是OSS WebP原图的实际尺寸，而不是本地原图的尺寸
                // 优先使用从WebP文件读取的实际尺寸，如果不存在则使用原图尺寸（通常WebP保持原图尺寸）
                Integer webpWidth = compressResult.getWebpWidth() != null ? 
                                   compressResult.getWebpWidth() : compressResult.getOriginalWidth();
                Integer webpHeight = compressResult.getWebpHeight() != null ? 
                                    compressResult.getWebpHeight() : compressResult.getOriginalHeight();
                
                imageEntity.setWidth(webpWidth);   // OSS WebP原图实际宽度
                imageEntity.setHeight(webpHeight); // OSS WebP原图实际高度
                imageEntity.setFileSize(compressResult.getWebpSize() != null ? 
                                       compressResult.getWebpSize() : 
                                       compressResult.getOriginalSize()); // WebP文件大小
                imageEntity.setFormat("webp"); // 固定为webp格式
                
                log.debug("【图片保存】保存WebP尺寸 - WebP实际尺寸: {}x{} (原图尺寸: {}x{})", 
                        webpWidth, webpHeight, 
                        compressResult.getOriginalWidth(), compressResult.getOriginalHeight());
                
                // 【简化方案】缩略图功能通过开关控制
                if (thumbnailEnabled && compressResult.getThumbWebpOssUrl() != null) {
                    // 启用缩略图功能时，保存缩略图信息
                    imageEntity.setThumbWebpUrl(compressResult.getThumbWebpOssUrl());
                    imageEntity.setThumbWidth(compressResult.getThumbWidth());
                    imageEntity.setThumbHeight(compressResult.getThumbHeight());
                    imageEntity.setThumbSize(compressResult.getThumbWebpSize());
                    log.debug("【图片保存】缩略图功能已启用 - thumbWebpUrl: {}", compressResult.getThumbWebpOssUrl());
                } else {
                    // 跳过缩略图生成，字段设为null
                    imageEntity.setThumbWebpUrl(null);
                    imageEntity.setThumbWidth(null);
                    imageEntity.setThumbHeight(null);
                    imageEntity.setThumbSize(null);
                }
                
                // 【简化方案】不再保存原图尺寸和webpSize（与fileSize重复）
                // imageEntity.setWebpSize(null); // 与fileSize重复，不保存
                
                log.debug("【图片保存】设置图片信息（简化方案） - imageUrl: {}（降级方案）, webpUrl: {}（基础URL）, width: {}, height: {}, fileSize: {}", 
                        imageUrl, compressResult.getWebpOssUrl(), 
                        imageEntity.getWidth(), imageEntity.getHeight(), imageEntity.getFileSize());
            } else {
                // OSS上传失败，只保存本地原图URL（降级方案）
                log.warn("【图片保存】OSS上传失败，只保存本地原图URL（降级方案） - imageUrl: {}", imageUrl);
                imageEntity.setWebpUrl(null);
                imageEntity.setWidth(null);
                imageEntity.setHeight(null);
                imageEntity.setFileSize(null);
                imageEntity.setFormat(null);
            }
            
            // 判断是否设为主图
            String primaryImageFileName = request.getParameter("primaryImageUrl");
            boolean isPrimaryByFileName = false;
            if (primaryImageFileName != null && !primaryImageFileName.trim().isEmpty() && uploaded.getFile() != null) {
                String uploadedFileName = uploaded.getFile().getOriginalFilename();
                if (uploadedFileName != null && uploadedFileName.equals(primaryImageFileName.trim())) {
                    isPrimaryByFileName = true;
                }
            }
            
            boolean userSpecifiedPrimary = uploaded.getIsPrimary() != null && uploaded.getIsPrimary();
            boolean shouldBePrimary = false;
            
            if (isPrimaryByFileName || userSpecifiedPrimary) {
                shouldBePrimary = true;
            } else if (existingPrimaryImageUrl == null && !hasNewPrimary) {
                shouldBePrimary = true;
            }
            
            if (shouldBePrimary) {
                for (ShopProductImages img : toSave) {
                    if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                        img.setIsPrimary(0);
                    }
                }
                imageEntity.setIsPrimary(1);
                primaryImageUrl = imageUrl;
                hasNewPrimary = true;
                if (existingPrimaryImageUrl != null && !existingPrimaryImageUrl.equals(imageUrl)) {
                    result.setPrimaryImageChanged(true);
                } else if (existingPrimaryImageUrl == null) {
                    result.setPrimaryImageChanged(true);
                }
            } else {
                imageEntity.setIsPrimary(0);
            }
            
            toSave.add(imageEntity);
        }
        
        // 3. 如果没有新主图，使用保留的主图
        if (!hasNewPrimary) {
            ShopProductImages newPrimaryInToSave = null;
            for (ShopProductImages img : toSave) {
                if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                    newPrimaryInToSave = img;
                    break;
                }
            }
            
            if (newPrimaryInToSave != null) {
                primaryImageUrl = newPrimaryInToSave.getImageUrl();
                if (originalPrimaryImageId != null && newPrimaryInToSave.getId() != null) {
                    if (!originalPrimaryImageId.equals(newPrimaryInToSave.getId())) {
                        result.setPrimaryImageChanged(true);
                    }
                } else if (originalPrimaryImageId == null && newPrimaryInToSave.getId() != null) {
                    result.setPrimaryImageChanged(true);
                }
            } else if (existingPrimaryImageUrl != null) {
                primaryImageUrl = existingPrimaryImageUrl;
            }
        }
        
        // 4. 如果删除了主图，自动设置新主图
        if (isPrimaryDeleted && !hasNewPrimary && existingPrimaryImageUrl == null && !toSave.isEmpty()) {
            ShopProductImages firstImage = toSave.get(0);
            firstImage.setIsPrimary(1);
            primaryImageUrl = firstImage.getImageUrl();
            result.setPrimaryImageChanged(true);
        }
        
        // 5. 确保至少有一张主图
        if (primaryImageUrl == null && !toSave.isEmpty()) {
            ShopProductImages firstImage = toSave.get(0);
            firstImage.setIsPrimary(1);
            primaryImageUrl = firstImage.getImageUrl();
            if (productId == null || existingPrimaryImageUrl == null) {
                result.setPrimaryImageChanged(true);
            }
        }
        
        // 6. 确保只有一个主图
        boolean foundPrimary = false;
        for (ShopProductImages img : toSave) {
            if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                if (foundPrimary) {
                    img.setIsPrimary(0);
                } else {
                    foundPrimary = true;
                    primaryImageUrl = img.getImageUrl();
                }
            }
        }
        
        // 7. 按排序顺序重新编号（确保连续）
        // 【修复】如果前端指定了排序顺序，直接使用前端的排序值；否则自动排序
        if (hasCustomSortOrder && !imageSortOrderMap.isEmpty()) {
            // 前端指定了排序，直接使用前端指定的排序值
            // 先按前端指定的排序值排序
            toSave.sort((a, b) -> {
                Integer orderA = imageSortOrderMap.get(a.getId());
                Integer orderB = imageSortOrderMap.get(b.getId());
                // 如果前端没有指定该图片的排序，放到最后
                if (orderA == null) orderA = Integer.MAX_VALUE;
                if (orderB == null) orderB = Integer.MAX_VALUE;
                return Integer.compare(orderA, orderB);
            });
            
            // 直接使用前端指定的排序值
            int maxCustomSort = 0;
            for (ShopProductImages img : toSave) {
                if (img.getId() != null && imageSortOrderMap.containsKey(img.getId())) {
                    Integer customSort = imageSortOrderMap.get(img.getId());
                    img.setSortOrder(customSort);
                    if (customSort > maxCustomSort) {
                        maxCustomSort = customSort;
                    }
                    log.debug("【图片排序】应用前端排序 - 图片ID: {}, 排序: {}", img.getId(), customSort);
                } else {
                    // 新上传的图片，使用递增排序（从最大排序值+1开始）
                    img.setSortOrder(++maxCustomSort);
                    log.debug("【图片排序】新图片自动排序 - 图片ID: {}, 排序: {}", img.getId(), maxCustomSort);
                }
            }
            log.info("【图片排序】使用前端指定的排序顺序，共{}张图片", toSave.size());
        } else {
            // 没有前端指定排序，按当前排序值排序后重新编号
            toSave.sort((a, b) -> {
                int orderA = a.getSortOrder() != null ? a.getSortOrder() : Integer.MAX_VALUE;
                int orderB = b.getSortOrder() != null ? b.getSortOrder() : Integer.MAX_VALUE;
                return Integer.compare(orderA, orderB);
            });
            // 重新编号为连续值
            for (int i = 0; i < toSave.size(); i++) {
                toSave.get(i).setSortOrder(i + 1);
            }
            log.debug("【图片排序】使用自动排序，重新编号为连续值");
        }
        
        result.setToSave(toSave);
        result.setToDelete(toDelete);
        result.setPrimaryImageUrl(primaryImageUrl);
        result.setCompressResults(compressResults);
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("【图片逻辑】处理完成 - 商品ID: {}, 耗时: {}ms, 保存: {}条, 删除: {}条, 主图变化: {}", 
                productId, duration, toSave.size(), toDelete.size(), result.isPrimaryImageChanged());
        
        return result;
    }

    @Override
    public List<CompletableFuture<ImageProcessResult>> compressAndUploadToOssAsync(
            List<File> localFiles,
            String namespace) {
        
        log.info("【异步处理】开始异步处理图片 - 文件数量: {}", localFiles.size());
        
        // 注意：这个方法返回List<CompletableFuture>，不需要@Async注解
        // 因为每个文件的处理已经是异步的（通过CompletableFuture.supplyAsync）
        // 如果需要使用配置的线程池，需要注入Executor，这里暂时使用默认的ForkJoinPool
        // 如果需要使用imageProcessExecutor，需要注入@Qualifier("imageProcessExecutor") Executor executor
        
        return localFiles.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    long startTime = System.currentTimeMillis();
                    try {
                        ImageProcessResult result = compressAndUploadSingle(file, namespace);
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("【异步处理】图片处理完成 - 文件: {}, 耗时: {}ms, 成功: {}", 
                                file.getName(), duration, result.isSuccess());
                        return result;
                    } catch (Exception e) {
                        long duration = System.currentTimeMillis() - startTime;
                        log.error("【异步处理】图片处理失败 - 文件: {}, 耗时: {}ms", 
                                file.getName(), duration, e);
                        ImageProcessResult errorResult = new ImageProcessResult();
                        errorResult.setSuccess(false);
                        errorResult.setErrorMessage(e.getMessage());
                        return errorResult;
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, ImageProcessResult> compressAndUploadToOssWithRetry(
            List<File> localFiles,
            String namespace) {
        
        log.info("【批量处理】开始批量处理图片（带重试） - 文件数量: {}", localFiles.size());
        Map<String, ImageProcessResult> results = new HashMap<>();
        
        for (File file : localFiles) {
            ImageProcessResult result = compressAndUploadWithRetry(file, namespace);
            results.put(file.getAbsolutePath(), result);
        }
        
        long successCount = results.values().stream().filter(ImageProcessResult::isSuccess).count();
        log.info("【批量处理】批量处理完成 - 总数: {}, 成功: {}, 失败: {}", 
                localFiles.size(), successCount, localFiles.size() - successCount);
        
        return results;
    }

    @Override
    public void saveProductImages(Long productId, ProductImageProcessResult processResult) {
        if (productId == null) {
            log.warn("【保存图片】productId为null，无法保存");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        log.info("【保存图片】开始保存图片记录 - 商品ID: {}, 保存: {}条, 删除: {}条", 
                productId, processResult.getToSave().size(), processResult.getToDelete().size());
        
        try {
            // 1. 删除图片
            if (processResult.getToDelete() != null && !processResult.getToDelete().isEmpty()) {
                for (Long imageId : processResult.getToDelete()) {
                    try {
                        shopProductImagesService.removeById(imageId);
                        log.debug("【保存图片】删除图片 - ID: {}", imageId);
                    } catch (Exception e) {
                        log.error("【保存图片】删除图片失败 - ID: {}", imageId, e);
                    }
                }
            }
            
            // 2. 保存/更新图片
            if (processResult.getToSave() != null && !processResult.getToSave().isEmpty()) {
                for (ShopProductImages image : processResult.getToSave()) {
                    image.setProductId(productId);
                    if (image.getId() != null) {
                        shopProductImagesService.update(image);
                    } else {
                        shopProductImagesService.save(image);
                    }
                }
            }
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("【保存图片】保存完成 - 商品ID: {}, 耗时: {}ms", productId, duration);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("【保存图片】保存失败 - 商品ID: {}, 耗时: {}ms", productId, duration, e);
            throw e;
        }
    }

    // ========== 私有辅助方法 ==========

    /**
     * 检查是否有图片变化
     */
    private boolean hasImageChanges(HttpServletRequest request, Long productId, boolean isCreate) {
        // 1. 检查是否有新文件上传
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            boolean hasFiles = (mreq.getFile(IMAGE_FILE_FIELD_NAME) != null && !mreq.getFile(IMAGE_FILE_FIELD_NAME).isEmpty())
                    || (mreq.getFiles("imageFiles") != null && !mreq.getFiles("imageFiles").isEmpty());
            if (hasFiles) {
                return true;
            }
        }
        
        // 2. 检查是否有删除图片（编辑时）
        if (!isCreate && productId != null) {
            String deletedImageIds = request.getParameter("deletedImageIds");
            if (deletedImageIds != null && !deletedImageIds.trim().isEmpty()) {
                return true;
            }
            
            // 3. 检查是否有主图变化
            String primaryImageId = request.getParameter("primaryImageId");
            String primaryImageUrl = request.getParameter("primaryImageUrl");
            if ((primaryImageId != null && !primaryImageId.trim().isEmpty()) 
                    || (primaryImageUrl != null && !primaryImageUrl.trim().isEmpty())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 压缩并上传单个文件（带重试）
     */
    private ImageProcessResult compressAndUploadWithRetry(File file, String namespace) {
        int retryCount = 0;
        Exception lastException = null;
        
        while (retryCount < MAX_RETRY_TIMES) {
            try {
                if (imageCompressUtil == null) {
                    log.warn("【重试】ImageCompressUtil未注入，跳过处理");
                    ImageProcessResult errorResult = new ImageProcessResult();
                    errorResult.setSuccess(false);
                    errorResult.setErrorMessage("ImageCompressUtil未注入");
                    return errorResult;
                }
                
                ImageProcessResult result = imageCompressUtil.compressAndConvert(file, file.getParent(), namespace);
                
                if (result != null && result.isSuccess()) {
                    if (retryCount > 0) {
                        log.info("【重试】图片处理成功 - 文件: {}, 重试次数: {}", file.getName(), retryCount);
                    }
                    return result;
                } else {
                    throw new RuntimeException("图片压缩失败");
                }
                
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                log.warn("【重试】图片处理失败 - 文件: {}, 重试次数: {}/{}", 
                        file.getName(), retryCount, MAX_RETRY_TIMES, e);
                
                if (retryCount < MAX_RETRY_TIMES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * retryCount);  // 指数退避
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        // 所有重试都失败
        log.error("【重试】图片处理最终失败 - 文件: {}, 重试次数: {}", file.getName(), retryCount, lastException);
        ImageProcessResult errorResult = new ImageProcessResult();
        errorResult.setSuccess(false);
        errorResult.setErrorMessage(lastException != null ? lastException.getMessage() : "处理失败");
        return errorResult;
    }

    /**
     * 压缩并上传单个文件（不带重试，用于异步处理）
     */
    private ImageProcessResult compressAndUploadSingle(File file, String namespace) {
        if (imageCompressUtil == null) {
            log.warn("ImageCompressUtil未注入，跳过处理");
            ImageProcessResult errorResult = new ImageProcessResult();
            errorResult.setSuccess(false);
            errorResult.setErrorMessage("ImageCompressUtil未注入");
            return errorResult;
        }
        
        return imageCompressUtil.compressAndConvert(file, file.getParent(), namespace);
    }

    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".png") || name.endsWith(".gif")
                || name.endsWith(".webp");
    }

    /**
     * 确定最终图片URL：优先OSS WebP，失败降级到本地原图
     */
    private String determineFinalImageUrl(ImageProcessResult compressResult,
                                         String originalUrl, String relative,
                                         String serverRoot, String storageRoot) {
        // 1. 优先使用OSS WebP原图URL
        if (compressResult.getWebpOssUrl() != null) {
            return compressResult.getWebpOssUrl();
        }
        
        // 2. 降级使用OSS缩略图WebP URL
        if (compressResult.getThumbWebpOssUrl() != null) {
            return compressResult.getThumbWebpOssUrl();
        }
        
        // 3. OSS上传失败，降级使用本地原图URL
        return originalUrl;
    }

    /**
     * 从完整URL中提取相对路径
     */
    private String extractRelativePath(String fullUrl, String serverRoot) {
        if (fullUrl == null) {
            return null;
        }
        if (serverRoot != null && fullUrl.startsWith(serverRoot)) {
            return fullUrl.substring(serverRoot.length());
        }
        return fullUrl;
    }
}
