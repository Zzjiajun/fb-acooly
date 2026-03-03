/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-28
 */
package com.acooly.showcase.shop.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.ofile.OFileProperties;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.acooly.core.common.web.MappingMethod;
import com.google.common.collect.Maps;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.showcase.shop.dto.HeadDisplayDTO;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopReviews;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.service.ShopReviewsService;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopConfig;
import com.acooly.showcase.shop.service.ShopConfigService;

/**
 * shop_config 管理控制器
 *
 * @author acooly
 * @date 2025-11-28 23:22:47
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopConfig")
public class ShopConfigManagerController extends AbstractJsonEntityController<ShopConfig, ShopConfigService> {

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopConfigService shopConfigService;

	@Autowired
	private ShopProductsService shopProductsService;

	@Autowired
	private ShopReviewsService shopReviewsService;

	@Autowired
	private RedisShopUtil redisUtil;

	@Resource
	private OFileProperties oFileProperties; // 用于获取 storageRoot 和 serverRoot

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// ========== 图片上传配置常量 ==========
	private static final String STORAGE_NAMESPACE = "shop/config";
	private static final String ALLOWED_EXTENSIONS = "jpg,png,jpeg,gif,webp";
	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
	private static final String LOGO_FILE_FIELD_NAME = "logoFile";
	private static final String PRODUCT_DESCRIPTION_FILE_FIELD_NAME = "productDescriptionFile";

	/**
	 * 重写edit方法，在返回给前端前解码headDisplay
	 */
	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.allow(request, response, MappingMethod.update);

		try {
			model.addAllAttributes(this.referenceData(request));
			ShopConfig entity = this.loadEntity(request);

		// 解码headDisplay（如果是Base64编码的）
		if (entity != null && entity.getHeadDisplay() != null) {
			String headDisplay = entity.getHeadDisplay();
			try {
				// 尝试Base64解码
				byte[] decoded = Base64.getDecoder().decode(headDisplay);
				String decodedText = new String(decoded, "UTF-8");
				entity.setHeadDisplay(decodedText);
				log.debug("headDisplay Base64解码成功");
			} catch (Exception e) {
				// Base64解码失败，说明不是Base64编码，保持原值
				log.debug("headDisplay不是Base64格式，保持原值");
			}
		}

		// 【排查问题】解码productDescription（如果包含HTML实体编码）
		if (entity != null && entity.getProductDescription() != null) {
			String originalDesc = entity.getProductDescription();
			log.info("【排查】编辑页面加载 - productDescription原始值（长度: {}）: {}", 
					originalDesc.length(), originalDesc.length() > 100 ? originalDesc.substring(0, 100) + "..." : originalDesc);
			try {
				// 尝试HTML解码（防止数据库中存储了HTML编码的值）
				String decodedDesc = StringEscapeUtils.unescapeHtml4(originalDesc);
				if (!decodedDesc.equals(originalDesc)) {
					log.info("【排查】productDescription HTML解码 - 原始长度: {}, 解码后长度: {}", 
							originalDesc.length(), decodedDesc.length());
					entity.setProductDescription(decodedDesc);
				} else {
					log.debug("【排查】productDescription无需HTML解码");
				}
			} catch (Exception e) {
				log.warn("【排查】productDescription HTML解码失败，保持原值", e);
			}
		}

		model.addAttribute("action", "edit");
		model.addAttribute(this.getEntityName(), entity);
		this.onEdit(request, response, model, entity);
		} catch (Exception e) {
			log.warn("编辑配置失败", e);
			this.handleException("编辑", e, request);
		}

		return this.getEditView();
	}

	/**
	 * 重写list方法，查询唯一配置并解析数据
	 */
	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.allow(request, response, MappingMethod.list);

		try {
			// 查询唯一配置（ShopConfig表只有一条数据）
			List<ShopConfig> allConfigs = shopConfigService.getAll();
			ShopConfig config = allConfigs.isEmpty() ? null : allConfigs.get(0);

			if (config != null) {
				// 解析ID数组并查询商品/评论详情
				List<ShopProducts> carouselProducts = parseProductIds(config.getCarouselProducts());
				List<ShopProducts> displayProducts = parseProductIds(config.getDisplayProducts());
				List<ShopReviews> showComments = parseReviewIds(config.getShowComments());

				// 解析headDisplay JSON
				HeadDisplayDTO headDisplay = parseHeadDisplay(config.getHeadDisplay());

				model.addAttribute("config", config);
				model.addAttribute("carouselProducts", carouselProducts);
				model.addAttribute("displayProducts", displayProducts);
				model.addAttribute("showComments", showComments);
				model.addAttribute("headDisplay", headDisplay);
			} else {
				// 没有配置时，设置空列表
				model.addAttribute("config", null);
				model.addAttribute("carouselProducts", Collections.emptyList());
				model.addAttribute("displayProducts", Collections.emptyList());
				model.addAttribute("showComments", Collections.emptyList());
				model.addAttribute("headDisplay", null);
			}
		} catch (Exception e) {
			log.error("查询配置失败", e);
			model.addAttribute("config", null);
			model.addAttribute("carouselProducts", Collections.emptyList());
			model.addAttribute("displayProducts", Collections.emptyList());
			model.addAttribute("showComments", Collections.emptyList());
			model.addAttribute("headDisplay", null);
		}

		return "/manage/shop/shopConfig";
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		try {
			// 查询唯一配置（ShopConfig表只有一条数据）
			List<ShopConfig> allConfigs = shopConfigService.getAll();
			ShopConfig config = allConfigs.isEmpty() ? null : allConfigs.get(0);

			if (config != null) {
				// 解析ID数组并查询商品/评论详情
				List<ShopProducts> carouselProducts = parseProductIds(config.getCarouselProducts());
				List<ShopProducts> displayProducts = parseProductIds(config.getDisplayProducts());
				List<ShopReviews> showComments = parseReviewIds(config.getShowComments());

				// 解析headDisplay JSON
				HeadDisplayDTO headDisplay = parseHeadDisplay(config.getHeadDisplay());

				model.put("config", config);
				model.put("carouselProducts", carouselProducts);
				model.put("displayProducts", displayProducts);
				model.put("showComments", showComments);
				model.put("headDisplay", headDisplay);
			} else {
				// 没有配置时，设置空列表
				model.put("config", null);
				model.put("carouselProducts", Collections.emptyList());
				model.put("displayProducts", Collections.emptyList());
				model.put("showComments", Collections.emptyList());
				model.put("headDisplay", null);
			}
		} catch (Exception e) {
			log.error("查询配置失败", e);
			model.put("config", null);
			model.put("carouselProducts", Collections.emptyList());
			model.put("displayProducts", Collections.emptyList());
			model.put("showComments", Collections.emptyList());
			model.put("headDisplay", null);
		}
		super.referenceData(request, model);
	}

	/**
	 * 商品查询接口（供选择器使用）
	 */
	@RequestMapping("/productsJson.html")
	@ResponseBody
	public JsonListResult<ShopProducts> productsJson(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String ids,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows) {

		JsonListResult<ShopProducts> result = new JsonListResult<>();
		this.allow(request, response, MappingMethod.list);

		try {
			Map<String, Object> searchParams = Maps.newHashMap();

			// 如果传了ids参数，按ID列表查询
			if (ids != null && !ids.trim().isEmpty()) {
				String[] idArray = ids.split(",");
				List<Long> idList = new ArrayList<>();
				for (String idStr : idArray) {
					try {
						idList.add(Long.parseLong(idStr.trim()));
					} catch (NumberFormatException e) {
						log.warn("无效的商品ID: {}", idStr);
					}
				}
				if (!idList.isEmpty()) {
					searchParams.put("IN_id", idList);
				}
			} else if (keyword != null && !keyword.trim().isEmpty()) {
				// 按关键词搜索
				searchParams.put("LIKE_name", keyword);
			}

			// 构建分页信息
			PageInfo<ShopProducts> pageInfo = new PageInfo<>();
			pageInfo.setCurrentPage(page);
			pageInfo.setCountOfCurrentPage(rows);

			// 查询商品列表（使用框架标准方法）
			PageInfo<ShopProducts> queryResult = shopProductsService.query(pageInfo, searchParams, null);

			result.setRows(queryResult.getPageResults());
			result.setTotal(queryResult.getTotalCount());
			result.setHasNext(queryResult.hasNext());
			result.setPageNo(queryResult.getCurrentPage());
			result.setPageSize(queryResult.getCountOfCurrentPage());
		} catch (Exception e) {
			log.error("查询商品失败", e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 评论查询接口（供选择器使用）
	 */
	@RequestMapping("/reviewsJson.html")
	@ResponseBody
	public JsonListResult<ShopReviews> reviewsJson(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String ids,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows) {

		JsonListResult<ShopReviews> result = new JsonListResult<>();
		this.allow(request, response, MappingMethod.list);

		try {
			Map<String, Object> searchParams = Maps.newHashMap();

			// 如果传了ids参数，按ID列表查询
			if (ids != null && !ids.trim().isEmpty()) {
				String[] idArray = ids.split(",");
				List<Long> idList = new ArrayList<>();
				for (String idStr : idArray) {
					try {
						idList.add(Long.parseLong(idStr.trim()));
					} catch (NumberFormatException e) {
						log.warn("无效的评论ID: {}", idStr);
					}
				}
				if (!idList.isEmpty()) {
					searchParams.put("IN_id", idList);
				}
			} else if (keyword != null && !keyword.trim().isEmpty()) {
				// 按关键词搜索
				searchParams.put("LIKE_comment", keyword);
			}

			// 构建分页信息
			PageInfo<ShopReviews> pageInfo = new PageInfo<>();
			pageInfo.setCurrentPage(page);
			pageInfo.setCountOfCurrentPage(rows);

			// 查询评论列表（使用框架标准方法）
			PageInfo<ShopReviews> queryResult = shopReviewsService.query(pageInfo, searchParams, null);

			result.setRows(queryResult.getPageResults());
			result.setTotal(queryResult.getTotalCount());
			result.setHasNext(queryResult.hasNext());
			result.setPageNo(queryResult.getCurrentPage());
			result.setPageSize(queryResult.getCountOfCurrentPage());
		} catch (Exception e) {
			log.error("查询评论失败", e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 保存时清除缓存
	 * 对headDisplay进行Base64编码，避免MyBatis日志拦截器的正则表达式问题
	 * 处理图片上传（logoUrl 和 productDescriptionUrl）
	 */
	@Override
	protected ShopConfig onSave(HttpServletRequest request, HttpServletResponse response,
								Model model, ShopConfig entity, boolean isCreate) throws Exception {

		// 【排查问题】记录保存前的原始值（从数据库获取）
		String originalProductDescriptionFromDb = null;
		if (!isCreate && entity != null && entity.getId() != null) {
			ShopConfig exist = getEntityService().get(entity.getId());
			if (exist != null) {
				originalProductDescriptionFromDb = exist.getProductDescription();
				log.info("【排查】保存前 - 数据库中productDescription原始值（长度: {}）: {}", 
						originalProductDescriptionFromDb != null ? originalProductDescriptionFromDb.length() : 0,
						originalProductDescriptionFromDb != null && originalProductDescriptionFromDb.length() > 100 
							? originalProductDescriptionFromDb.substring(0, 100) + "..." 
							: originalProductDescriptionFromDb);
			}
		}

		// 【排查问题】记录提交时的值
		if (entity != null && entity.getProductDescription() != null) {
			String submittedValue = entity.getProductDescription();
			log.info("【排查】保存时 - 提交的productDescription值（长度: {}）: {}", 
					submittedValue.length(),
					submittedValue.length() > 100 ? submittedValue.substring(0, 100) + "..." : submittedValue);
		}

		if (entity != null) {
			if (entity.getProductDescription() != null && !entity.getProductDescription().trim().isEmpty()) {
				String beforeDecode = entity.getProductDescription();
				String decodedDesc = StringEscapeUtils.unescapeHtml4(entity.getProductDescription());
				if (!decodedDesc.equals(entity.getProductDescription())) {
					log.info("【排查】productDescription HTML解码 - 解码前长度: {}, 解码后长度: {}", 
							beforeDecode.length(), decodedDesc.length());
					entity.setProductDescription(decodedDesc);
				} else {
					log.debug("【排查】productDescription无需HTML解码");
				}
				
				// 【修复问题】检查并防止重复累积
				if (!isCreate && originalProductDescriptionFromDb != null 
						&& !originalProductDescriptionFromDb.trim().isEmpty()) {
					String currentValue = entity.getProductDescription();
					String originalValue = originalProductDescriptionFromDb.trim();
					
					// 检查是否是完全重复模式（例如：原始值是"ABC"，提交值是"ABCABC"）
					if (currentValue.length() >= originalValue.length() * 2) {
						int originalLen = originalValue.length();
						String firstPart = currentValue.substring(0, originalLen);
						String secondPart = currentValue.substring(originalLen, 
								Math.min(originalLen * 2, currentValue.length()));
						
						// 如果前两部分都等于原始值，说明是完全重复
						if (firstPart.equals(originalValue) && secondPart.equals(originalValue)) {
							log.warn("【修复】检测到productDescription值被完全重复！原始长度: {}, 提交长度: {}, 已修复为去重后的值", 
									originalValue.length(), currentValue.length());
							// 修复：去除重复，只保留一份
							entity.setProductDescription(originalValue);
						} else if (currentValue.length() > originalValue.length() * 1.5 
								&& currentValue.contains(originalValue)) {
							// 如果是部分重复，尝试去除重复部分
							// 检查是否以原始值开头且后面又包含原始值
							if (currentValue.startsWith(originalValue) 
									&& currentValue.substring(originalLen).contains(originalValue)) {
								log.warn("【修复】检测到productDescription值被部分重复！原始长度: {}, 提交长度: {}, 尝试修复", 
										originalValue.length(), currentValue.length());
								// 如果提交的值只是原始值的重复，使用原始值
								String withoutFirst = currentValue.substring(originalLen);
								if (withoutFirst.trim().equals(originalValue) 
										|| withoutFirst.trim().startsWith(originalValue)) {
									entity.setProductDescription(originalValue);
								}
							}
						}
					}
				}
			}
			if (entity.getHeadDisplay() != null && !entity.getHeadDisplay().trim().isEmpty()) {
				String decodedDescription = StringEscapeUtils.unescapeHtml4(entity.getHeadDisplay());
				if (!decodedDescription.equals(entity.getHeadDisplay())) {
					log.debug("商品描述HTML解码 - 原始: {}, 解码后: {}", entity.getHeadDisplay(), decodedDescription);
					entity.setHeadDisplay(decodedDescription);
				}
			}
		}



		// ========== 1. 处理图片上传（logoUrl 和 productDescriptionUrl）==========
		// 配置上传设置
		configureUploadSettings();

		// 修改时，先保存原有值（用于判断是否被清空）
		String originalLogoUrl = null;
		String originalProductDescriptionUrl = null;
		if (!isCreate && entity != null && entity.getId() != null) {
			ShopConfig exist = getEntityService().get(entity.getId());
			if (exist != null) {
				originalLogoUrl = exist.getLogoUrl();
				originalProductDescriptionUrl = exist.getProductDescriptionUrl();
				log.debug("修改配置，保存原有图片URL - logoUrl: {}, productDescriptionUrl: {}",
						originalLogoUrl, originalProductDescriptionUrl);
			}
		}

		// 重要：只调用一次 doUpload，上传所有文件，然后从结果中提取
		Map<String, String> uploadedFileUrls = new HashMap<>();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
			// 检查是否有文件上传
			MultipartFile logoFile = mreq.getFile(LOGO_FILE_FIELD_NAME);
			MultipartFile productDescriptionFile = mreq.getFile(PRODUCT_DESCRIPTION_FILE_FIELD_NAME);
			boolean hasFiles = (logoFile != null && !logoFile.isEmpty())
					|| (productDescriptionFile != null && !productDescriptionFile.isEmpty());

			if (hasFiles) {
				try {
					// 一次性上传所有文件
					Map<String, UploadResult> uploadResultMap = doUpload(request);
					if (uploadResultMap != null && !uploadResultMap.isEmpty()) {
						String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
								? oFileProperties.getServerRoot().replaceAll("/+$", "") : "";

						// 提取所有上传的文件URL
						for (Map.Entry<String, UploadResult> entry : uploadResultMap.entrySet()) {
							UploadResult ur = entry.getValue();
							if (ur != null && ur.getRelativeFile() != null) {
								String relative = ur.getRelativeFile();
								String fullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;
								uploadedFileUrls.put(entry.getKey(), fullUrl);
								log.debug("文件上传成功 - 字段: {}, URL: {}", entry.getKey(), fullUrl);
							}
						}
					}
				} catch (Exception e) {
					log.error("文件上传失败", e);
					throw new BusinessException("UPLOAD_ERROR", "文件上传失败：" + e.getMessage(), "");
				}
			}
		}

		// 处理 logoUrl
		String uploadedLogoUrl = uploadedFileUrls.get(LOGO_FILE_FIELD_NAME);
		if (uploadedLogoUrl != null) {
			// 上传了新文件，使用新URL
			entity.setLogoUrl(uploadedLogoUrl);
			log.debug("Logo图片上传成功，URL: {}", uploadedLogoUrl);
		} else {
			// 没有上传新文件
			if (isCreate) {
				// 新增时：如果没有上传文件，设置为null（不支持手动输入URL）
				entity.setLogoUrl(null);
				log.debug("新增配置，未上传Logo图片，设置为null");
			} else {
				// 修改时：如果没有上传新文件，判断是否被清空
				String currentLogoUrl = entity.getLogoUrl();
				if (currentLogoUrl == null || currentLogoUrl.trim().isEmpty()) {
					// 隐藏字段被清空（用户点击了清除按钮），清空数据库中的URL
					entity.setLogoUrl(null);
					log.debug("修改配置，Logo图片被清空，设置为null");
				} else {
					// 隐藏字段有值（保持原有值），使用原有值
					entity.setLogoUrl(originalLogoUrl);
					log.debug("修改配置，未上传新Logo图片，保持原有值: {}", originalLogoUrl);
				}
			}
		}

		// 处理 productDescriptionUrl
		String uploadedProductDescriptionUrl = uploadedFileUrls.get(PRODUCT_DESCRIPTION_FILE_FIELD_NAME);
		if (uploadedProductDescriptionUrl != null) {
			// 上传了新文件，使用新URL
			entity.setProductDescriptionUrl(uploadedProductDescriptionUrl);
			log.debug("商品描述图片上传成功，URL: {}", uploadedProductDescriptionUrl);
		} else {
			// 没有上传新文件
			if (isCreate) {
				// 新增时：如果没有上传文件，设置为null（不支持手动输入URL）
				entity.setProductDescriptionUrl(null);
				log.debug("新增配置，未上传商品描述图片，设置为null");
			} else {
				// 修改时：如果没有上传新文件，判断是否被清空
				String currentProductDescriptionUrl = entity.getProductDescriptionUrl();
				if (currentProductDescriptionUrl == null || currentProductDescriptionUrl.trim().isEmpty()) {
					// 隐藏字段被清空（用户点击了清除按钮），清空数据库中的URL
					entity.setProductDescriptionUrl(null);
					log.debug("修改配置，商品描述图片被清空，设置为null");
				} else {
					// 隐藏字段有值（保持原有值），使用原有值
					entity.setProductDescriptionUrl(originalProductDescriptionUrl);
					log.debug("修改配置，未上传新商品描述图片，保持原有值: {}", originalProductDescriptionUrl);
				}
			}
		}

		// ========== 2. 处理 headDisplay Base64 编码（现有逻辑）==========
		// 对headDisplay进行Base64编码，避免特殊字符（如>、emoji等）导致MyBatis日志拦截器报错
		String headDisplay = entity.getHeadDisplay();
		if (headDisplay != null && !headDisplay.trim().isEmpty()) {
			try {
				// 使用Base64编码，避免特殊字符问题
				String encoded = Base64.getEncoder().encodeToString(headDisplay.getBytes("UTF-8"));
				entity.setHeadDisplay(encoded);
				log.debug("headDisplay已Base64编码，长度: {}", encoded.length());
			} catch (Exception e) {
				log.warn("headDisplay Base64编码失败，使用原始值", e);
				// 编码失败时使用原始值，但可能会触发日志拦截器错误
			}
		}

		// ========== 3. productDescription 不需要特殊处理，框架会自动绑定 ==========

		// 【排查问题】记录保存前的值
		if (entity != null && entity.getProductDescription() != null) {
			log.info("【排查】调用super.onSave前 - productDescription值（长度: {}）: {}", 
					entity.getProductDescription().length(),
					entity.getProductDescription().length() > 100 
						? entity.getProductDescription().substring(0, 100) + "..." 
						: entity.getProductDescription());
		}

		ShopConfig saved = super.onSave(request, response, model, entity, isCreate);

		// 【排查问题】记录保存后的值
		if (saved != null && saved.getProductDescription() != null) {
			log.info("【排查】保存后 - productDescription值（长度: {}）: {}", 
					saved.getProductDescription().length(),
					saved.getProductDescription().length() > 100 
						? saved.getProductDescription().substring(0, 100) + "..." 
						: saved.getProductDescription());
			
			// 检查是否有重复累积的问题
			if (originalProductDescriptionFromDb != null && saved.getProductDescription() != null) {
				if (saved.getProductDescription().contains(originalProductDescriptionFromDb) 
						&& saved.getProductDescription().length() > originalProductDescriptionFromDb.length() * 1.5) {
					log.warn("【排查】⚠️ 疑似productDescription值被重复累积！原始长度: {}, 保存后长度: {}", 
							originalProductDescriptionFromDb.length(), saved.getProductDescription().length());
				}
			}
		}

		// 清除首页缓存（参考getHomeContent方法的缓存key）
		redisUtil.deleteHotResultKeys("home:content");
		log.info("配置保存成功，已清除首页缓存");

		return saved;
	}

	/**
	 * 配置上传参数
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

			// 设置 storageRoot
			if (oFileProperties != null && oFileProperties.getStorageRoot() != null) {
				uploadConfig.setStorageRoot(oFileProperties.getStorageRoot());
			}
		} catch (Exception e) {
			log.error("配置上传设置失败", e);
			throw new BusinessException("UPLOAD_CONFIG_ERROR", "上传配置失败：" + e.getMessage(), "");
		}
	}


	/**
	 * 解析商品ID数组
	 */
	private List<ShopProducts> parseProductIds(String jsonStr) {
		if (jsonStr == null || jsonStr.trim().isEmpty()) {
			return Collections.emptyList();
		}

		try {
			List<Long> ids = objectMapper.readValue(jsonStr, new TypeReference<List<Long>>() {});

			if (ids == null || ids.isEmpty()) {
				return Collections.emptyList();
			}

			// 批量查询商品
			List<ShopProducts> products = new ArrayList<>();
			for (Long id : ids) {
				ShopProducts product = shopProductsService.get(id);
				if (product != null) {
					products.add(product);
				}
			}
			return products;
		} catch (Exception e) {
			log.warn("解析商品ID数组失败: {}", jsonStr, e);
			return Collections.emptyList();
		}
	}

	/**
	 * 解析评论ID数组
	 */
	private List<ShopReviews> parseReviewIds(String jsonStr) {
		if (jsonStr == null || jsonStr.trim().isEmpty()) {
			return Collections.emptyList();
		}

		try {
			List<Long> ids = objectMapper.readValue(jsonStr, new TypeReference<List<Long>>() {});

			if (ids == null || ids.isEmpty()) {
				return Collections.emptyList();
			}

			// 批量查询评论
			List<ShopReviews> reviews = new ArrayList<>();
			for (Long id : ids) {
				ShopReviews review = shopReviewsService.get(id);
				if (review != null) {
					reviews.add(review);
				}
			}
			return reviews;
		} catch (Exception e) {
			log.warn("解析评论ID数组失败: {}", jsonStr, e);
			return Collections.emptyList();
		}
	}

	/**
	 * 解析headDisplay（支持Base64编码、JSON格式和纯文本格式）
	 * 优先尝试Base64解码，然后尝试JSON解析，最后按纯文本处理
	 */
	private HeadDisplayDTO parseHeadDisplay(String encodedOrTextStr) {
		if (encodedOrTextStr == null || encodedOrTextStr.trim().isEmpty()) {
			return null;
		}

		String textStr = null;

		// 1. 优先尝试Base64解码（新格式）
		try {
			byte[] decoded = Base64.getDecoder().decode(encodedOrTextStr);
			textStr = new String(decoded, "UTF-8");
			log.debug("headDisplay Base64解码成功");
		} catch (Exception e) {
			// Base64解码失败，可能是旧格式，继续尝试其他方式
			textStr = encodedOrTextStr;
		}

		// 2. 尝试解析为JSON格式（兼容旧JSON数据）
		try {
			HeadDisplayDTO dto = objectMapper.readValue(textStr, HeadDisplayDTO.class);
			if (dto != null && dto.getType() != null) {
				return dto;
			}
		} catch (Exception e) {
			// 不是JSON格式，按纯文本处理
		}

		// 3. 纯文本格式：每行一条消息
		String[] lines = textStr.split("\n");
		List<String> messages = new ArrayList<>();
		for (String line : lines) {
			String trimmed = line.trim();
			if (!trimmed.isEmpty()) {
				messages.add(trimmed);
			}
		}

		if (messages.isEmpty()) {
			return null;
		}

		// 转换为DTO格式（用于前端显示）
		HeadDisplayDTO dto = new HeadDisplayDTO();
		dto.setType("messages");
		List<HeadDisplayDTO.MessageItem> items = new ArrayList<>();
		for (String msg : messages) {
			HeadDisplayDTO.MessageItem item = new HeadDisplayDTO.MessageItem();
			item.setText(msg);
			items.add(item);
		}
		dto.setItems(items);

		return dto;
	}
}
