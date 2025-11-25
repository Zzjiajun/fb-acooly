/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.shop.entity.ShopParentCategories;
import com.acooly.showcase.shop.entity.ShopSubCategories;
import com.acooly.showcase.shop.service.ShopParentCategoriesService;
import com.acooly.showcase.shop.service.ShopSubCategoriesService;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import com.acooly.core.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.service.ShopProductsService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.stream.Collectors;

/**
 * 商品表（关联子级分类） 管理控制器
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopProducts")
public class ShopProductsManagerController extends AbstractJsonEntityController<ShopProducts, ShopProductsService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ShopProductsService shopProductsService;

    @Autowired(required = false)
    private Environment environment;

    @Autowired
    private ShopSubCategoriesService shopSubCategoriesService;

    @Autowired
    private ShopParentCategoriesService shopParentCategoriesService;

    @Autowired
    private RedisShopUtil redisUtil;

    @Autowired
    private ApplicationContext applicationContext;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @PostConstruct
//    public void verifyRedisConfig() {
//        System.out.println("========== Redis 配置验证 ==========");
//        System.out.println("Value Serializer: " + redisTemplate.getValueSerializer().getClass().getName());
//
//        // 应该看到: GenericJackson2JsonRedisSerializer
//        // 而不是: com.acooly.module.cache.DefaultRedisSerializer
//    }
    @Resource
    private OFileProperties oFileProperties; // 用于获取 storageRoot 和 serverRoot（访问域名前缀）

    // ========== 商品图片上传配置常量 ==========
    private static final String STORAGE_NAMESPACE = "shop/products";
    private static final String ALLOWED_EXTENSIONS = "jpg,png,jpeg,gif,webp";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String IMAGE_FILE_FIELD_NAME = "imageFile"; // 文件上传字段名
    private static final String IMAGE_URL_FIELD_NAME = "imageUrl"; // 实体属性名/隐藏字段名

    private static final String PRODUCT_LIST_FULL_RESULT = "product:list:full_result";
    public static final String PRODUCT_Count = "products:count:";
    public static final String CATEGORY_TREE = "category:tree:all";
    public static final String PRODUCT_LIST = "products:list:";
    public static final String PRODUCT_DETAIL = "product:detail:";
    private static final String PRODUCT_RELATED_PREFIX = "product:related:";
    private static final String SUB_CATEGORY_PRODUCTS_PREFIX = "sub_category:products:";
    private static final String SUB_CATEGORY_WITH_PRODUCTS = "sub_category_with_products:";
    private static final String PARENT_CATEGORY_PRODUCTS_PREFIX = "parent_category:products:";
    public static final String PRODUCT_CATEGORY_WITH_PRODUCTS = "parent_category_with_products:";


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        // 1) 查询所有父级分类
        List<ShopParentCategories> parents = shopParentCategoriesService.getAll();
        // 2) 查询所有子级分类
        List<ShopSubCategories> subs = shopSubCategoriesService.getAll();
        // 3) 构建父级 Map<String, String>（parentId -> parentName）
        // 方式A：Java 8 Stream（保持插入顺序，重复 key 取第一个）

        // 3) 构建父级Map<String,String>: parentId -> parentName
        Map<String, String> parentMap = parents.stream()
                .collect(Collectors.toMap(
                        p -> String.valueOf(p.getId()),
                        ShopParentCategories::getName,
                        (a, b) -> a,
                        LinkedHashMap::new));

        // 4) 构建子级Map<String, Map<String,String>>:
        //    key=parentId(String), value=Map<subId(String), subName(String)>
        Map<String, Map<String, String>> subMap = new LinkedHashMap<>();
        for (ShopSubCategories s : subs) {
            String pid = String.valueOf(s.getParentId());
            subMap.computeIfAbsent(pid, k -> new LinkedHashMap<>())
                    .put(String.valueOf(s.getId()), s.getName());
        }
        // 构建分类完整名称Map: subId -> "父类名称-子类名称"
        Map<String, String> categoryFullNameMap = subs.stream()
                .collect(Collectors.toMap(
                        sub -> String.valueOf(sub.getId()),  // key 转成 String
                        sub -> {
                            String parentName = parentMap.get(String.valueOf(sub.getParentId()));
                            if (parentName == null) parentName = "未知分类";
                            return parentName + "-" + sub.getName();
                        },
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        // 5) 放入模型，页面一次性拿到
        model.put("parentMap", parentMap);
        model.put("subMap", subMap);
        model.put("categoryFullNameMap", categoryFullNameMap);
    }

    @Override
    public JsonEntityResult<ShopProducts> saveJson(HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<ShopProducts> result = new JsonEntityResult<>();
        this.allow(request, response, MappingMethod.create);

        try {
            ShopProducts shopProducts = this.doSave(request, response, (Model) null, true);
            result.setEntity(shopProducts);
            result.setMessage("新增成功");
            //删除商品缓存
            log.info("商品新增成功 - ID: {}, 名称: {}, 图片URL: {}",
                    shopProducts.getId(), shopProducts.getName(), shopProducts.getImageUrl());
        } catch (Exception e) {
            this.handleException(result, "新增", e);
            log.error("商品新增失败", e);
        }

        return result;
    }

    private String buildFullResponseCacheKey(int page, int size) {
        return redisUtil.buildKey(PRODUCT_LIST_FULL_RESULT, String.valueOf(page), String.valueOf(size));
    }

    // ========== 重写updateJson方法 Cyber ==========

    /**
     * 更新商品接口
     */
    @Override
    public JsonEntityResult<ShopProducts> updateJson(HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<ShopProducts> result = new JsonEntityResult<>();
        this.allow(request, response, MappingMethod.update);
        try {
            ShopProducts shopProducts = this.doSave(request, response, (Model) null, false);
            result.setEntity(shopProducts);
            result.setMessage("更新成功");
            log.info("商品更新成功 - ID: {}, 名称: {}, 图片URL: {}",
                    shopProducts.getId(), shopProducts.getName(), shopProducts.getImageUrl());
        } catch (Exception e) {
            this.handleException(result, "更新", e);
            log.error("商品更新失败", e);
        }

        return result;
    }



    // ========== 核心方法：重写onSave处理文件上传 ==========

    /**
     * 保存前的处理：处理文件上传
     * <p>
     * 流程：
     * 1. 检查请求中是否有文件上传
     * 2. 如果有文件，配置上传参数并调用doUpload上传
     * 3. doUpload会自动将上传后的文件URL设置到entity.imageUrl
     * 4. 如果没有文件，保持entity中已有的imageUrl（可能是编辑时保持原有图片）
     * 5. 调用父类方法保存实体
     */

    @Override
    protected ShopProducts onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShopProducts entity, boolean isCreate) throws Exception {
        log.debug("开始处理商品保存 - {}, id={}", isCreate ? "新增" : "更新", entity == null ? "null" : entity.getId());

        // 1) 先确保 uploadConfig 有正确 storageRoot（否则文件会写到系统 tmp）
        configureUploadSettings();

        // 2) 是否为 multipart 请求并包含文件
        boolean hasFile = false;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            if (mreq.getFileMap() != null && !mreq.getFileMap().isEmpty()) {
                hasFile = mreq.getFile(IMAGE_FILE_FIELD_NAME) != null && !mreq.getFile(IMAGE_FILE_FIELD_NAME).isEmpty();
                // 也允许 mreq.getFileMap().containsKey(IMAGE_FILE_FIELD_NAME)
            }
        }

        if (hasFile) {
            // 上传并获取结果（doUpload 来自 AbstractFileOperationController）
            Map<String, UploadResult> uploadResultMap = doUpload(request);
            if (uploadResultMap == null || uploadResultMap.isEmpty()) {
                throw new BusinessException("IMAGE_UPLOAD_FAILED", "图片上传失败：没有上传结果", "");
            }

            // 稳健查找：有可能 key 为 imageFile 或 imageFile0 等，UploadResult.getParameterName() 才是实际表单字段名
            UploadResult matched = null;
            for (UploadResult ur : uploadResultMap.values()) {
                String paramName = ur.getParameterName();
                if (IMAGE_FILE_FIELD_NAME.equals(paramName) || paramName != null && paramName.startsWith(IMAGE_FILE_FIELD_NAME)) {
                    matched = ur;
                    break;
                }
            }

            if (matched == null) {
                // 兜底：如果没找到精确匹配，尝试取第一个非空文件
                for (UploadResult ur : uploadResultMap.values()) {
                    if (ur != null && ur.getRelativeFile() != null) {
                        matched = ur;
                        break;
                    }
                }
            }

            if (matched == null || matched.getRelativeFile() == null) {
                throw new BusinessException("IMAGE_UPLOAD_NO_URL", "图片上传失败，未能获取文件路径", "");
            }

            // matched.getRelativeFile() 是相对路径（/xxx/xxx.jpg），需要拼接 serverRoot
            String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
                    ? oFileProperties.getServerRoot().replaceAll("/+$", "")  // 去尾斜杠
                    : "";

            String relative = matched.getRelativeFile();
            // relative 一般以 / 开始，确保拼接正确
            String finalUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;

            entity.setImageUrl(finalUrl);
            log.info("图片上传并设置到实体：{}", finalUrl);
        } else {
            // 没有文件上传：处理表单中 imageUrl 字段（新增/更新）
            String imageUrlFromForm = request.getParameter(IMAGE_URL_FIELD_NAME);
            if (!isCreate) {
                // 更新操作：如果表单明确给了 imageUrl（可能为空串表示删除），以表单为准；否则保持 DB 中原值
                if (imageUrlFromForm != null) {
                    entity.setImageUrl(imageUrlFromForm.trim().isEmpty() ? null : imageUrlFromForm);
                } else if (entity.getId() != null) {
                    // 保持库中的原值
                    ShopProducts exist = getEntityService().get(entity.getId());
                    if (exist != null) {
                        entity.setImageUrl(exist.getImageUrl());
                    }
                }
            } else {
                // 新增操作：若表单带了 imageUrl（例如从外链），则直接使用
                if (imageUrlFromForm != null && !imageUrlFromForm.trim().isEmpty()) {
                    entity.setImageUrl(imageUrlFromForm);
                }
            }
        }

        // 调用父类保存（父类会执行保存逻辑）
        ShopProducts saved = super.onSave(request, response, model, entity, isCreate);
        log.debug("保存完成 - id={}, imageUrl={}", saved == null ? "null" : saved.getId(), saved == null ? "null" : saved.getImageUrl());
        invalidateProductCache(entity);
        return saved;
    }

    // 配置上传参数（确保设置 storageRoot）
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
            //String urlPrefix = oFileProperties.getServerRoot(); // 返回URL前缀
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
     * 商品缓存失效
     */
    private void invalidateProductCache(ShopProducts product) {
        if (product == null) {
            log.warn("invalidateProductCache called with null product");
            return;
        }
        Long productId = product.getId();
        Long subId = product.getSubCategoryId();
        if (subId == null) {
            log.warn("Product ID {} has null subCategoryId, skipping category cache cleanup", productId);
            return;
        }
        ShopSubCategories shopSubCategories = shopSubCategoriesService.get(subId);
        if (shopSubCategories == null) {
            log.warn("SubCategory ID {} not found for product ID {}", subId, productId);
            return;
        }
        Long parentId = shopSubCategories.getParentId();
        // ==== 产品相关缓存 ====
        redisUtil.del(redisUtil.buildKey(PRODUCT_DETAIL, String.valueOf(productId)));
        redisUtil.del(redisUtil.buildKey(PRODUCT_RELATED_PREFIX, String.valueOf(productId)));

        // ==== 商品列表缓存 ====
        redisUtil.deleteByPattern(PRODUCT_LIST + "*");
        redisUtil.deleteByPattern(PRODUCT_LIST_FULL_RESULT + "*");
        redisUtil.del(PRODUCT_Count);

        // ==== 分类相关缓存 ====
        redisUtil.del(CATEGORY_TREE); // 这里修复了 CATEGORY_TREE 未定义问题

        redisUtil.deleteByPattern(SUB_CATEGORY_PRODUCTS_PREFIX + subId + "*");
        redisUtil.deleteByPattern(SUB_CATEGORY_WITH_PRODUCTS + subId + "*");

        if (parentId != null) {
            redisUtil.deleteByPattern(PARENT_CATEGORY_PRODUCTS_PREFIX + parentId + "*");
            redisUtil.deleteByPattern(PRODUCT_CATEGORY_WITH_PRODUCTS + parentId + "*");
        }

        log.info("🔄 Cache invalidated for productID={}, subId={}, parentId={}", productId, subId, parentId);
    }

    private boolean checkFileUpload(HttpServletRequest request) {
        // 步骤1：检查是否为multipart请求
        if (!(request instanceof MultipartHttpServletRequest)) {
            log.debug("请求不是multipart类型，无文件上传");
            return false;
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // 步骤2：检查是否有名为"imageFile"的文件上传
        MultipartFile file = multipartRequest.getFile(IMAGE_FILE_FIELD_NAME);

        if (file != null && !file.isEmpty()) {
            log.debug("检测到文件上传 - 文件名: {}, 大小: {}字节 ({}MB), 类型: {}",
                    file.getOriginalFilename(),
                    file.getSize(),
                    String.format("%.2f", file.getSize() / 1024.0 / 1024.0),
                    file.getContentType());
            return true;
        }

        log.debug("未检测到名为'{}'的文件上传", IMAGE_FILE_FIELD_NAME);
        return false;
    }

    // ========== 辅助方法：确保目录存在 ==========

    /**
     * 确保存储根目录存在且有写入权限
     *
     * @param storageRoot 存储根路径
     */
    private void ensureDirectoryExists(String storageRoot) {
        if (Strings.isBlank(storageRoot)) {
            throw new BusinessException("STORAGE_ROOT_EMPTY", "存储根路径为空", "");
        }

        try {
            File rootDir = new File(storageRoot);

            // 检查目录是否存在
            if (!rootDir.exists()) {
                log.info("存储根目录不存在，尝试创建: {}", storageRoot);
                boolean created = rootDir.mkdirs();
                if (!created) {
                    log.error("无法创建存储根目录: {}", storageRoot);
                    throw new BusinessException("STORAGE_ROOT_CREATE_FAILED",
                            "无法创建存储根目录: " + storageRoot + "，请检查路径权限", "");
                }
                log.info("存储根目录创建成功: {}", storageRoot);
            } else {
                // 检查是否为目录
                if (!rootDir.isDirectory()) {
                    log.error("存储根路径不是目录: {}", storageRoot);
                    throw new BusinessException("STORAGE_ROOT_NOT_DIRECTORY",
                            "存储根路径不是目录: " + storageRoot, "");
                }
            }

            // 检查目录是否有写入权限
            if (!rootDir.canWrite()) {
                log.error("存储根目录没有写入权限: {}", storageRoot);
                throw new BusinessException("STORAGE_ROOT_NO_WRITE_PERMISSION",
                        "存储根目录没有写入权限: " + storageRoot + "，请修改目录权限", "");
            }

            log.debug("存储根目录验证通过 - 路径: {}, 存在: {}, 可写: {}",
                    storageRoot, rootDir.exists(), rootDir.canWrite());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证存储根目录时发生异常 - 路径: {}, 异常: {}", storageRoot, e.getMessage(), e);
            throw new BusinessException("STORAGE_ROOT_VALIDATE_ERROR",
                    "验证存储根目录时发生异常: " + e.getMessage(), "");
        }
    }
}
