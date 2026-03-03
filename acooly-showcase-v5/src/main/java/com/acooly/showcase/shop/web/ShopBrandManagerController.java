/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-22
*/
package com.acooly.showcase.shop.web;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.event.EventBus;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.showcase.shop.dto.ImageProcessResult;
import com.acooly.showcase.shop.entity.ShopBrand;
import com.acooly.showcase.shop.service.ShopBrandService;
import com.acooly.showcase.shop.utils.ImageCompressUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 品牌表 管理控制器
 *
 * @author acooly
 * @date 2025-12-22 20:03:41
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopBrand")
public class ShopBrandManagerController extends AbstractJsonEntityController<ShopBrand, ShopBrandService> {

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopBrandService shopBrandService;
	@Autowired
	private RedisShopUtil redisUtil;
	@Autowired
	private EventBus eventBus;

	@Resource
	private OFileProperties oFileProperties; // 用于获取 storageRoot 和 serverRoot（访问域名前缀）

	@Autowired(required = false)
	private ImageCompressUtil imageCompressUtil; // 图片压缩工具

	// ========== 品牌Logo上传配置常量 ==========
	private static final String STORAGE_NAMESPACE = "shop/brand";
	private static final String ALLOWED_EXTENSIONS = "jpg,png,jpeg,gif,webp";
	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
	private static final String LOGO_FILE_FIELD_NAME = "logoFile"; // 文件上传字段名
	private static final String LOGO_URL_FIELD_NAME = "logo"; // 实体属性名/隐藏字段名

	private static final String PRODUCT_BRAND_PREFIX = "product:brand:";

