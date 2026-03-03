package com.acooly.showcase.shop.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class OssStorageService {

    @Value("${oss.enabled:false}")
    private boolean ossEnabled;

    // 阿里云OSS配置
    @Value("${oss.aliyun.endpoint:}")
    private String aliyunEndpoint;

    @Value("${oss.aliyun.accessKeyId:}")
    private String aliyunAccessKeyId;

    @Value("${oss.aliyun.accessKeySecret:}")
    private String aliyunAccessKeySecret;

    @Value("${oss.aliyun.bucketName:}")
    private String aliyunBucketName;

    // CDN域名配置（使用主域名或子域名）
    @Value("${oss.cdn.domain:https://ltbotstk.com}")
    private String cdnDomain;

    /**
     * 检查OSS配置是否完整（用于诊断）
     */
    public boolean isOssConfigured() {
        boolean configured = ossEnabled 
                && aliyunEndpoint != null && !aliyunEndpoint.isEmpty()
                && aliyunAccessKeyId != null && !aliyunAccessKeyId.isEmpty()
                && aliyunAccessKeySecret != null && !aliyunAccessKeySecret.isEmpty()
                && aliyunBucketName != null && !aliyunBucketName.isEmpty();
        
        if (!configured) {
            log.warn("OSS配置不完整 - ossEnabled: {}, endpoint: {}, accessKeyId: {}, accessKeySecret: {}, bucketName: {}", 
                    ossEnabled,
                    aliyunEndpoint != null && !aliyunEndpoint.isEmpty(),
                    aliyunAccessKeyId != null && !aliyunAccessKeyId.isEmpty(),
                    aliyunAccessKeySecret != null && !aliyunAccessKeySecret.isEmpty(),
                    aliyunBucketName != null && !aliyunBucketName.isEmpty());
        }
        
        return configured;
    }

    /**
     * 上传文件到OSS
     *
     * @param localFile 本地文件
     * @param ossKey OSS对象键（路径）
     * @return OSS访问URL（通过CDN域名）
     */
    public String uploadToOss(File localFile, String ossKey) {
        if (!ossEnabled) {
            log.warn("OSS未启用，跳过上传 - 文件: {}, OSS Key: {}", localFile.getName(), ossKey);
            return null;
        }

        // 检查OSS配置
        if (aliyunEndpoint == null || aliyunEndpoint.isEmpty()) {
            log.error("OSS配置错误：endpoint为空");
            return null;
        }
        if (aliyunAccessKeyId == null || aliyunAccessKeyId.isEmpty()) {
            log.error("OSS配置错误：accessKeyId为空");
            return null;
        }
        if (aliyunAccessKeySecret == null || aliyunAccessKeySecret.isEmpty()) {
            log.error("OSS配置错误：accessKeySecret为空");
            return null;
        }
        if (aliyunBucketName == null || aliyunBucketName.isEmpty()) {
            log.error("OSS配置错误：bucketName为空");
            return null;
        }

        // 检查文件
        if (localFile == null || !localFile.exists()) {
            log.error("文件不存在 - 文件: {}", localFile != null ? localFile.getAbsolutePath() : "null");
            return null;
        }
        if (!localFile.isFile()) {
            log.error("不是文件 - 路径: {}", localFile.getAbsolutePath());
            return null;
        }

        log.debug("开始上传到OSS - 文件: {}, 大小: {}KB, OSS Key: {}", 
                localFile.getName(), localFile.length() / 1024, ossKey);

        try {
            String ossUrl = uploadToAliyunOss(localFile, ossKey);
            if (ossUrl != null) {
                log.debug("文件上传OSS成功 - 文件: {}", localFile.getName());
            } else {
                log.warn("文件上传OSS返回null - 文件: {}, OSS Key: {}", localFile.getName(), ossKey);
            }
            return ossUrl;
        } catch (Exception e) {
            log.error("上传文件到OSS失败 - 文件: {}, OSS Key: {}", localFile.getName(), ossKey, e);
            return null;
        }
    }

    /**
     * 上传到阿里云OSS
     */
    private String uploadToAliyunOss(File localFile, String ossKey) throws FileNotFoundException {
        log.debug("创建OSS客户端 - endpoint: {}, bucket: {}", aliyunEndpoint, aliyunBucketName);
        OSS ossClient = new OSSClientBuilder().build(aliyunEndpoint, aliyunAccessKeyId, aliyunAccessKeySecret);

        try {
            log.debug("准备上传文件 - 文件: {}, 大小: {}KB, OSS Key: {}", 
                    localFile.getAbsolutePath(), localFile.length() / 1024, ossKey);
            
            try (InputStream inputStream = new FileInputStream(localFile)) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(aliyunBucketName, ossKey, inputStream);
                ossClient.putObject(putObjectRequest);
                log.debug("OSS putObject调用成功 - OSS Key: {}", ossKey);
            } catch (IOException e) {
                log.error("读取文件流失败 - 文件: {}, OSS Key: {}", localFile.getName(), ossKey, e);
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("OSS上传异常 - 文件: {}, OSS Key: {}", localFile.getName(), ossKey, e);
                throw e;
            }

            // 返回CDN域名URL
            String ossUrl = cdnDomain + "/" + ossKey;
            log.debug("OSS上传完成 - 文件: {}", localFile.getName());
            return ossUrl;
        } catch (Exception e) {
            log.error("上传到阿里云OSS失败 - 文件: {}, OSS Key: {}", localFile.getName(), ossKey, e);
            throw e;
        } finally {
            ossClient.shutdown();
            log.debug("OSS客户端已关闭");
        }
    }

    /**
     * 删除OSS文件
     */
    public void deleteFromOss(String ossKey) {
        if (!ossEnabled) {
            return;
        }

        try {
            OSS ossClient = new OSSClientBuilder().build(aliyunEndpoint, aliyunAccessKeyId, aliyunAccessKeySecret);
            try {
                ossClient.deleteObject(aliyunBucketName, ossKey);
                log.info("删除OSS文件成功 - OSS Key: {}", ossKey);
            } finally {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            log.error("删除OSS文件失败 - OSS Key: {}", ossKey, e);
        }
    }

    /**
     * 生成OSS对象键（路径）
     *
     * @param namespace 命名空间（如：shop/products）
     * @param fileName 文件名
     * @return OSS对象键
     */
    public String generateOssKey(String namespace, String fileName) {
        // 生成时间路径：shop/products/2025/01/15/filename.webp
        // 如果使用主域名且配置了路径前缀，可以在这里添加：
        // return "cdn/" + namespace + "/" + datePath + "/" + fileName;
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePath = String.format("%d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return namespace + "/" + datePath + "/" + fileName;
    }

    /**
     * 生成OSS对象键（带路径前缀，用于主域名方案）
     *
     * @param pathPrefix 路径前缀（如：cdn 或 media）
     * @param namespace 命名空间（如：shop/products）
     * @param fileName 文件名
     * @return OSS对象键
     */
    public String generateOssKeyWithPrefix(String pathPrefix, String namespace, String fileName) {
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePath = String.format("%d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        return pathPrefix + "/" + namespace + "/" + datePath + "/" + fileName;
    }
}
