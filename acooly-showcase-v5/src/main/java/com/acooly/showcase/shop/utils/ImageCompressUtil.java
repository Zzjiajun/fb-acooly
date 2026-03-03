package com.acooly.showcase.shop.utils;

import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.service.OssStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Component
public class ImageCompressUtil {

    // 静态初始化块：手动注册 WebP ImageIO 插件
    static {
        try {
            log.info("========== 开始初始化 WebP ImageIO 插件 ==========");
            
            // 方法1：首先尝试通过 SPI 机制自动发现并注册
            try {
                javax.imageio.spi.IIORegistry registry = javax.imageio.spi.IIORegistry.getDefaultInstance();
                registry.registerApplicationClasspathSpis();
                log.debug("已调用 registerApplicationClasspathSpis() 自动发现 SPI");
            } catch (Exception e) {
                log.warn("registerApplicationClasspathSpis() 失败", e);
            }
            
            // 方法2：尝试手动加载并注册（如果知道类名）
            javax.imageio.spi.IIORegistry registry = javax.imageio.spi.IIORegistry.getDefaultInstance();
            boolean readerRegistered = false;
            boolean writerRegistered = false;
            
            // WebP ImageIO 可能的类名（根据实际库调整）
            // 优先使用 sejda webp-imageio，在 Maven 中央仓库，更稳定
            String[] possibleReaderClasses = {
                "org.sejda.imageio.webp.WebPImageReaderSpi",  // sejda webp-imageio (优先)
                "com.luciad.imageio.webp.WebPImageReaderSpi",  // imageio-webp
                "com.github.nintha.webp.imageio.WebPImageReaderSpi"  // webp-imageio-core
            };
            
            String[] possibleWriterClasses = {
                "org.sejda.imageio.webp.WebPImageWriterSpi",  // sejda webp-imageio (优先)
                "com.luciad.imageio.webp.WebPImageWriterSpi",  // imageio-webp
                "com.github.nintha.webp.imageio.WebPImageWriterSpi"  // webp-imageio-core
            };
            
            // 尝试注册 ImageReader
            for (String className : possibleReaderClasses) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    if (instance instanceof javax.imageio.spi.ImageReaderSpi) {
                        registry.registerServiceProvider((javax.imageio.spi.ImageReaderSpi) instance);
                        log.info("✅ 手动注册 WebP ImageReader: {}", className);
                        readerRegistered = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // 继续尝试下一个
                    log.debug("类 {} 未找到，继续尝试", className);
                } catch (Exception e) {
                    log.debug("尝试注册 {} 失败: {}", className, e.getMessage());
                }
            }
            
            // 尝试注册 ImageWriter
            for (String className : possibleWriterClasses) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    if (instance instanceof javax.imageio.spi.ImageWriterSpi) {
                        registry.registerServiceProvider((javax.imageio.spi.ImageWriterSpi) instance);
                        log.info("✅ 手动注册 WebP ImageWriter: {}", className);
                        writerRegistered = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // 继续尝试下一个
                    log.debug("类 {} 未找到，继续尝试", className);
                } catch (Exception e) {
                    log.debug("尝试注册 {} 失败: {}", className, e.getMessage());
                }
            }
            
            // 验证 WebP ImageWriter 是否可用
            java.util.Iterator<javax.imageio.ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (writers.hasNext()) {
                log.info("✅ WebP ImageWriter 已成功注册，可以使用");
                // 列出所有可用的 WebP ImageWriter
                int count = 0;
                while (writers.hasNext()) {
                    javax.imageio.ImageWriter writer = writers.next();
                    log.info("   - WebP ImageWriter #{}: {}", ++count, writer.getClass().getName());
                }
            } else {
                log.error("❌ WebP ImageWriter 未找到！");
                log.error("   请检查以下事项：");
                log.error("   1. pom.xml 中是否添加了 webp-imageio-core 依赖");
                log.error("   2. 是否添加了 jitpack.io 仓库（如果依赖在 jitpack）");
                log.error("   3. Maven 是否已重新编译（mvn clean install）");
                log.error("   4. 应用是否已重启");
                log.error("   5. 检查 META-INF/services/javax.imageio.spi.ImageWriterSpi 文件是否存在");
            }
            
