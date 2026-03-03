package com.acooly.showcase.shop.service;

import com.acooly.showcase.shop.dto.ExistingImageInfo;
import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.dto.UploadedImageInfo;
import com.acooly.showcase.shop.entity.ShopProductImages;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 商品图片处理服务
 * 负责：图片上传、压缩、OSS上传、批量处理、异步处理、错误重试、监控日志
 *
 * @author acooly
 * @date 2025-12-18
 */
public interface ProductImageService {

    /**
     * 处理商品图片上传（统一入口，支持批量）
     * 
     * @param request HTTP请求
     * @param productId 商品ID（新增时为null）
     * @param isCreate 是否新增
     * @return 图片处理结果
     */
    ProductImageProcessResult processProductImageUpload(
            HttpServletRequest request,
            Long productId,
            boolean isCreate);

    /**
     * 异步处理图片压缩和OSS上传（批量）
     * 
     * @param localFiles 本地文件列表
     * @param namespace OSS命名空间
     * @return CompletableFuture列表，每个元素对应一个文件的处理结果
     */
    List<CompletableFuture<ImageProcessResult>> compressAndUploadToOssAsync(
            List<File> localFiles,
            String namespace);

    /**
     * 同步处理图片压缩和OSS上传（批量，带重试）
     * 
     * @param localFiles 本地文件列表
     * @param namespace OSS命名空间
     * @return 处理结果Map，key为文件路径，value为处理结果
     */
    Map<String, ImageProcessResult> compressAndUploadToOssWithRetry(
            List<File> localFiles,
            String namespace);

    /**
     * 保存图片记录到数据库
     * 
     * @param productId 商品ID
     * @param processResult 图片处理结果
     */
    void saveProductImages(Long productId, ProductImageProcessResult processResult);

    /**
     * 处理上传后的文件：压缩、转换、上传OSS（批量，带重试）
     * 这个方法在Controller中调用doUpload后使用
     * 
     * @param uploadedFileUrls Map，key为参数名，value为上传后的相对路径（如 /shop/products/2025/12/18/image.jpg）
     * @param request HTTP请求
     * @return 压缩结果Map，key为图片URL，value为处理结果
     */
    Map<String, ImageProcessResult> processUploadedFiles(
            Map<String, String> uploadedFileUrls,
            HttpServletRequest request);

    /**
     * 处理图片逻辑：合并新上传的图片和已存在的图片
     * 
     * @param productId 商品ID
     * @param uploadedImages 上传的图片列表
     * @param existingImages 已存在的图片列表
     * @param compressResults 压缩结果Map
     * @param request HTTP请求
     * @return 图片处理结果
     */
    ProductImageProcessResult processProductImages(
            Long productId,
            List<UploadedImageInfo> uploadedImages,
            List<ExistingImageInfo> existingImages,
            Map<String, ImageProcessResult> compressResults,
            HttpServletRequest request);

    /**
     * 图片处理结果（内部类）
     */
    @Data
    class ProductImageProcessResult {
        private List<ShopProductImages> toSave;      // 需要保存的图片记录
        private List<Long> toDelete;                  // 需要删除的图片ID
        private String primaryImageUrl;               // 主图URL
        private boolean primaryImageChanged;          // 主图是否变化
        private Map<String, ImageProcessResult> compressResults; // 压缩结果Map（key为图片URL）
        
        public ProductImageProcessResult() {
            this.toSave = new java.util.ArrayList<>();
            this.toDelete = new java.util.ArrayList<>();
            this.compressResults = new java.util.HashMap<>();
        }
    }
}