	/**
	 * 保存前的处理：处理Logo图片上传
	 * 参考商品的多图片上传流程，简化为单图片上传
	 */
	@Override
	protected ShopBrand onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopBrand entity, boolean isCreate) throws Exception {
		log.debug("开始处理品牌保存 - {}, id={}", isCreate ? "新增" : "更新", entity == null ? "null" : entity.getId());

		// 1. 配置上传设置
		configureUploadSettings();

		// 2. 保存原始的logo（防止doUpload自动修改）
		String originalLogo = entity != null ? entity.getLogo() : null;
		if (!isCreate && entity != null && entity.getId() != null) {
			// 编辑时，从数据库获取最新的logo（这是真正的原始值）
			ShopBrand exist = getEntityService().get(entity.getId());
			if (exist != null) {
				originalLogo = exist.getLogo();
				log.debug("编辑品牌，从数据库获取原始logo - 品牌ID: {}, logo: {}", entity.getId(), originalLogo);
			}
		}
		log.debug("保存原始logo - originalLogo: {}", originalLogo);

		// 3. 处理Logo图片上传和压缩
		Map<String, String> uploadedFileUrls = new HashMap<>();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
			// 检查是否有文件上传
			MultipartFile logoFile = mreq.getFile(LOGO_FILE_FIELD_NAME);
			boolean hasFile = logoFile != null && !logoFile.isEmpty();

			if (hasFile) {
				try {
					// 上传文件到本地
					Map<String, UploadResult> uploadResultMap = doUpload(request);
					if (uploadResultMap != null && !uploadResultMap.isEmpty()) {
						String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
								? oFileProperties.getServerRoot().replaceAll("/+$", "") : "";
						String storageRoot = oFileProperties != null && oFileProperties.getStorageRoot() != null
								? oFileProperties.getStorageRoot() : "";

						// 提取上传的文件URL并处理压缩
						for (Map.Entry<String, UploadResult> entry : uploadResultMap.entrySet()) {
							UploadResult ur = entry.getValue();
							if (ur != null && ur.getRelativeFile() != null) {
								String relative = ur.getRelativeFile();
								String originalFullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;
								
								// 压缩并转换为WebP（保存到本地，不上传OSS）
								String finalUrl = processLogoImage(relative, originalFullUrl, storageRoot, serverRoot);
								uploadedFileUrls.put(entry.getKey(), finalUrl);
								log.debug("Logo文件上传成功 - 字段: {}, 原始URL: {}, 最终URL: {}", 
										entry.getKey(), originalFullUrl, finalUrl);
							}
						}
					}
				} catch (Exception e) {
					log.error("Logo文件上传失败", e);
					throw new BusinessException("UPLOAD_ERROR", "Logo文件上传失败：" + e.getMessage(), "");
				}
			}
		}

		// 4. 处理 logo 字段
		String uploadedLogoUrl = uploadedFileUrls.get(LOGO_FILE_FIELD_NAME);
		if (uploadedLogoUrl != null) {
			// 上传了新文件，使用新URL（可能是WebP格式）
			entity.setLogo(uploadedLogoUrl);
			log.debug("Logo图片上传成功，URL: {}", uploadedLogoUrl);
		} else {
			// 没有上传新文件
			if (isCreate) {
				// 新增时：如果没有上传文件，设置为null
				entity.setLogo(null);
				log.debug("新增品牌，未上传Logo图片，设置为null");
			} else {
				// 修改时：如果没有上传新文件，判断是否被清空
				String currentLogo = entity.getLogo();
				if (currentLogo == null || currentLogo.trim().isEmpty()) {
					// 隐藏字段被清空（用户点击了清除按钮），清空数据库中的URL
					entity.setLogo(null);
					log.debug("修改品牌，Logo图片被清空，设置为null");
				} else {
					// 隐藏字段有值（保持原有值），使用原有值
					entity.setLogo(originalLogo);
					log.debug("修改品牌，未上传新Logo图片，保持原有值: {}", originalLogo);
				}
			}
		}

		// 5. 调用父类方法保存实体
		return super.onSave(request, response, model, entity, isCreate);
	}

	@Override
	public JsonEntityResult<ShopBrand> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopBrand> result = super.saveJson(request, response);
		ShopBrand entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setBrandId(entity.getId());
			event.setAction("Brand");
			eventBus.publish(event);
		}
		return result;
	}

	@Override
	public JsonEntityResult<ShopBrand> updateJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<ShopBrand> result = super.updateJson(request, response);
		ShopBrand entity = result.getEntity();
		if (entity != null && entity.getId() != null) {
			CacheShopRedisEvent event = new CacheShopRedisEvent();
			event.setBrandId(entity.getId());
			event.setAction("Brand");
			eventBus.publish(event);
		}
		return result;
	}

	@Override
	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		// 在删除前发布缓存清理事件
		for (Serializable id : ids) {
			if (id instanceof Long) {
				ShopBrand brand = this.getEntityService().get(id);
				if (brand != null && brand.getId() != null) {
					CacheShopRedisEvent event = new CacheShopRedisEvent();
					event.setBrandId(brand.getId());
					event.setAction("Brand");
					eventBus.publish(event);
				}
			}
		}
		// 调用父类删除品牌
		super.onRemove(request, response, model, ids);
	}





	/**
	 * 配置上传参数（确保设置 storageRoot）
	 */
	private void configureUploadSettings() {
		try {
			UploadConfig uploadConfig = getUploadConfig();
			uploadConfig.setStorageNameSpace(STORAGE_NAMESPACE);
			uploadConfig.setAllowExtentions(ALLOWED_EXTENSIONS);
			uploadConfig.setMaxSize(MAX_FILE_SIZE);
			uploadConfig.setUseMemery(false);
			uploadConfig.setNeedRemaneToTimestamp(false);
			uploadConfig.setNeedTimePartPath(true);
			uploadConfig.setThumbnailEnable(false);
			
			// **关键**：设置 storageRoot // 实际文件目录 为 ofile 配置中的 storageRoot （本地存储时必须）
			if (oFileProperties != null && oFileProperties.getStorageRoot() != null) {
				uploadConfig.setStorageRoot(oFileProperties.getStorageRoot());
			}

		} catch (Exception e) {
			log.error("配置上传设置失败", e);
			throw new BusinessException("UPLOAD_CONFIG_ERROR", "上传配置失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 处理Logo图片：压缩并转换为WebP格式，保存到本地（不上传OSS）
	 * 
	 * @param relative 相对路径
	 * @param originalFullUrl 原始完整URL
	 * @param storageRoot 存储根路径
	 * @param serverRoot 服务器根路径
	 * @return 最终图片URL（WebP格式，如果压缩成功）
	 */
	private String processLogoImage(String relative, String originalFullUrl, String storageRoot, String serverRoot) {
		if (imageCompressUtil == null) {
			log.warn("ImageCompressUtil未注入，跳过图片压缩处理，使用原图");
			return originalFullUrl;
		}

		try {
			// 获取本地文件的完整路径
			String fullPath = storageRoot + File.separator + relative.replace("/", File.separator);
			File localFile = new File(fullPath);
			
			if (!localFile.exists() || !localFile.isFile()) {
				log.warn("本地文件不存在或不是文件，跳过压缩处理 - 文件: {}", fullPath);
				return originalFullUrl;
			}

			// 判断是否为图片文件
			if (!isImageFile(localFile)) {
				log.debug("非图片文件，跳过压缩处理 - 文件: {}", localFile.getName());
				return originalFullUrl;
			}

			log.info("开始Logo图片压缩处理 - 文件: {}, 大小: {}KB, 路径: {}", 
					localFile.getName(), localFile.length() / 1024, localFile.getAbsolutePath());

			// 调用压缩工具（传入null作为namespace，不上传OSS）
			ImageProcessResult compressResult = imageCompressUtil
					.compressAndConvert(localFile, localFile.getParent(), null);

			if (compressResult == null || !compressResult.isSuccess()) {
				log.warn("Logo图片压缩失败，使用原图 - 文件: {}", localFile.getName());
				return originalFullUrl;
			}

			// 由于compressAndConvert生成的是临时文件（用于OSS上传），我们需要手动保存WebP到本地
			// 获取临时WebP文件（如果存在）
			// 注意：compressAndConvert会清理临时文件，所以我们需要在清理前保存
			// 但更好的方法是直接生成WebP到本地目录
			
			// 方案：直接生成WebP到本地目录（与原图同一目录）
			String webpPath = generateWebPToLocal(localFile);
			if (webpPath != null) {
				// 基于原始的 relative 路径生成 WebP 的相对路径（更可靠）
				// relative 格式：/shop/brand/2025/12/30/xxx.jpg
				// 生成：/shop/brand/2025/12/30/xxx.webp
				String baseName = getBaseName(relative.substring(relative.lastIndexOf('/') + 1));
				String relativeDir = relative.substring(0, relative.lastIndexOf('/') + 1);
				String webpRelative = relativeDir + baseName + ".webp";
				
				// 生成完整URL
				String webpFullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + webpRelative;
				
				log.info("Logo图片压缩完成 - 原图: {}KB, WebP: {}KB, WebP相对路径: {}, WebP URL: {}", 
						compressResult.getOriginalSize() / 1024,
						compressResult.getWebpSize() != null ? compressResult.getWebpSize() / 1024 : 0,
						webpRelative, webpFullUrl);
				
				return webpFullUrl;
			} else {
				log.warn("生成WebP文件失败，使用原图 - 文件: {}", localFile.getName());
				return originalFullUrl;
			}

		} catch (Exception e) {
			log.error("处理Logo图片异常 - 文件: {}", relative, e);
			return originalFullUrl;
		}
	}

	/**
	 * 生成WebP文件到本地目录（与原图同一目录）
	 */
	private String generateWebPToLocal(File originalFile) {
		if (imageCompressUtil == null) {
			return null;
		}

		try {
			// 使用反射调用私有方法generateWebP（已废弃但保留用于本地保存）
			// 或者直接使用ImageIO生成WebP
			java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(originalFile);
			if (image == null) {
				log.warn("无法读取图片: {}", originalFile.getName());
				return null;
			}

			// 生成WebP文件路径（与原图同一目录）
			String baseName = getBaseName(originalFile.getName());
			String webpPath = originalFile.getParent() + File.separator + baseName + ".webp";
			File webpFile = new File(webpPath);

			// 使用ImageIO写入WebP
			java.util.Iterator<javax.imageio.ImageWriter> writers = javax.imageio.ImageIO.getImageWritersByFormatName("webp");
			if (!writers.hasNext()) {
				log.warn("未找到WebP ImageWriter，请检查webp-imageio依赖");
				return null;
			}

			javax.imageio.ImageWriter writer = writers.next();
			try (javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(webpFile)) {
				writer.setOutput(ios);
				javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
				
				if (param.canWriteCompressed()) {
					param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
					
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
						}
					} catch (Exception e) {
						log.debug("无法设置压缩类型: {}", e.getMessage());
					}
					
					// 设置压缩质量（80%）
					float quality = 0.8f;
					try {
						param.setCompressionQuality(quality);
					} catch (IllegalStateException e) {
						log.warn("设置压缩质量失败，使用默认质量: {}", e.getMessage());
					}
				}

				writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
			} finally {
				writer.dispose();
			}

			log.info("WebP文件生成成功 - 路径: {}", webpPath);
			return webpPath;

		} catch (Exception e) {
			log.error("生成WebP文件失败 - 文件: {}", originalFile.getName(), e);
			return null;
		}
	}

	/**
	 * 获取文件名（不含扩展名）
	 */
	private String getBaseName(String fileName) {
		if (fileName == null) {
			return "image";
		}
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot > 0) {
			return fileName.substring(0, lastDot);
		}
		return fileName;
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
}