            log.info("========== WebP ImageIO 插件初始化完成 ==========");
        } catch (Exception e) {
            log.error("❌ WebP ImageIO 插件初始化失败", e);
        }
    }

    @Value("${image.compress.enabled:true}")
    private boolean compressEnabled;

    @Value("${image.compress.quality.webp}")
    private int webpQuality;

    @Value("${image.compress.quality.thumbnail:85}")
    private int thumbnailQuality;

    @Value("${image.size.thumbnail.width:250}")
    private int thumbnailWidth;

    @Value("${image.size.thumbnail.height:200}")
    private int thumbnailHeight;

    @Value("${image.upload.thumbnail.enabled:false}")
    private boolean thumbnailEnabled;

    @Value("${image.webp.enabled:true}")
    private boolean webpEnabled;

    @Autowired(required = false)
    private OssStorageService ossStorageService;

    @Value("${oss.enabled:false}")
    private boolean ossEnabled;

    /**
     * 检查压缩和OSS配置（用于诊断）
     */
    public void checkConfiguration() {
        log.info("========== 图片压缩配置检查 ==========");
        log.info("compressEnabled: {}", compressEnabled);
        log.info("webpEnabled: {}", webpEnabled);
        log.info("webpQuality: {}", webpQuality);
        log.info("thumbnailEnabled: {} (简化方案：默认关闭)", thumbnailEnabled);
        log.info("thumbnailQuality: {}", thumbnailQuality);
        log.info("thumbnailSize: {}x{} (width={}, height={})", thumbnailWidth, thumbnailHeight, thumbnailWidth, thumbnailHeight);
        log.info("ossEnabled: {}", ossEnabled);
        log.info("ossStorageService: {}", ossStorageService != null ? "已注入" : "未注入");
        if (ossStorageService != null) {
            log.info("OSS配置完整: {}", ossStorageService.isOssConfigured());
        }
        log.info("=====================================");
    }

    /**
     * 压缩图片并生成WebP版本（支持OSS上传）
     *
     * @param originalFile 原始文件
     * @param outputDir 输出目录（与原图同一目录）
     * @param namespace OSS命名空间（如：shop/products）
     * @return 图片处理结果（包含OSS URL）
     */
    public ImageProcessResult compressAndConvert(File originalFile, String outputDir, String namespace) {
        ImageProcessResult result = new ImageProcessResult();
        result.setOriginalPath(originalFile.getAbsolutePath());
        result.setOriginalSize(originalFile.length());

        if (!compressEnabled) {
            result.setSuccess(true);
            return result;
        }

        try {
            // 1. 读取原图
            BufferedImage originalImage = ImageIO.read(originalFile);
            if (originalImage == null) {
                throw new IOException("无法读取图片文件: " + originalFile.getName());
            }

            // 2. 提取元数据
            result.setOriginalWidth(originalImage.getWidth());
            result.setOriginalHeight(originalImage.getHeight());
            result.setFormat(getImageFormat(originalFile.getName()));

            // 3. 【优化】生成WebP版本（原图尺寸）- 只生成临时文件用于OSS上传，不上传到本地
            File tempWebpFile = null;
            if (webpEnabled) {
                tempWebpFile = generateWebPToTemp(originalFile, originalImage, webpQuality);
                if (tempWebpFile != null && tempWebpFile.exists()) {
                    result.setWebpSize(tempWebpFile.length());
                    // 【重要】从WebP文件中读取实际尺寸，确保是OSS WebP的尺寸，而不是本地原图的尺寸
                    try {
                        BufferedImage webpImage = ImageIO.read(tempWebpFile);
                        if (webpImage != null) {
                            result.setWebpWidth(webpImage.getWidth());   // WebP实际宽度
                            result.setWebpHeight(webpImage.getHeight());  // WebP实际高度
                            log.debug("【WebP尺寸】从WebP文件读取实际尺寸: {}x{} (原图尺寸: {}x{})", 
                                    webpImage.getWidth(), webpImage.getHeight(), 
                                    originalImage.getWidth(), originalImage.getHeight());
                        } else {
                            // 如果无法读取WebP文件，使用原图尺寸（通常WebP保持原图尺寸）
                            log.warn("【WebP尺寸】无法读取WebP文件尺寸，使用原图尺寸: {}x{}", 
                                    originalImage.getWidth(), originalImage.getHeight());
                            result.setWebpWidth(originalImage.getWidth());
                            result.setWebpHeight(originalImage.getHeight());
                        }
                    } catch (Exception e) {
                        // 如果读取失败，使用原图尺寸（通常WebP保持原图尺寸）
                        log.warn("【WebP尺寸】读取WebP文件尺寸失败，使用原图尺寸: {}x{}, 错误: {}", 
                                originalImage.getWidth(), originalImage.getHeight(), e.getMessage());
                        result.setWebpWidth(originalImage.getWidth());
                        result.setWebpHeight(originalImage.getHeight());
                    }
                    // 不设置webpPath，因为不保存到本地
                }
            }

            // 4. 【简化方案】生成缩略图（通过开关控制）
            File tempThumbFile = null;
            if (thumbnailEnabled) {
                // 【修复】计算实际缩略图尺寸（保持原图比例）
                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                double scaleX = (double) thumbnailWidth / originalWidth;
                double scaleY = (double) thumbnailHeight / originalHeight;
                double scale = Math.min(scaleX, scaleY);
                int actualThumbWidth = (int) (originalWidth * scale);
                int actualThumbHeight = (int) (originalHeight * scale);
                // 确保尺寸至少为1像素
                if (actualThumbWidth < 1) actualThumbWidth = 1;
                if (actualThumbHeight < 1) actualThumbHeight = 1;
                
                tempThumbFile = generateThumbnailWebPToTemp(originalImage, originalFile);
                if (tempThumbFile != null && tempThumbFile.exists()) {
                    result.setThumbWebpSize(tempThumbFile.length());
                    // 【修复】保存实际生成的缩略图尺寸，而不是配置的最大尺寸
                    result.setThumbWidth(actualThumbWidth);
                    result.setThumbHeight(actualThumbHeight);
                    log.debug("【缩略图】保存缩略图实际尺寸: {}x{} (配置最大尺寸: {}x{})", 
                            actualThumbWidth, actualThumbHeight, thumbnailWidth, thumbnailHeight);
                    // 不设置thumbWebpPath，因为不保存到本地
                }
            } else {
                // 【简化方案】缩略图功能已关闭，跳过生成
                log.debug("【缩略图】缩略图功能已关闭（image.upload.thumbnail.enabled=false），跳过生成");
            }

            // 5. 【优化】上传到OSS（如果启用）- 上传临时文件后删除
            if (ossEnabled && ossStorageService != null && namespace != null) {
                log.debug("开始上传到OSS - 文件: {}, namespace: {}", originalFile.getName(), namespace);
                uploadToOss(result, tempWebpFile, tempThumbFile, originalFile, namespace);
                
                // 检查OSS上传结果
                if (result.getWebpOssUrl() != null) {
                    log.info("WebP文件已上传到OSS - URL: {}", result.getWebpOssUrl());
                } else {
                    log.warn("WebP文件未上传到OSS - 文件: {}", originalFile.getName());
                }
                
                if (result.getThumbWebpOssUrl() != null) {
                    log.info("缩略图WebP文件已上传到OSS - URL: {}", result.getThumbWebpOssUrl());
                } else {
                    log.warn("缩略图WebP文件未上传到OSS - 文件: {}", originalFile.getName());
                }
            } else {
                log.warn("OSS上传条件不满足 - ossEnabled: {}, ossStorageService: {}, namespace: {}", 
                        ossEnabled, ossStorageService != null, namespace);
            }
            
            // 6. 【优化】清理临时文件
            cleanupTempFiles(tempWebpFile, tempThumbFile);

            result.setSuccess(true);
            log.info("图片处理完成 - 文件: {}, 原图: {}KB, WebP: {}KB, 缩略图: {}KB, OSS WebP: {}, OSS 缩略图: {}",
                    originalFile.getName(),
                    result.getOriginalSize() / 1024,
                    result.getWebpSize() != null ? result.getWebpSize() / 1024 : 0,
                    result.getThumbWebpSize() != null ? result.getThumbWebpSize() / 1024 : 0,
                    result.getWebpOssUrl() != null ? result.getWebpOssUrl() : "未上传",
                    result.getThumbWebpOssUrl() != null ? result.getThumbWebpOssUrl() : "未上传");

        } catch (Exception e) {
            log.error("图片处理失败 - 文件: {}", originalFile.getName(), e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 【优化】生成WebP格式到临时文件（用于OSS上传，不上传到本地）
     */
    private File generateWebPToTemp(File originalFile, BufferedImage image, int quality) {
        try {
            // 创建临时文件
            String baseName = getBaseName(originalFile.getName());
            if (baseName.length()<3){
                baseName="image"+baseName;
            }
            File tempFile = File.createTempFile(baseName + "_", ".webp");
            tempFile.deleteOnExit(); // 程序退出时删除

            // 使用webp-imageio写入WebP
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                log.warn("未找到WebP ImageWriter，请检查webp-imageio依赖");
                return null;
            }

            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile)) {
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();
                
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    
                    // 设置压缩类型
                    try {
                        String[] compressionTypes = param.getCompressionTypes();
                        if (compressionTypes != null && compressionTypes.length > 0) {
                            String compressionType = compressionTypes[0];
                            for (String type : compressionTypes) {
                                if ("lossy".equalsIgnoreCase(type)) {
                                    compressionType = type;
                                    break;
                                }
                            }
                            param.setCompressionType(compressionType);
                            log.debug("设置WebP压缩类型: {}", compressionType);
                        }
                    } catch (Exception e) {
                        log.debug("无法设置压缩类型: {}", e.getMessage());
                    }
                    
                    // 设置压缩质量
                    float qualityFloat = Math.max(0.0f, Math.min(1.0f, quality / 100.0f));
                    try {
                        param.setCompressionQuality(qualityFloat);
                        log.debug("设置WebP压缩质量: {}", qualityFloat);
                    } catch (IllegalStateException e) {
                        log.warn("设置压缩质量失败，使用默认质量: {}", e.getMessage());
                    }
                }

                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }

            return tempFile;
        } catch (Exception e) {
            log.error("生成WebP临时文件失败 - 文件: {}", originalFile.getName(), e);
            return null;
        }
    }

    /**
     * 【保留】生成WebP格式（使用webp-imageio）- 已废弃，保留用于兼容
     */
    @Deprecated
    private String generateWebP(File originalFile, BufferedImage image, String outputDir, int quality) {
        try {
            String baseName = getBaseName(originalFile.getName());
            String webpPath = outputDir + File.separator + baseName + ".webp";
            File webpFile = new File(webpPath);

            // 使用webp-imageio写入WebP
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                log.warn("未找到WebP ImageWriter，请检查webp-imageio依赖");
                return null;
            }

            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(webpFile)) {
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                
                // 处理不同类型的 WebP WriteParam
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    
                    // 对于 com.luciad.imageio.webp.WebPWriteParam，需要先设置压缩类型
                    try {
                        // 尝试获取压缩类型列表
                        String[] compressionTypes = param.getCompressionTypes();
                        if (compressionTypes != null && compressionTypes.length > 0) {
                            // 设置第一个可用的压缩类型（通常是 "lossy" 或 "lossless"）
                            String compressionType = compressionTypes[0];
                            // 优先使用 lossy（有损压缩，文件更小）
                            for (String type : compressionTypes) {
                                if ("lossy".equalsIgnoreCase(type)) {
                                    compressionType = type;
                                    break;
                                }
                            }
                            param.setCompressionType(compressionType);
                            log.debug("设置WebP压缩类型: {}", compressionType);
                        }
                    } catch (Exception e) {
                        log.debug("无法设置压缩类型: {}", e.getMessage());
                    }
                    
                    // 设置压缩质量（0.0-1.0）
                    float qualityFloat = Math.max(0.0f, Math.min(1.0f, quality / 100.0f));
                    try {
                        param.setCompressionQuality(qualityFloat);
                        log.debug("设置WebP压缩质量: {}", qualityFloat);
                    } catch (IllegalStateException e) {
                        // 如果设置质量失败，尝试不设置参数直接写入（使用默认质量）
                        log.warn("设置压缩质量失败，使用默认质量写入: {}", e.getMessage());
                        writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
                        return webpPath;
                    }
                }

                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }

            return webpPath;
        } catch (Exception e) {
            log.error("生成WebP失败 - 文件: {}", originalFile.getName(), e);
            return null;
        }
    }

    /**
     * 【优化】生成缩略图到临时文件（用于OSS上传，不上传到本地）
     * 【清晰度优化】使用更高质量的缩放算法和渲染设置
     */
    private File generateThumbnailWebPToTemp(BufferedImage originalImage, File originalFile) {
        try {
            // 计算缩放比例（保持宽高比）
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            double scaleX = (double) thumbnailWidth / originalWidth;
            double scaleY = (double) thumbnailHeight / originalHeight;
            double scale = Math.min(scaleX, scaleY);

            int thumbWidth = (int) (originalWidth * scale);
            int thumbHeight = (int) (originalHeight * scale);
            
            // 【调试】输出实际使用的缩略图尺寸信息
            log.info("【缩略图生成】配置的最大尺寸: {}x{}, 原图尺寸: {}x{}, 缩放比例: {}, 计算后缩略图尺寸: {}x{}", 
                    thumbnailWidth, thumbnailHeight, originalWidth, originalHeight, String.format("%.4f", scale), thumbWidth, thumbHeight);
            
            // 【清晰度优化】确保缩略图尺寸至少为1像素
            if (thumbWidth < 1) thumbWidth = 1;
            if (thumbHeight < 1) thumbHeight = 1;

            // 【清晰度优化】使用原图的颜色模型和透明度，而不是强制使用RGB
            // 这样可以保持更好的图像质量
            int imageType = originalImage.getType();
            // 如果原图类型不支持，使用ARGB（支持透明度）
            if (imageType == BufferedImage.TYPE_CUSTOM || imageType == 0) {
                imageType = BufferedImage.TYPE_INT_ARGB;
            }
            
            // 【清晰度优化】创建缩略图，使用原图类型或ARGB
            BufferedImage thumbnail = new BufferedImage(thumbWidth, thumbHeight, imageType);
            Graphics2D g = thumbnail.createGraphics();
            
            // 【清晰度优化】使用最高质量的渲染设置
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            
            // 【清晰度优化】使用高质量的双线性插值进行缩放
            // 使用 drawImage 并配合高质量的渲染提示进行缩放
            // 这种方式比 getScaledInstance 更可靠，且质量更好
            g.drawImage(originalImage, 0, 0, thumbWidth, thumbHeight, null);
            
            g.dispose();

            // 创建临时文件
            String baseName = getBaseName(originalFile.getName());
            File tempFile = File.createTempFile(baseName + "_thumb_", ".webp");
            tempFile.deleteOnExit(); // 程序退出时删除

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                log.warn("未找到WebP ImageWriter");
                return null;
            }

            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile)) {
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();
                
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    
                    // 设置压缩类型
                    try {
                        String[] compressionTypes = param.getCompressionTypes();
                        if (compressionTypes != null && compressionTypes.length > 0) {
                            String compressionType = compressionTypes[0];
                            // 【修复】当质量>=100时，优先使用lossless（无损压缩）
                            if (thumbnailQuality >= 100) {
                                // 查找lossless模式
                                boolean foundLossless = false;
                                for (String type : compressionTypes) {
                                    if ("lossless".equalsIgnoreCase(type)) {
                                        compressionType = type;
                                        foundLossless = true;
                                        log.debug("缩略图质量设置为100%，使用lossless（无损）模式");
                                        break;
                                    }
                                }
                                // 如果没有lossless模式，使用lossy模式
                                if (!foundLossless) {
                                    for (String type : compressionTypes) {
                                        if ("lossy".equalsIgnoreCase(type)) {
                                            compressionType = type;
                                            break;
                                        }
                                    }
                                    log.warn("缩略图质量设置为100%，但未找到lossless模式，使用lossy模式");
                                }
                            } else {
                                // 质量<100时，使用lossy模式
                                for (String type : compressionTypes) {
                                    if ("lossy".equalsIgnoreCase(type)) {
                                        compressionType = type;
                                        break;
                                    }
                                }
                            }
                            param.setCompressionType(compressionType);
                            log.debug("设置缩略图WebP压缩类型: {} (质量配置: {}%)", compressionType, thumbnailQuality);
                        }
                    } catch (Exception e) {
                        log.debug("无法设置压缩类型: {}", e.getMessage());
                    }
                    
                    // 【修复】设置压缩质量（根据image.compress.quality.thumbnail参数）
                    // 当 quality>=100 且使用lossless模式时，不需要设置质量参数
                    // 当使用lossy模式时，根据配置的质量值设置
                    if (thumbnailQuality >= 100) {
                        // 检查是否使用了lossless模式
                        String compressionType = param.getCompressionType();
                        if (compressionType != null && "lossless".equalsIgnoreCase(compressionType.toLowerCase())) {
                            // lossless模式不需要设置质量参数
                            log.debug("缩略图使用lossless（无损）模式，无需设置质量参数");
                        } else {
                            // 如果lossless不可用，使用lossy模式的最高质量
                            float qualityFloat = 0.99f;
                            try {
                                param.setCompressionQuality(qualityFloat);
                                log.debug("缩略图质量设置为100%，但使用lossy模式，质量: {}", qualityFloat);
                            } catch (IllegalStateException e) {
                                log.warn("设置压缩质量失败，使用默认质量: {}", e.getMessage());
                            }
                        }
                    } else {
                        // 质量<100时，使用lossy模式，根据配置值设置质量
                        float qualityFloat = Math.max(0.0f, Math.min(1.0f, thumbnailQuality / 100.0f));
                        try {
                            param.setCompressionQuality(qualityFloat);
                            log.debug("设置缩略图WebP压缩质量: {} (原始配置: {}%)", qualityFloat, thumbnailQuality);
                        } catch (IllegalStateException e) {
                            log.warn("设置压缩质量失败，使用默认质量: {}", e.getMessage());
                        }
                    }
                }

                writer.write(null, new javax.imageio.IIOImage(thumbnail, null, null), param);
            } finally {
                writer.dispose();
            }

            return tempFile;
        } catch (Exception e) {
            log.error("生成缩略图临时文件失败 - 文件: {}", originalFile.getName(), e);
            return null;
        }
    }

    /**
     * 【保留】生成缩略图（WebP格式）- 已废弃，保留用于兼容
     */
    @Deprecated
    private String generateThumbnailWebP(BufferedImage originalImage, File originalFile, String outputDir) {
        try {
            // 计算缩放比例（保持宽高比）
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            double scaleX = (double) thumbnailWidth / originalWidth;
            double scaleY = (double) thumbnailHeight / originalHeight;
            double scale = Math.min(scaleX, scaleY);

            int thumbWidth = (int) (originalWidth * scale);
            int thumbHeight = (int) (originalHeight * scale);
            
            // 【清晰度优化】确保缩略图尺寸至少为1像素
            if (thumbWidth < 1) thumbWidth = 1;
            if (thumbHeight < 1) thumbHeight = 1;

            // 【清晰度优化】使用原图的颜色模型和透明度
            int imageType = originalImage.getType();
            if (imageType == BufferedImage.TYPE_CUSTOM || imageType == 0) {
                imageType = BufferedImage.TYPE_INT_ARGB;
            }
            
            // 【清晰度优化】创建缩略图，使用更高质量的渲染设置
            BufferedImage thumbnail = new BufferedImage(thumbWidth, thumbHeight, imageType);
            Graphics2D g = thumbnail.createGraphics();
            // 【清晰度优化】使用最高质量的渲染设置
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g.drawImage(originalImage, 0, 0, thumbWidth, thumbHeight, null);
            g.dispose();

            // 保存为WebP格式
            String baseName = getBaseName(originalFile.getName());
            String thumbPath = outputDir + File.separator + baseName + "_thumb.webp";
            File thumbFile = new File(thumbPath);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
            if (!writers.hasNext()) {
                log.warn("未找到WebP ImageWriter");
                return null;
            }

            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(thumbFile)) {
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                
                // 处理不同类型的 WebP WriteParam
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    
                    // 对于 com.luciad.imageio.webp.WebPWriteParam，需要先设置压缩类型
                    try {
                        // 尝试获取压缩类型列表
                        String[] compressionTypes = param.getCompressionTypes();
                        if (compressionTypes != null && compressionTypes.length > 0) {
                            String compressionType = compressionTypes[0];
                            // 【修复】当质量>=100时，优先使用lossless（无损压缩）
                            if (thumbnailQuality >= 100) {
                                // 查找lossless模式
                                boolean foundLossless = false;
                                for (String type : compressionTypes) {
                                    if ("lossless".equalsIgnoreCase(type)) {
                                        compressionType = type;
                                        foundLossless = true;
                                        log.debug("缩略图质量设置为100%，使用lossless（无损）模式");
                                        break;
                                    }
                                }
                                // 如果没有lossless模式，使用lossy模式
                                if (!foundLossless) {
                                    for (String type : compressionTypes) {
                                        if ("lossy".equalsIgnoreCase(type)) {
                                            compressionType = type;
                                            break;
                                        }
                                    }
                                    log.warn("缩略图质量设置为100%，但未找到lossless模式，使用lossy模式");
                                }
                            } else {
                                // 质量<100时，使用lossy模式
                                for (String type : compressionTypes) {
                                    if ("lossy".equalsIgnoreCase(type)) {
                                        compressionType = type;
                                        break;
                                    }
                                }
                            }
                            param.setCompressionType(compressionType);
                            log.debug("设置缩略图WebP压缩类型: {} (质量配置: {}%)", compressionType, thumbnailQuality);
                        }
                    } catch (Exception e) {
                        log.debug("无法设置压缩类型: {}", e.getMessage());
                    }
                    
                    // 【修复】设置压缩质量（根据image.compress.quality.thumbnail参数）
                    // 当 quality>=100 且使用lossless模式时，不需要设置质量参数
                    // 当使用lossy模式时，根据配置的质量值设置
                    if (thumbnailQuality >= 100) {
                        // 检查是否使用了lossless模式
                        String compressionType = param.getCompressionType();
                        if (compressionType != null && "lossless".equalsIgnoreCase(compressionType.toLowerCase())) {
                            // lossless模式不需要设置质量参数
                            log.debug("缩略图使用lossless（无损）模式，无需设置质量参数");
                        } else {
                            // 如果lossless不可用，使用lossy模式的最高质量
                            float qualityFloat = 0.99f;
                            try {
                                param.setCompressionQuality(qualityFloat);
                                log.debug("缩略图质量设置为100%，但使用lossy模式，质量: {}", qualityFloat);
                            } catch (IllegalStateException e) {
                                // 如果设置质量失败，尝试不设置参数直接写入（使用默认质量）
                                log.warn("设置压缩质量失败，使用默认质量写入: {}", e.getMessage());
                                writer.write((IIOImage) null);
                                return thumbPath;
                            }
                        }
                    } else {
                        // 质量<100时，使用lossy模式，根据配置值设置质量
                        float qualityFloat = Math.max(0.0f, Math.min(1.0f, thumbnailQuality / 100.0f));
                        try {
                            param.setCompressionQuality(qualityFloat);
                            log.debug("设置缩略图WebP压缩质量: {} (原始配置: {}%)", qualityFloat, thumbnailQuality);
                        } catch (IllegalStateException e) {
                            // 如果设置质量失败，尝试不设置参数直接写入（使用默认质量）
                            log.warn("设置压缩质量失败，使用默认质量写入: {}", e.getMessage());
                            writer.write((IIOImage) null);
                            return thumbPath;
                        }
                    }
                }

                writer.write(null, new javax.imageio.IIOImage(thumbnail, null, null), param);
            } finally {
                writer.dispose();
            }

            return thumbPath;
        } catch (Exception e) {
            log.error("生成缩略图失败 - 文件: {}", originalFile.getName(), e);
            return null;
        }
    }

    private String getBaseName(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }

    private String getImageFormat(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return "jpg";
        } else if (lowerName.endsWith(".png")) {
            return "png";
        } else if (lowerName.endsWith(".gif")) {
            return "gif";
        } else if (lowerName.endsWith(".webp")) {
            return "webp";
        }
        return "unknown";
    }

    /**
     * 【优化】上传文件到OSS（使用临时文件）
     */
    private void uploadToOss(ImageProcessResult result, File tempWebpFile, File tempThumbFile, File originalFile, String namespace) {
        try {
            String baseName = getBaseName(originalFile.getName());
            log.debug("开始上传到OSS - 原文件: {}, baseName: {}, namespace: {}", originalFile.getName(), baseName, namespace);

            // 上传WebP原图到OSS
            if (tempWebpFile != null && tempWebpFile.exists()) {
                String ossKey = ossStorageService.generateOssKey(namespace, baseName + ".webp");
                log.debug("准备上传WebP到OSS - 文件: {}, OSS Key: {}", tempWebpFile.getAbsolutePath(), ossKey);
                String ossUrl = ossStorageService.uploadToOss(tempWebpFile, ossKey);
                if (ossUrl != null) {
                    result.setWebpOssUrl(ossUrl);
                    log.info("WebP文件上传OSS成功 - 文件: {}, OSS URL: {}", tempWebpFile.getName(), ossUrl);
                } else {
                    log.warn("WebP文件上传OSS失败，返回null - 文件: {}, OSS Key: {}", tempWebpFile.getName(), ossKey);
                }
            } else {
                log.debug("WebP临时文件不存在，跳过WebP上传到OSS");
            }

            // 上传缩略图WebP到OSS（优先）
            if (tempThumbFile != null && tempThumbFile.exists()) {
                String ossKey = ossStorageService.generateOssKey(namespace, baseName + "_thumb.webp");
                log.debug("准备上传缩略图WebP到OSS - 文件: {}, OSS Key: {}", tempThumbFile.getAbsolutePath(), ossKey);
                String ossUrl = ossStorageService.uploadToOss(tempThumbFile, ossKey);
                if (ossUrl != null) {
                    result.setThumbWebpOssUrl(ossUrl);
                    log.info("缩略图WebP文件上传OSS成功 - 文件: {}, OSS URL: {}", tempThumbFile.getName(), ossUrl);
                } else {
                    log.warn("缩略图WebP文件上传OSS失败，返回null - 文件: {}, OSS Key: {}", tempThumbFile.getName(), ossKey);
                }
            } else {
                log.debug("缩略图WebP临时文件不存在，跳过缩略图上传到OSS");
            }

        } catch (Exception e) {
            log.error("上传OSS失败 - 原文件: {}", originalFile.getName(), e);
        }
    }

    /**
     * 【优化】清理临时文件
     */
    private void cleanupTempFiles(File... tempFiles) {
        for (File tempFile : tempFiles) {
            if (tempFile != null && tempFile.exists()) {
                try {
                    boolean deleted = tempFile.delete();
                    if (deleted) {
                        log.debug("临时文件已删除: {}", tempFile.getAbsolutePath());
                    } else {
                        log.warn("临时文件删除失败: {}", tempFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    log.warn("删除临时文件异常: {}", tempFile.getAbsolutePath(), e);
                }
            }
        }
    }

}
