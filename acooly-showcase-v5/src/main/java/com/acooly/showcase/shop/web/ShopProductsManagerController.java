/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.web;

import java.io.Serializable;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.module.event.EventBus;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.showcase.shop.dto.*;
import com.acooly.showcase.shop.entity.ShopAttr;
import com.acooly.showcase.shop.entity.ShopAttrValue;
import com.acooly.showcase.shop.entity.ShopBrand;
import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.entity.ShopParentCategories;
import com.acooly.showcase.shop.entity.ShopProductAttr;
import com.acooly.showcase.shop.entity.ShopProductImages;
import com.acooly.showcase.shop.entity.ShopSubCategories;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import com.acooly.showcase.shop.event.CreateShopEsEvent;
import com.acooly.showcase.shop.service.*;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import com.acooly.showcase.shop.utils.SerialNumberGenerator;
import com.aliyuncs.utils.MapUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.acooly.core.utils.Strings;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.shop.entity.ShopProducts;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private SerialNumberGenerator serialNumberGenerator;
    @Autowired
    private ShopSubCategoriesService shopSubCategoriesService;

    @Autowired
    private ShopParentCategoriesService shopParentCategoriesService;

    @Autowired
    private ShopProductImagesService shopProductImagesService;

    @Autowired
    private ShopBrandService shopBrandService;

    @Autowired
    private ShopAttrService shopAttrService;

    @Autowired
    private ShopAttrValueService shopAttrValueService;

    @Autowired
    private ShopProductAttrService shopProductAttrService;

    @Autowired
    private ShopI18nLocalesService shopI18nLocalesService;

    @Autowired
    private ShopI18nTranslationsService shopI18nTranslationsService;

    @Autowired
    private com.acooly.showcase.shop.service.ProductTranslationSyncService productTranslationSyncService;

    @Autowired(required = false)
    private com.acooly.showcase.shop.service.OssStorageService ossStorageService;

    @Autowired(required = false)
    private com.acooly.showcase.shop.utils.ImageCompressUtil imageCompressUtil;

    @Autowired(required = false)
    private com.acooly.showcase.shop.service.ProductImageService productImageService;

    @Autowired(required = false)
    private com.acooly.showcase.shop.service.PrimaryImageService primaryImageService;

    @Autowired
    private RedisShopUtil redisUtil;
    @Autowired
    private EventBus eventBus;
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
    private static final String PRODUCT_IMAGE_LIST = "product:image:list:";
    private static final String SUB_ALL_PRODUCTS = "sub:all:products";
    private static final String PRODUCT_PRIMARY_IMAGE="product:primary_image:";

    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(2, r -> {
        Thread t = new Thread(r, "cache-eviction-async");
        t.setDaemon(true);
        return t;
    });

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
        // 5) 查询所有品牌，构建品牌Map
        List<ShopBrand> brands = shopBrandService.getAll();
        Map<String, String> brandMap = brands.stream()
                .filter(brand -> brand.getStatus() != null && brand.getStatus() == 1) // 只显示启用的品牌
                .collect(Collectors.toMap(
                        brand -> String.valueOf(brand.getId()),
                        ShopBrand::getName,
                        (a, b) -> a,
                        LinkedHashMap::new));
        model.put("brandMap", brandMap);

        // 6) 查询所有属性和属性值，构建属性数据
        List<ShopAttr> attrs = shopAttrService.getAll();
        Map<String, String> attrMap = attrs.stream()
                .filter(attr -> attr.getStatus() == null || attr.getStatus() == 1) // 只显示启用的属性
                .collect(Collectors.toMap(
                        attr -> String.valueOf(attr.getId()),
                        ShopAttr::getName,
                        (a, b) -> a,
                        LinkedHashMap::new));

        List<ShopAttrValue> attrValues = shopAttrValueService.getAll();
        // 构建属性值Map: attrId -> List<AttrValue>
        Map<String, List<Map<String, Object>>> attrValueMap = new LinkedHashMap<>();
        for (ShopAttrValue attrValue : attrValues) {
            if (attrValue.getStatus() == null || attrValue.getStatus() == 1) { // 只显示启用的属性值
                String attrId = String.valueOf(attrValue.getAttrId());
                attrValueMap.computeIfAbsent(attrId, k -> new ArrayList<>())
                        .add(new HashMap<String, Object>() {{
                            put("id", attrValue.getId());
                            put("value", attrValue.getValue());
                            put("sort", attrValue.getSort() != null ? attrValue.getSort() : 0);
                        }});
            }
        }
        // 对每个属性的属性值按sort排序
        attrValueMap.forEach((attrId, values) -> {
            values.sort((a, b) -> {
                Integer sortA = (Integer) a.get("sort");
                Integer sortB = (Integer) b.get("sort");
                return Integer.compare(sortA != null ? sortA : 0, sortB != null ? sortB : 0);
            });
        });

        model.put("attrMap", attrMap);
        model.put("attrValueMap", attrValueMap);

        // 7) 放入模型，页面一次性拿到
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
            
            // 重要：在doSave返回后，实体已经保存到数据库，此时可以保存图片记录
            // 从request中获取图片处理结果
            ProductImageService.ProductImageProcessResult processResult =
                    (ProductImageService.ProductImageProcessResult) request.getAttribute("_productImageProcessResult");
            String originalImageUrl = (String) request.getAttribute("_productOriginalImageUrl");
            
            // 【性能优化】只有在有图片变化时才保存图片记录
            if (shopProducts != null && shopProducts.getId() != null && processResult != null) {
                // 检查是否有实际的图片操作（新增、删除、主图变化）
                boolean hasImageOperations = (processResult.getToSave() != null && !processResult.getToSave().isEmpty())
                        || (processResult.getToDelete() != null && !processResult.getToDelete().isEmpty())
                        || processResult.isPrimaryImageChanged();
                
                if (hasImageOperations) {
                    // 保存图片记录（此时productId已经存在）
                    if (productImageService != null) {
                        productImageService.saveProductImages(shopProducts.getId(), processResult);
                    } else {
                        saveProductImagesFallback(shopProducts.getId(), processResult);
                    }
                    
                    // 保存图片后，只有在主图真正变化时才同步到ShopProducts.imageUrl
                    if (processResult.isPrimaryImageChanged()) {
                        ShopProductImages finalPrimary = shopProductImagesService.findPrimaryByProductId(shopProducts.getId());
                        if (finalPrimary != null) {
                            // 【重要】优先使用webpUrl，如果为空则使用imageUrl
                            String finalPrimaryUrl = null;
                            if (finalPrimary.getWebpUrl() != null && !finalPrimary.getWebpUrl().trim().isEmpty()) {
                                finalPrimaryUrl = finalPrimary.getWebpUrl();
                                log.debug("【主图同步】使用主图记录的webpUrl - 商品ID: {}", shopProducts.getId());
                            } else {
                                finalPrimaryUrl = finalPrimary.getImageUrl();
                                log.debug("【主图同步】主图记录的webpUrl为空，使用imageUrl - 商品ID: {}", shopProducts.getId());
                            }
                            
                            // 只有当主图真正变化时，才更新imageUrl
                            if (shopProducts.getImageUrl() == null || !finalPrimaryUrl.equals(shopProducts.getImageUrl())) {
                                shopProducts.setImageUrl(finalPrimaryUrl);
                                getEntityService().update(shopProducts);
                                log.info("主图已变化 - 商品ID: {}", shopProducts.getId());
                            }
                        } else if (shopProducts.getImageUrl() != null && processResult.getToSave().isEmpty()) {
                            // 如果没有主图了，且所有图片都被删除，清空imageUrl
                            shopProducts.setImageUrl(null);
                            getEntityService().update(shopProducts);
                                log.info("清空商品主图URL - 商品ID: {}", shopProducts.getId());
                        }
                    } else if (originalImageUrl != null && (shopProducts.getImageUrl() == null || !shopProducts.getImageUrl().equals(originalImageUrl))) {
                        // 主图未变化，但需要检查webpUrl字段
                        ShopProductImages currentPrimary = shopProductImagesService.findPrimaryByProductId(shopProducts.getId());
                        String finalUrl = null;
                        if (currentPrimary != null) {
                            // 优先使用webpUrl，如果为空则使用imageUrl
                            if (currentPrimary.getWebpUrl() != null && !currentPrimary.getWebpUrl().trim().isEmpty()) {
                                finalUrl = currentPrimary.getWebpUrl();
                                log.debug("【主图同步】主图未变化，使用主图记录的webpUrl - 商品ID: {}", shopProducts.getId());
                            } else {
                                finalUrl = currentPrimary.getImageUrl();
                                log.debug("【主图同步】主图未变化，主图记录的webpUrl为空，使用imageUrl - 商品ID: {}", shopProducts.getId());
                            }
                        }
                        
                        // 如果查询到最终URL，使用它；否则使用原始imageUrl
                        shopProducts.setImageUrl(finalUrl != null ? finalUrl : originalImageUrl);
                        getEntityService().update(shopProducts);
                        log.debug("主图未变化，已同步主图URL - 商品ID: {}", shopProducts.getId());
                    }
                } else {
                    log.debug("没有图片操作，跳过图片保存和主图同步 - 商品ID: {}", shopProducts.getId());
                }
            }
            
            // 处理商品属性关联
            // 直接从请求参数获取（前端发送的是 attrValueIds）
            String attrValueIdsStr = request.getParameter("attrValueIds");
            if (attrValueIdsStr == null || attrValueIdsStr.trim().isEmpty()) {
                // 如果参数为空，尝试从隐藏字段获取
                attrValueIdsStr = request.getParameter("productAttrValueIds");
            }
            log.info("【商品属性保存】saveJson - 商品ID: {}, 参数名attrValueIds: {}, 参数名productAttrValueIds: {}, 最终值: {}", 
                    shopProducts != null ? shopProducts.getId() : "null",
                    request.getParameter("attrValueIds"),
                    request.getParameter("productAttrValueIds"),
                    attrValueIdsStr != null ? attrValueIdsStr : "null");
            if (shopProducts != null && shopProducts.getId() != null) {
                saveProductAttributes(shopProducts.getId(), attrValueIdsStr);
            } else {
                log.warn("【商品属性保存】saveJson - 商品或商品ID为null，跳过属性保存");
            }

            // 重要：重新从数据库加载最新数据，确保包含最新的imageUrl（主图URL可能已同步）
            // ES只需要ShopProducts实体，不需要ShopProductImages，但需要确保imageUrl是最新的主图URL
            if (shopProducts != null && shopProducts.getId() != null) {
                shopProducts = getEntityService().get(shopProducts.getId());
                log.debug("重新加载商品数据用于ES - 商品ID: {}, imageUrl: {}", shopProducts.getId(), shopProducts.getImageUrl());
                CacheShopRedisEvent event = new CacheShopRedisEvent();
                event.setShopProducts(shopProducts);
                event.setAction("Product");
                eventBus.publish(event);

            }
            
            // 【数据同步】同步商品表数据到默认语言的翻译表（创建时自动创建默认翻译）
            if (shopProducts != null && shopProducts.getId() != null) {
                try {
                    productTranslationSyncService.syncProductToDefaultTranslation(shopProducts);
                    log.info("已同步商品数据到默认语言翻译（创建） - 商品ID: {}", shopProducts.getId());
                } catch (Exception e) {
                    log.error("同步商品数据到默认语言翻译失败（创建） - 商品ID: {}", shopProducts.getId(), e);
                    // 不抛出异常，避免影响商品保存流程
                }
            }
            
            result.setEntity(shopProducts);
            result.setMessage("新增成功");
            log.info("商品新增成功 - ID: {}, 名称: {}, 图片URL: {}",
                    shopProducts.getId(), shopProducts.getName(), shopProducts.getImageUrl());
        } catch (Exception e) {
            this.handleException(result, "新增", e);
            log.error("商品新增失败", e);
        }
        //异步事件添加es内容（使用重新加载后的最新数据）
        if (result.getEntity() != null && result.getEntity().getId() != null) {
            eventEsShopProduct(result.getEntity(), "create", new Long[]{result.getEntity().getId()});
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
            
            // 重要：在doSave返回后，实体已经保存到数据库，此时可以保存图片记录
            // 从request中获取图片处理结果
            ProductImageService.ProductImageProcessResult processResult =
                    (ProductImageService.ProductImageProcessResult) request.getAttribute("_productImageProcessResult");
            String originalImageUrl = (String) request.getAttribute("_productOriginalImageUrl");
            
            // 【性能优化】只有在有图片变化时才保存图片记录
            if (shopProducts != null && shopProducts.getId() != null && processResult != null) {
                // 检查是否有实际的图片操作（新增、删除、主图变化）
                boolean hasImageOperations = (processResult.getToSave() != null && !processResult.getToSave().isEmpty())
                        || (processResult.getToDelete() != null && !processResult.getToDelete().isEmpty())
                        || processResult.isPrimaryImageChanged();
                
                if (hasImageOperations) {
                    // 使用Service保存图片记录
                    if (productImageService != null) {
                        productImageService.saveProductImages(shopProducts.getId(), processResult);
                    } else {
                        // 降级处理
                        saveProductImagesFallback(shopProducts.getId(), processResult);
                    }
                    
                    // 保存图片后，只有在主图真正变化时才同步到ShopProducts.imageUrl
                    if (processResult.isPrimaryImageChanged()) {
                        ShopProductImages finalPrimary = shopProductImagesService.findPrimaryByProductId(shopProducts.getId());
                        if (finalPrimary != null) {
                            // 【重要】优先使用webpUrl，如果为空则使用imageUrl
                            String finalPrimaryUrl = null;
                            if (finalPrimary.getWebpUrl() != null && !finalPrimary.getWebpUrl().trim().isEmpty()) {
                                finalPrimaryUrl = finalPrimary.getWebpUrl();
                                log.debug("【主图同步】使用主图记录的webpUrl - 商品ID: {}", shopProducts.getId());
                            } else {
                                finalPrimaryUrl = finalPrimary.getImageUrl();
                                log.debug("【主图同步】主图记录的webpUrl为空，使用imageUrl - 商品ID: {}", shopProducts.getId());
                            }
                            
                            // 只有当主图真正变化时，才更新imageUrl
                            if (shopProducts.getImageUrl() == null || !finalPrimaryUrl.equals(shopProducts.getImageUrl())) {
                                shopProducts.setImageUrl(finalPrimaryUrl);
                                getEntityService().update(shopProducts);
                                log.info("主图已变化 - 商品ID: {}", shopProducts.getId());
                            }
                        } else if (shopProducts.getImageUrl() != null && processResult.getToSave().isEmpty()) {
                            // 如果没有主图了，且所有图片都被删除，清空imageUrl
                            shopProducts.setImageUrl(null);
                            getEntityService().update(shopProducts);
                                log.info("清空商品主图URL - 商品ID: {}", shopProducts.getId());
                        }
                    } else if (originalImageUrl != null && (shopProducts.getImageUrl() == null || !shopProducts.getImageUrl().equals(originalImageUrl))) {
                        // 主图未变化，但需要检查webpUrl字段
                        ShopProductImages currentPrimary = shopProductImagesService.findPrimaryByProductId(shopProducts.getId());
                        String finalUrl = null;
                        if (currentPrimary != null) {
                            // 优先使用webpUrl，如果为空则使用imageUrl
                            if (currentPrimary.getWebpUrl() != null && !currentPrimary.getWebpUrl().trim().isEmpty()) {
                                finalUrl = currentPrimary.getWebpUrl();
                                log.debug("【主图同步】主图未变化，使用主图记录的webpUrl - 商品ID: {}", shopProducts.getId());
                            } else {
                                finalUrl = currentPrimary.getImageUrl();
                                log.debug("【主图同步】主图未变化，主图记录的webpUrl为空，使用imageUrl - 商品ID: {}", shopProducts.getId());
                            }
                        }
                        
                        // 如果查询到最终URL，使用它；否则使用原始imageUrl
                        shopProducts.setImageUrl(finalUrl != null ? finalUrl : originalImageUrl);
                        getEntityService().update(shopProducts);
                        log.debug("主图未变化，已同步主图URL - 商品ID: {}", shopProducts.getId());
                    }
                } else {
                    log.debug("没有图片操作，跳过图片保存和主图同步 - 商品ID: {}", shopProducts.getId());
                }
            }
            
            // 处理商品属性关联
            // 直接从请求参数获取（前端发送的是 attrValueIds）
            String attrValueIdsStr = request.getParameter("attrValueIds");
            if (attrValueIdsStr == null || attrValueIdsStr.trim().isEmpty()) {
                // 如果参数为空，尝试从隐藏字段获取
                attrValueIdsStr = request.getParameter("productAttrValueIds");
            }
            log.debug("【商品属性保存】updateJson - 商品ID: {}, 参数名attrValueIds: {}, 参数名productAttrValueIds: {}, 最终值: {}", 
                    shopProducts != null ? shopProducts.getId() : "null",
                    request.getParameter("attrValueIds"),
                    request.getParameter("productAttrValueIds"),
                    attrValueIdsStr != null ? attrValueIdsStr : "null");
            if (shopProducts != null && shopProducts.getId() != null) {
                saveProductAttributes(shopProducts.getId(), attrValueIdsStr);
            } else {
                log.warn("【商品属性保存】updateJson - 商品或商品ID为null，跳过属性保存");
            }

            // 重要：重新从数据库加载最新数据，确保包含最新的imageUrl（主图URL可能已同步）
            // ES只需要ShopProducts实体，不需要ShopProductImages，但需要确保imageUrl是最新的主图URL
            if (shopProducts != null && shopProducts.getId() != null) {
                shopProducts = getEntityService().get(shopProducts.getId());
                log.debug("重新加载商品数据用于ES - 商品ID: {}, imageUrl: {}", shopProducts.getId(), shopProducts.getImageUrl());
                CacheShopRedisEvent event = new CacheShopRedisEvent();
                event.setShopProducts(shopProducts);
                event.setAction("Product");
                eventBus.publish(event);
            }
            
            // 【数据同步】同步商品表数据到默认语言的翻译表（更新时自动更新默认翻译）
            if (shopProducts != null && shopProducts.getId() != null) {
                try {
                    productTranslationSyncService.syncProductToDefaultTranslation(shopProducts);
                    log.info("已同步商品数据到默认语言翻译（更新） - 商品ID: {}", shopProducts.getId());
                } catch (Exception e) {
                    log.error("同步商品数据到默认语言翻译失败（更新） - 商品ID: {}", shopProducts.getId(), e);
                    // 不抛出异常，避免影响商品保存流程
                }
            }
            
            result.setEntity(shopProducts);
            result.setMessage("更新成功");
            log.info("商品更新成功 - ID: {}, 名称: {}, 图片URL: {}",
                    shopProducts.getId(), shopProducts.getName(), shopProducts.getImageUrl());
        } catch (Exception e) {
            this.handleException(result, "更新", e);
            log.error("商品更新失败", e);
        }
        //异步事件更新es内容（使用重新加载后的最新数据）
        if (result.getEntity() != null && result.getEntity().getId() != null) {
            eventEsShopProduct(result.getEntity(), "update", new Long[]{result.getEntity().getId()});
        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
        // 在删除前发布缓存清理事件
        for (Serializable id : ids) {
            if (id instanceof Long) {
                ShopProducts product = this.getEntityService().get((Long) id);
                if (product != null) {
                    CacheShopRedisEvent event = new CacheShopRedisEvent();
                    event.setShopProducts(product);
                    event.setAction("Product");
                    eventBus.publish(event);
                }
                // 删除图片记录
                shopProductImagesService.deleteByProductId((Long) id);
                //删除对应的值
                shopProductAttrService.deleteByProductId((Long) id);
                //删除对应的翻译值
                Map<String, Object> map = Maps.newHashMap();
                map.put("EQ_entityType","product");
                map.put("EQ_entityId",id);
                List<ShopI18nTranslations> query = shopI18nTranslationsService.query(map, null);
                if (query.size() > 0){
                    query.forEach(s ->{
                        shopI18nTranslationsService.removeById(s.getId());
                    });
                }
            }
        }
        // 调用父类删除商品
        super.onRemove(request, response, model, ids);
        // 异步事件删除es内容
        eventEsShopProduct(null, "delete", ids);
    }

    private void eventEsShopProduct(ShopProducts shopProducts, String action,Serializable[] ids) {
        CreateShopEsEvent event = new CreateShopEsEvent();
        event.setAction(action);
        event.setShopProducts(shopProducts);
        event.setIds(ids);
        eventBus.publish(event);
    }

    // ========== 内部类：图片处理相关数据结构 ==========
    // 注意：UploadedImageInfo和ExistingImageInfo已提取为独立的DTO类（com.acooly.showcase.shop.dto包）

    /**
     * 图片处理结果
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ImageProcessResult {
        private List<ShopProductImages> toSave;      // 需要保存的图片记录
        private List<Long> toDelete;                  // 需要删除的图片ID
        private String primaryImageUrl;               // 主图URL（用于同步到ShopProducts.imageUrl）
        private boolean primaryImageChanged;          // 主图是否发生变化（用于判断是否需要更新ShopProducts.imageUrl）
    }

    // ========== 核心方法：重写onSave处理文件上传（统一存储方案） ==========

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
        if(isCreate){
            String generated = serialNumberGenerator.generateShortSerial();
            entity.setSerialNumber(generated);
        }
        // 0. 处理HTML实体编码问题：对商品名称和描述进行解码
        // 防止前端提交时HTML实体（如 &#39;）被直接保存到数据库
        if (entity != null) {
            if (entity.getName() != null && !entity.getName().trim().isEmpty()) {
                String decodedName = StringEscapeUtils.unescapeHtml4(entity.getName());
                if (!decodedName.equals(entity.getName())) {
                    log.debug("商品名称HTML解码 - 原始: {}, 解码后: {}", entity.getName(), decodedName);
                    entity.setName(decodedName);
                }
            }
            if (entity.getDescription() != null && !entity.getDescription().trim().isEmpty()) {
                String decodedDescription = StringEscapeUtils.unescapeHtml4(entity.getDescription());
                if (!decodedDescription.equals(entity.getDescription())) {
                    log.debug("商品描述HTML解码 - 原始: {}, 解码后: {}", entity.getDescription(), decodedDescription);
                    entity.setDescription(decodedDescription);
                }
            }
        }

        // 1. 保存原始的imageUrl（防止doUpload自动修改）
        // 重要：必须在调用doUpload之前保存，因为doUpload可能会修改entity.imageUrl
        String originalImageUrl = entity != null ? entity.getImageUrl() : null;
        if (!isCreate && entity != null && entity.getId() != null) {
            // 编辑时，从数据库获取最新的imageUrl（这是真正的原始值）
            ShopProducts exist = getEntityService().get(entity.getId());
            if (exist != null) {
                originalImageUrl = exist.getImageUrl();
                log.debug("编辑商品，从数据库获取原始imageUrl - 商品ID: {}, imageUrl: {}", entity.getId(), originalImageUrl);
            }
        }
        log.debug("保存原始imageUrl - originalImageUrl: {}", originalImageUrl);

        // 0. 【重要】先获取商品属性关联参数（必须在所有返回之前获取）
        String attrValueIdsStr = request.getParameter("attrValueIds");
        if (attrValueIdsStr == null || attrValueIdsStr.trim().isEmpty()) {
            // 如果参数为空，尝试从隐藏字段获取
            attrValueIdsStr = request.getParameter("productAttrValueIds");
        }
        log.debug("【商品属性保存】onSave - 参数名attrValueIds: {}, 参数名productAttrValueIds: {}, 最终值: {}", 
                request.getParameter("attrValueIds"), 
                request.getParameter("productAttrValueIds"),
                attrValueIdsStr != null ? attrValueIdsStr : "null");
        // 保存到 request attribute，供 saveJson/updateJson 使用
        request.setAttribute("_productAttrValueIds", attrValueIdsStr);

        // 1. 配置上传设置
        configureUploadSettings();

        // 2. 【性能优化】检查是否有图片相关变化，如果没有则跳过图片处理
        ImageChangeType changeType = checkImageChanges(request, entity, isCreate);
        if (changeType == ImageChangeType.NONE) {
            // 没有图片变化，创建空的处理结果，跳过图片处理
            log.debug("没有图片变化，跳过图片处理 - 商品ID: {}", entity != null ? entity.getId() : "null");
            ProductImageService.ProductImageProcessResult emptyResult = new ProductImageService.ProductImageProcessResult();
            emptyResult.setPrimaryImageUrl(originalImageUrl);
            emptyResult.setPrimaryImageChanged(false);
            
            // 保存到 request attribute，供 saveJson/updateJson 使用
            request.setAttribute("_productImageProcessResult", emptyResult);
            request.setAttribute("_productOriginalImageUrl", originalImageUrl);
            
            // 直接保存商品，跳过图片处理
            ShopProducts saved = super.onSave(request, response, model, entity, isCreate);
            return saved;
        } else if (changeType == ImageChangeType.SORT_ONLY) {
            // 只有排序变化，直接更新数据库中的sortOrder，跳过文件上传等操作
            log.debug("只有图片排序变化，直接更新数据库 - 商品ID: {}", entity != null ? entity.getId() : "null");
            updateImageSortOrderOnly(request, entity != null ? entity.getId() : null);
            
            // 创建空的处理结果（因为只更新了排序，没有其他操作）
            ProductImageService.ProductImageProcessResult emptyResult = new ProductImageService.ProductImageProcessResult();
            emptyResult.setPrimaryImageUrl(originalImageUrl);
            emptyResult.setPrimaryImageChanged(false);
            
            // 保存到 request attribute，供 saveJson/updateJson 使用
            request.setAttribute("_productImageProcessResult", emptyResult);
            request.setAttribute("_productOriginalImageUrl", originalImageUrl);
            
            // 直接保存商品，跳过图片处理
            ShopProducts saved = super.onSave(request, response, model, entity, isCreate);
            return saved;
        }

        // 3. 使用Service处理图片上传（需要在Controller中调用doUpload，因为它是父类方法）
        ProductImageService.ProductImageProcessResult processResult = handleImageUploadAndProcessing(
                request, entity, isCreate, originalImageUrl);

        // 4. 使用PrimaryImageService同步主图URL到商品实体
        if (primaryImageService != null) {
            primaryImageService.syncPrimaryImageToProduct(entity, processResult, originalImageUrl, isCreate, request);
        } else {
            // 降级处理：使用原有逻辑
            log.warn("PrimaryImageService未注入，使用降级逻辑");
            syncPrimaryImageUrlFallback(entity, processResult, originalImageUrl, isCreate, request);
        }

        // 5. 保存商品实体
        log.debug("准备保存商品 - 商品ID: {}, entity.imageUrl: {}, primaryImageChanged: {}",
                entity != null ? entity.getId() : "null",
                entity != null ? entity.getImageUrl() : "null",
                processResult.isPrimaryImageChanged());
        ShopProducts saved = super.onSave(request, response, model, entity, isCreate);
        
        // 6. 【数据同步】同步商品表数据到默认语言的翻译表
        // 当商品表的 name 或 description 被修改时，自动同步到默认语言的翻译记录
        if (saved != null && saved.getId() != null) {
            try {
                productTranslationSyncService.syncProductToDefaultTranslation(saved);
                log.debug("已同步商品数据到默认语言翻译 - 商品ID: {}", saved.getId());
            } catch (Exception e) {
                log.error("同步商品数据到默认语言翻译失败 - 商品ID: {}", saved.getId(), e);
                // 不抛出异常，避免影响商品保存流程
            }
        }
        log.debug("商品保存后 - 商品ID: {}", saved != null ? saved.getId() : "null");

        // 6. 如果主图未变化，立即恢复原始imageUrl（防止super.onSave重新绑定了参数）
        if (!isCreate && saved != null && saved.getId() != null && !processResult.isPrimaryImageChanged()) {
            if (originalImageUrl != null && (saved.getImageUrl() == null || !saved.getImageUrl().equals(originalImageUrl))) {
                log.warn("检测到super.onSave后imageUrl被修改 - 商品ID: {}, 原始值: {}, 当前值: {}, 正在恢复...",
                        saved.getId(), originalImageUrl, saved.getImageUrl());
                saved.setImageUrl(originalImageUrl);
                getEntityService().update(saved);
                log.info("已恢复原始imageUrl - 商品ID: {}, imageUrl: {}", saved.getId(), originalImageUrl);
            }
        }

        // 7. 重要：将processResult保存到request attribute，以便在doSave返回后使用
        request.setAttribute("_productImageProcessResult", processResult);
        request.setAttribute("_productOriginalImageUrl", originalImageUrl);
        return saved;
    }

    /**
     * 【重构】处理图片上传和处理（提取方法，简化onSave）
     * 
     * @return 图片处理结果
     */
    private ProductImageService.ProductImageProcessResult handleImageUploadAndProcessing(
            HttpServletRequest request, ShopProducts entity, boolean isCreate, String originalImageUrl) throws Exception {
        
        long startTime = System.currentTimeMillis();
        log.debug("【图片处理】开始处理图片上传 - 商品ID: {}, 是否新增: {}", entity != null ? entity.getId() : "null", isCreate);
        
        // 1. 上传文件到本地（使用框架的doUpload方法）
        Map<String, String> uploadedFileUrls = new HashMap<>();
        Map<String, UploadResult> uploadResultMap = null;
        
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            boolean hasFiles = (mreq.getFile(IMAGE_FILE_FIELD_NAME) != null && !mreq.getFile(IMAGE_FILE_FIELD_NAME).isEmpty())
                    || (mreq.getFiles("imageFiles") != null && !mreq.getFiles("imageFiles").isEmpty());

            if (hasFiles) {
                // 上传所有文件（本地存储）
                uploadResultMap = doUpload(request);
                if (uploadResultMap != null && !uploadResultMap.isEmpty()) {
                    String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
                            ? oFileProperties.getServerRoot().replaceAll("/+$", "") : "";
                    String storageRoot = oFileProperties != null && oFileProperties.getStorageRoot() != null
                            ? oFileProperties.getStorageRoot() : "";

                    // 处理所有上传的文件
                    for (UploadResult ur : uploadResultMap.values()) {
                        if (ur != null && ur.getRelativeFile() != null) {
                            processUploadedImageFile(ur, uploadedFileUrls, serverRoot, storageRoot, request);
                        }
                    }
                }
            }
        }

        // 2. 使用Service批量压缩并上传OSS（带重试）
        Map<String, com.acooly.showcase.shop.dto.ImageProcessResult> compressResults = new HashMap<>();
        if (productImageService != null && !uploadedFileUrls.isEmpty()) {
            // 将uploadedFileUrls传递给Service（已包含相对路径信息）
            compressResults = productImageService.processUploadedFiles(uploadedFileUrls, request);
        } else {
            log.warn("ProductImageService未注入，跳过批量压缩处理");
        }

        // 3. 收集上传的图片信息（确保URL是完整的，包含域名）
        String serverRoot = oFileProperties != null && oFileProperties.getServerRoot() != null
                ? oFileProperties.getServerRoot().replaceAll("/+$", "") : "";
        // 将相对路径转换为完整URL
        Map<String, String> fullUploadedFileUrls = new HashMap<>();
        for (Map.Entry<String, String> entry : uploadedFileUrls.entrySet()) {
            String relative = entry.getValue();
            if (relative != null && !relative.trim().isEmpty()) {
                // 如果已经是完整URL（包含http://或https://），则直接使用
                if (relative.startsWith("http://") || relative.startsWith("https://")) {
                    fullUploadedFileUrls.put(entry.getKey(), relative);
                } else {
                    // 否则，拼接serverRoot生成完整URL
                    String fullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;
                    fullUploadedFileUrls.put(entry.getKey(), fullUrl);
                }
            }
        }
        List<UploadedImageInfo> uploadedImages = collectUploadedImagesWithUrls(request, fullUploadedFileUrls);

        // 4. 收集已存在的图片信息（编辑时）
        List<ExistingImageInfo> existingImages = collectExistingImages(request, entity != null ? entity.getId() : null);

        // 5. 使用Service处理图片逻辑（合并、删除、主图）
        ProductImageService.ProductImageProcessResult processResult;
        if (productImageService != null) {
            processResult = productImageService.processProductImages(
                    entity != null ? entity.getId() : null,
                    uploadedImages,
                    existingImages,
                    compressResults,
                    request
            );
        } else {
            // 降级处理：使用原有方法
            log.warn("ProductImageService未注入，使用降级逻辑");
            processResult = processProductImagesFallback(
                    entity != null ? entity.getId() : null,
                    uploadedImages,
                    existingImages,
                    request
            );
        }

        long duration = System.currentTimeMillis() - startTime;
        if (processResult.getToSave().size() > 0 || processResult.getToDelete().size() > 0) {
            log.info("【图片处理】完成 - 商品ID: {}, 耗时: {}ms, 保存: {}条, 删除: {}条",
                    entity != null ? entity.getId() : "null", duration,
                    processResult.getToSave().size(), processResult.getToDelete().size());
        } else {
            log.debug("【图片处理】完成 - 商品ID: {}, 耗时: {}ms, 无变化",
                    entity != null ? entity.getId() : "null", duration);
        }
        
        return processResult;
    }

    /**
     * 【降级处理】同步主图URL（当PrimaryImageService未注入时使用）
     */
    private void syncPrimaryImageUrlFallback(ShopProducts entity,
                                            ProductImageService.ProductImageProcessResult processResult,
                                            String originalImageUrl,
                                            boolean isCreate,
                                            HttpServletRequest request) {
        // 使用原有逻辑
        if (isCreate) {
            if (processResult.getPrimaryImageUrl() != null) {
                entity.setImageUrl(processResult.getPrimaryImageUrl());
            } else {
                String imageUrlFromForm = request.getParameter(IMAGE_URL_FIELD_NAME);
                if (imageUrlFromForm != null && !imageUrlFromForm.trim().isEmpty()) {
                    entity.setImageUrl(imageUrlFromForm.trim());
                }
            }
        } else {
            if (entity.getId() != null) {
                ShopProductImages currentPrimary = shopProductImagesService.findPrimaryByProductId(entity.getId());
                String currentPrimaryImageUrl = currentPrimary != null ? currentPrimary.getImageUrl() : null;
                String newPrimaryImageUrl = processResult.getPrimaryImageUrl();

                if (newPrimaryImageUrl != null) {
                    String finalPrimaryImageUrl = determinePrimaryImageUrl(newPrimaryImageUrl, request);
                    if (currentPrimaryImageUrl == null || !currentPrimaryImageUrl.equals(finalPrimaryImageUrl)) {
                        entity.setImageUrl(finalPrimaryImageUrl);
                        processResult.setPrimaryImageChanged(true);
                    } else {
                        entity.setImageUrl(originalImageUrl);
                        processResult.setPrimaryImageChanged(false);
                    }
                } else {
                    entity.setImageUrl(originalImageUrl);
                    processResult.setPrimaryImageChanged(false);
                }
            }
        }
    }

    /**
     * 【降级处理】处理图片逻辑（当ProductImageService未注入时使用）
     */
    private ProductImageService.ProductImageProcessResult processProductImagesFallback(
            Long productId,
            List<UploadedImageInfo> uploadedImages,
            List<ExistingImageInfo> existingImages,
            HttpServletRequest request) throws Exception {
        
        // 转换为原有的ImageProcessResult格式
        ImageProcessResult oldResult = processProductImages(productId, uploadedImages, existingImages, request);
        
        // 转换为新的ProductImageProcessResult格式
        ProductImageService.ProductImageProcessResult newResult = new ProductImageService.ProductImageProcessResult();
        newResult.setToSave(oldResult.getToSave());
        newResult.setToDelete(oldResult.getToDelete());
        newResult.setPrimaryImageUrl(oldResult.getPrimaryImageUrl());
        newResult.setPrimaryImageChanged(oldResult.isPrimaryImageChanged());
        
        return newResult;
    }

    /**
     * 图片变化类型枚举
     */
    private enum ImageChangeType {
        NONE,           // 没有变化
        SORT_ONLY,      // 只有排序变化
        FULL_PROCESS    // 需要完整处理（上传、删除、主图变化等）
    }
    
    /**
     * 【性能优化】检查是否有图片相关变化
     * 如果没有变化，可以跳过图片处理，提升性能
     * 
     * @param request HTTP请求
     * @param entity 商品实体
     * @param isCreate 是否新增
     * @return ImageChangeType 表示变化类型
     */
    private ImageChangeType checkImageChanges(HttpServletRequest request, ShopProducts entity, boolean isCreate) {
        // 1. 检查是否有新文件上传
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            boolean hasFiles = (mreq.getFile(IMAGE_FILE_FIELD_NAME) != null && !mreq.getFile(IMAGE_FILE_FIELD_NAME).isEmpty())
                    || (mreq.getFiles("imageFiles") != null && !mreq.getFiles("imageFiles").isEmpty());
            if (hasFiles) {
                log.debug("检测到新文件上传，需要完整处理图片");
                return ImageChangeType.FULL_PROCESS;  // 有新文件上传，需要完整处理
            }
        }
        
        // 2. 检查是否有删除图片（编辑时）
        if (!isCreate && entity != null && entity.getId() != null) {
            String deletedImageIds = request.getParameter("deletedImageIds");
            if (deletedImageIds != null && !deletedImageIds.trim().isEmpty()) {
                log.debug("检测到删除图片参数，需要完整处理图片 - deletedImageIds: {}", deletedImageIds);
                return ImageChangeType.FULL_PROCESS;  // 有删除图片，需要完整处理
            }
            
            // 3. 检查是否有主图变化
            String primaryImageId = request.getParameter("primaryImageId");
            String primaryImageUrl = request.getParameter("primaryImageUrl");
            if ((primaryImageId != null && !primaryImageId.trim().isEmpty()) 
                    || (primaryImageUrl != null && !primaryImageUrl.trim().isEmpty())) {
                log.debug("检测到主图变化参数，需要完整处理图片 - primaryImageId: {}, primaryImageUrl: {}", 
                        primaryImageId, primaryImageUrl);
                return ImageChangeType.FULL_PROCESS;  // 有主图变化，需要完整处理
            }
            
            // 4. 【新增】检查是否有图片排序顺序变化（编辑时）
            String imageSortOrdersStr = request.getParameter("imageSortOrders");
            if (imageSortOrdersStr != null && !imageSortOrdersStr.trim().isEmpty()) {
                // 检查排序顺序是否与数据库中的不同
                List<ShopProductImages> dbImages = shopProductImagesService.findByProductId(entity.getId());
                if (dbImages != null && !dbImages.isEmpty()) {
                    // 解析前端传递的排序顺序
                    Map<Long, Integer> frontendSortOrderMap = new HashMap<>();
                    try {
                        String[] pairs = imageSortOrdersStr.split(",");
                        for (String pair : pairs) {
                            if (pair != null && !pair.trim().isEmpty()) {
                                String[] parts = pair.trim().split(":");
                                if (parts.length == 2) {
                                    Long imageId = Long.parseLong(parts[0].trim());
                                    Integer sortOrder = Integer.parseInt(parts[1].trim());
                                    frontendSortOrderMap.put(imageId, sortOrder);
                                }
                            }
                        }
                        
                        // 检查是否有排序变化：比较前端排序与数据库排序
                        boolean hasSortChange = false;
                        for (ShopProductImages dbImg : dbImages) {
                            Integer frontendSort = frontendSortOrderMap.get(dbImg.getId());
                            Integer dbSort = dbImg.getSortOrder();
                            if (frontendSort != null && !frontendSort.equals(dbSort)) {
                                hasSortChange = true;
                                log.debug("检测到图片排序变化 - 图片ID: {}, 数据库排序: {}, 前端排序: {}", 
                                        dbImg.getId(), dbSort, frontendSort);
                                break;
                            }
                        }
                        
                        if (hasSortChange) {
                            log.debug("检测到图片排序顺序变化，仅更新排序 - imageSortOrders: {}", imageSortOrdersStr);
                            return ImageChangeType.SORT_ONLY;  // 只有排序变化，仅更新排序
                        }
                    } catch (Exception e) {
                        log.warn("解析imageSortOrders失败，但继续处理: {}", imageSortOrdersStr, e);
                        // 如果解析失败，为了安全起见，认为需要完整处理
                        return ImageChangeType.FULL_PROCESS;
                    }
                }
            }
        }
        
        // 5. 新增商品时，如果没有新文件上传，也没有外部URL，则不需要处理图片
        if (isCreate) {
            String imageUrlFromForm = request.getParameter(IMAGE_URL_FIELD_NAME);
            if (imageUrlFromForm != null && !imageUrlFromForm.trim().isEmpty()) {
                log.debug("新增商品，检测到外部图片URL，需要完整处理图片");
                return ImageChangeType.FULL_PROCESS;  // 有外部URL，需要完整处理
            }
        }
        
        // 6. 没有图片相关变化
        log.debug("没有检测到图片相关变化，可以跳过图片处理");
        return ImageChangeType.NONE;
    }
    
    /**
     * 【性能优化】仅更新图片排序顺序（不执行文件上传等操作）
     * 
     * @param request HTTP请求
     * @param productId 商品ID
     */
    private void updateImageSortOrderOnly(HttpServletRequest request, Long productId) {
        if (productId == null) {
            log.warn("商品ID为null，无法更新图片排序");
            return;
        }
        
        String imageSortOrdersStr = request.getParameter("imageSortOrders");
        if (imageSortOrdersStr == null || imageSortOrdersStr.trim().isEmpty()) {
            log.debug("imageSortOrders参数为空，跳过排序更新");
            return;
        }
        
        try {
            // 解析前端传递的排序顺序
            Map<Long, Integer> sortOrderMap = new HashMap<>();
            String[] pairs = imageSortOrdersStr.split(",");
            for (String pair : pairs) {
                if (pair != null && !pair.trim().isEmpty()) {
                    String[] parts = pair.trim().split(":");
                    if (parts.length == 2) {
                        Long imageId = Long.parseLong(parts[0].trim());
                        Integer sortOrder = Integer.parseInt(parts[1].trim());
                        sortOrderMap.put(imageId, sortOrder);
                    }
                }
            }
            
            if (sortOrderMap.isEmpty()) {
                log.debug("解析后的排序映射为空，跳过排序更新");
                return;
            }
            
            // 获取所有图片
            List<ShopProductImages> dbImages = shopProductImagesService.findByProductId(productId);
            if (dbImages == null || dbImages.isEmpty()) {
                log.debug("商品没有图片，跳过排序更新");
                return;
            }
            
            // 更新排序顺序
            int updateCount = 0;
            for (ShopProductImages img : dbImages) {
                if (img.getId() != null && sortOrderMap.containsKey(img.getId())) {
                    Integer newSortOrder = sortOrderMap.get(img.getId());
                    if (img.getSortOrder() == null || !img.getSortOrder().equals(newSortOrder)) {
                        img.setSortOrder(newSortOrder);
                        shopProductImagesService.update(img);
                        updateCount++;
                        log.debug("更新图片排序 - 图片ID: {}, 新排序: {}", img.getId(), newSortOrder);
                    }
                }
            }
            
            log.info("图片排序更新完成 - 商品ID: {}, 更新数量: {}", productId, updateCount);
        } catch (Exception e) {
            log.error("更新图片排序失败 - 商品ID: {}", productId, e);
            // 不抛出异常，避免影响商品保存流程
        }
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
     * 收集上传的图片文件（包含上传后的URL）
     * 支持单文件和多文件两种方式，统一转换为列表
     */
    private List<UploadedImageInfo> collectUploadedImagesWithUrls(HttpServletRequest request, Map<String, String> uploadedFileUrls) {
        List<UploadedImageInfo> images = new ArrayList<>();
        
        if (!(request instanceof MultipartHttpServletRequest)) {
            return images;
        }
        
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
        
        // 方式1：多文件上传（imageFiles[]）
        List<MultipartFile> multipleFiles = mreq.getFiles("imageFiles");
        if (multipleFiles != null && !multipleFiles.isEmpty()) {
            for (int i = 0; i < multipleFiles.size(); i++) {
                MultipartFile file = multipleFiles.get(i);
                if (file != null && !file.isEmpty()) {
                    UploadedImageInfo info = new UploadedImageInfo();
                    info.setFile(file);
                    // 从上传结果中获取URL（可能key为imageFiles或imageFiles0等）
                    String imageUrl = findUploadedUrl(uploadedFileUrls, "imageFiles", i);
                    info.setImageUrl(imageUrl);
                    info.setSortOrder(i + 1);  // 默认排序
                    // 重要：不从后端默认设置主图，应该由前端明确指定，或者根据业务逻辑判断
                    // 默认设为false，只有在没有主图的情况下才会被设为主图
                    info.setIsPrimary(false);  // 默认非主图，由processProductImages根据实际情况决定
                    images.add(info);
                }
            }
        }
        
        // 方式2：单文件上传（imageFile）- 兼容旧逻辑
        if (images.isEmpty()) {
            MultipartFile singleFile = mreq.getFile(IMAGE_FILE_FIELD_NAME);
            if (singleFile != null && !singleFile.isEmpty()) {
                UploadedImageInfo info = new UploadedImageInfo();
                info.setFile(singleFile);
                // 从上传结果中获取URL
                String imageUrl = findUploadedUrl(uploadedFileUrls, IMAGE_FILE_FIELD_NAME, 0);
                info.setImageUrl(imageUrl);
                info.setSortOrder(1);
                // 重要：单文件上传时，也默认设为false，只有在没有主图的情况下才会被设为主图
                info.setIsPrimary(false);  // 默认非主图，由processProductImages根据实际情况决定
                images.add(info);
            }
        }
        
        return images;
    }

    /**
     * 从上传结果中查找文件URL
     */
    private String findUploadedUrl(Map<String, String> uploadedFileUrls, String fieldName, int index) {
        // 尝试多种可能的key
        String[] possibleKeys = {
            fieldName,
            fieldName + index,
            fieldName + "[" + index + "]"
        };
        
        for (String key : possibleKeys) {
            if (uploadedFileUrls.containsKey(key)) {
                return uploadedFileUrls.get(key);
            }
        }
        
        // 如果没找到精确匹配，尝试匹配前缀
        for (Map.Entry<String, String> entry : uploadedFileUrls.entrySet()) {
            if (entry.getKey() != null && entry.getKey().startsWith(fieldName)) {
                return entry.getValue();
            }
        }
        
        // 如果还是没找到，返回第一个（兜底）
        if (!uploadedFileUrls.isEmpty()) {
            return uploadedFileUrls.values().iterator().next();
        }
        
        return null;
    }

    /**
     * 收集已存在的图片信息（编辑时）
     * 同时处理用户指定的主图
     */
    private List<ExistingImageInfo> collectExistingImages(HttpServletRequest request, Long productId) {
        List<ExistingImageInfo> existing = new ArrayList<>();
        
        if (productId == null) {
            return existing;
        }
        
        // 从数据库加载已有图片
        List<ShopProductImages> dbImages = shopProductImagesService.findByProductId(productId);
        
        // 检查前端是否指定了要删除的图片ID
        String deleteImageIdsStr = request.getParameter("deletedImageIds");
        Set<Long> deleteImageIds = new HashSet<>();
        if (deleteImageIdsStr != null && !deleteImageIdsStr.trim().isEmpty()) {
            try {
                String[] ids = deleteImageIdsStr.split(",");
                for (String id : ids) {
                    if (!id.trim().isEmpty()) {
                        deleteImageIds.add(Long.parseLong(id.trim()));
                    }
                }
                log.debug("【删除图片】解析deletedImageIds成功 - 删除图片ID列表: {}", deleteImageIds);
            } catch (Exception e) {
                log.warn("解析deletedImageIds失败: {}", deleteImageIdsStr, e);
            }
        } else {
            log.debug("【删除图片】deletedImageIds参数为空或未发送");
        }
        
        // 检查前端是否指定了要保留的已存在图片ID（重要：前端明确告诉后端哪些图片是保留的）
        String existingImageIdsStr = request.getParameter("existingImageIds");
        Set<Long> existingImageIds = new HashSet<>();
        if (existingImageIdsStr != null && !existingImageIdsStr.trim().isEmpty()) {
            try {
                String[] ids = existingImageIdsStr.split(",");
                for (String id : ids) {
                    if (!id.trim().isEmpty()) {
                        existingImageIds.add(Long.parseLong(id.trim()));
                    }
                }
                log.debug("【删除图片】解析existingImageIds成功 - 保留图片ID列表: {}", existingImageIds);
            } catch (Exception e) {
                log.warn("解析existingImageIds失败: {}", existingImageIdsStr, e);
            }
        } else {
            log.debug("【删除图片】existingImageIds参数为空或未发送，将使用deletedImageIds判断（向后兼容模式）");
        }
        
        // 检查前端是否指定了主图ID（用户主动设置已存在图片为主图）
        String primaryImageIdStr = request.getParameter("primaryImageId");
        Long primaryImageId = null;
        if (primaryImageIdStr != null && !primaryImageIdStr.trim().isEmpty()) {
            try {
                primaryImageId = Long.parseLong(primaryImageIdStr.trim());
                log.debug("前端指定主图ID: {}", primaryImageId);
            } catch (Exception e) {
                log.warn("解析primaryImageId失败: {}", primaryImageIdStr, e);
            }
        }
        
        // 【新增】检查前端是否指定了图片排序顺序（格式：imageId:sortOrder,imageId:sortOrder）
        String imageSortOrdersStr = request.getParameter("imageSortOrders");
        Map<Long, Integer> imageSortOrderMap = new HashMap<>();
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
                log.debug("【图片排序】解析imageSortOrders成功 - 排序映射: {}", imageSortOrderMap);
            } catch (Exception e) {
                log.warn("解析imageSortOrders失败: {}", imageSortOrdersStr, e);
            }
        } else {
            log.debug("【图片排序】imageSortOrders参数为空或未发送");
        }
        
        // 【修复】构建已存在图片信息
        // 先构建所有图片信息，然后再统一处理主图逻辑（确保清除旧主图标记）
        for (ShopProductImages img : dbImages) {
            ExistingImageInfo info = new ExistingImageInfo();
            info.setImageId(img.getId());
            info.setImageUrl(img.getImageUrl());
            // 【新增】如果前端指定了新的排序顺序，使用前端的排序；否则使用数据库中的排序
            if (imageSortOrderMap.containsKey(img.getId())) {
                info.setSortOrder(imageSortOrderMap.get(img.getId()));
                log.debug("【图片排序】使用前端指定的排序顺序 - 图片ID: {}, 新排序: {}", img.getId(), imageSortOrderMap.get(img.getId()));
            } else {
                info.setSortOrder(img.getSortOrder());
                log.debug("【图片排序】使用数据库中的排序顺序 - 图片ID: {}, 排序: {}", img.getId(), img.getSortOrder());
            }
            
            // 判断是否保留：
            // 1. 如果前端明确指定了existingImageIds，则只保留在这个列表中的图片
            // 2. 如果前端没有指定existingImageIds，则使用deletedImageIds判断（向后兼容）
            boolean shouldKeep;
            if (!existingImageIds.isEmpty()) {
                // 前端明确指定了要保留的图片ID
                shouldKeep = existingImageIds.contains(img.getId()) && !deleteImageIds.contains(img.getId());
                log.debug("图片保留判断 - 图片ID: {}, existingImageIds包含: {}, deletedImageIds包含: {}, 结果: {}", 
                        img.getId(), existingImageIds.contains(img.getId()), deleteImageIds.contains(img.getId()), shouldKeep);
            } else {
                // 向后兼容：如果没有发送existingImageIds，使用deletedImageIds判断
                shouldKeep = !deleteImageIds.contains(img.getId());
                log.debug("图片保留判断（向后兼容模式） - 图片ID: {}, deletedImageIds包含: {}, 结果: {}", 
                        img.getId(), deleteImageIds.contains(img.getId()), shouldKeep);
            }
            
            info.setKeep(shouldKeep);
            
            // 重要：判断是否为主图
            // 1. 如果前端指定了primaryImageId，且当前图片ID匹配，则设为主图
            // 2. 否则，使用数据库中的isPrimary值（但如果图片被删除，则不是主图）
            if (primaryImageId != null && primaryImageId.equals(img.getId()) && shouldKeep) {
                // 用户主动设置这张图片为主图
                info.setIsPrimary(1);
                log.debug("用户设置已存在图片为主图 - 图片ID: {}, URL: {}", img.getId(), img.getImageUrl());
            } else if (shouldKeep) {
                // 保留原有主图状态（如果图片被保留）
                // 【修复】如果用户指定了primaryImageId，但当前图片不是指定的主图，则清除主图标记
                if (primaryImageId != null) {
                    // 用户指定了主图，但当前图片不是指定的主图，清除主图标记
                    info.setIsPrimary(0);
                    log.debug("清除旧主图标记 - 图片ID: {}, 新主图ID: {}", img.getId(), primaryImageId);
                } else {
                    // 用户没有指定主图，保持原有状态
                    info.setIsPrimary(img.getIsPrimary() != null && img.getIsPrimary() == 1 ? 1 : 0);
                }
            } else {
                // 图片被删除，不是主图
                info.setIsPrimary(0);
            }
            
            existing.add(info);
        }
        
        long keepCount = existing.stream().filter(ExistingImageInfo::getKeep).count();
        long deleteCount = existing.stream().filter(img -> !img.getKeep()).count();
        log.debug("收集已存在图片 - 总数: {}, 保留: {}, 删除: {}", 
                existing.size(), keepCount, deleteCount);
        
        // 【修复】输出详细信息，便于调试
        if (log.isDebugEnabled()) {
            for (ExistingImageInfo info : existing) {
                log.debug("图片信息 - ID: {}, URL: {}, 保留: {}, 主图: {}", 
                        info.getImageId(), info.getImageUrl(), info.getKeep(), info.getIsPrimary());
            }
        }
        
        return existing;
    }

    /**
     * 处理商品图片：合并新上传的图片和已存在的图片
     */
    private ImageProcessResult processProductImages(Long productId,
                                                    List<UploadedImageInfo> uploadedImages,
                                                    List<ExistingImageInfo> existingImages,
                                                    HttpServletRequest request) throws Exception {
        
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
        
        ImageProcessResult result = new ImageProcessResult();
        List<ShopProductImages> toSave = new ArrayList<>();
        List<Long> toDelete = new ArrayList<>();
        String primaryImageUrl = null;
        result.setPrimaryImageChanged(false);  // 默认主图未变化
        
        // 1. 先处理已存在的图片（编辑时），确定保留的图片和要删除的图片
        // 同时找出当前的主图（如果保留的话）
        String existingPrimaryImageUrl = null;
        Long originalPrimaryImageId = null;  // 【修复】记录数据库中的原始主图ID（用于判断主图是否变化）
        boolean isPrimaryDeleted = false;  // 标记是否删除了主图
        int maxSortOrder = 0;
        
        // 【修复】查询数据库获取原始主图ID（用于判断主图是否变化）
        if (productId != null) {
            ShopProductImages originalPrimary = shopProductImagesService.findPrimaryByProductId(productId);
            if (originalPrimary != null) {
                originalPrimaryImageId = originalPrimary.getId();
                log.debug("查询到原始主图ID: {}", originalPrimaryImageId);
            }
        }
        
        for (ExistingImageInfo existing : existingImages) {
            if (existing.getKeep()) {
                // 保留的图片
                ShopProductImages imageEntity = new ShopProductImages();
                imageEntity.setId(existing.getImageId());  // 有ID表示更新
                imageEntity.setProductId(productId);
                imageEntity.setImageUrl(existing.getImageUrl());
                imageEntity.setSortOrder(existing.getSortOrder());
                imageEntity.setIsPrimary(existing.getIsPrimary());
                
                toSave.add(imageEntity);
                
                // 记录当前主图URL（如果保留的主图）
                if (existing.getIsPrimary() == 1) {
                    existingPrimaryImageUrl = existing.getImageUrl();
                }
                
                // 记录最大排序号
                if (existing.getSortOrder() != null && existing.getSortOrder() > maxSortOrder) {
                    maxSortOrder = existing.getSortOrder();
                }
            } else {
                // 标记删除
                toDelete.add(existing.getImageId());
                log.info("【删除图片】标记删除图片 - 图片ID: {}, URL: {}", existing.getImageId(), existing.getImageUrl());
                // 检查是否删除了主图
                if (existing.getImageId() != null && existing.getImageId().equals(originalPrimaryImageId)) {
                    isPrimaryDeleted = true;
                    log.info("【删除图片】检测到删除主图 - 图片ID: {}", existing.getImageId());
                }
            }
        }
        
        // 2. 处理新上传的图片
        int nextSortOrder = maxSortOrder + 1;  // 新图片的排序从已有图片之后开始
        boolean hasNewPrimary = false;
        
        for (UploadedImageInfo uploaded : uploadedImages) {
            // 使用上传后的URL（已在collectUploadedImagesWithUrls中设置）
            String imageUrl = uploaded.getImageUrl();
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                continue;  // 跳过无效的图片
            }
            
            // 创建图片记录
            ShopProductImages imageEntity = new ShopProductImages();
            imageEntity.setProductId(productId);  // 如果是新增，这里可能是null，稍后设置
            imageEntity.setImageUrl(imageUrl);
            imageEntity.setSortOrder(uploaded.getSortOrder() != null ? uploaded.getSortOrder() : nextSortOrder++);
            
            // 【新增】从request attribute获取压缩结果，设置WebP URL和元数据
            com.acooly.showcase.shop.dto.ImageProcessResult compressResult = null;
            if (imageUrl != null) {
                compressResult = (com.acooly.showcase.shop.dto.ImageProcessResult) request.getAttribute("_imageCompressResult_" + imageUrl);
            }
            
            if (compressResult != null && compressResult.isSuccess()) {
                // 【优化】设置WebP URL（只使用OSS URL，不再保存本地路径）
                imageEntity.setWebpUrl(compressResult.getWebpOssUrl());
                imageEntity.setThumbWebpUrl(compressResult.getThumbWebpOssUrl());
                
                // 元数据
                imageEntity.setFileSize(compressResult.getOriginalSize());
                imageEntity.setWebpSize(compressResult.getWebpSize());
                imageEntity.setThumbSize(compressResult.getThumbWebpSize());
                imageEntity.setWidth(compressResult.getOriginalWidth());
                imageEntity.setHeight(compressResult.getOriginalHeight());
                imageEntity.setThumbWidth(compressResult.getThumbWidth());
                imageEntity.setThumbHeight(compressResult.getThumbHeight());
                imageEntity.setFormat(compressResult.getFormat());
                
                log.debug("设置图片WebP信息 - URL: {}, WebP OSS: {}, 缩略图OSS: {}",
                        imageUrl, imageEntity.getWebpUrl(), imageEntity.getThumbWebpUrl());
            }
            String primaryImageFileName = request.getParameter("primaryImageUrl");
            boolean isPrimaryByFileName = false;
            if (primaryImageFileName != null && !primaryImageFileName.trim().isEmpty() && uploaded.getFile() != null) {
                // 前端指定了主图文件名，检查是否匹配当前上传的文件
                String uploadedFileName = uploaded.getFile().getOriginalFilename();
                if (uploadedFileName != null && uploadedFileName.equals(primaryImageFileName.trim())) {
                    isPrimaryByFileName = true;
                    log.info("前端指定新上传图片为主图（通过文件名匹配） - 文件名: {}", uploadedFileName);
                }
            }
            
            boolean userSpecifiedPrimary = uploaded.getIsPrimary() != null && uploaded.getIsPrimary();
            boolean shouldBePrimary = false;
            
            if (isPrimaryByFileName || userSpecifiedPrimary) {
                // 用户明确指定为主图（通过文件名或isPrimary标记）
                shouldBePrimary = true;
            } else if (existingPrimaryImageUrl == null && !hasNewPrimary) {
                // 当前没有主图，且还没有设置新主图，则第一张新图片设为主图（新增商品时）
                shouldBePrimary = true;
            } else {
                // 已有主图，或已经设置了新主图，新图片默认为非主图
                shouldBePrimary = false;
            }
            
            if (shouldBePrimary) {
                // 设为主图：清除其他主图标记
                for (ShopProductImages img : toSave) {
                    if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                        img.setIsPrimary(0);  // 清除旧主图标记
                    }
                }
                imageEntity.setIsPrimary(1);
                primaryImageUrl = imageUrl;  // 新主图URL
                hasNewPrimary = true;
                // 如果新上传的图片被设为主图，且原有主图存在，则主图发生了变化
                if (existingPrimaryImageUrl != null && !existingPrimaryImageUrl.equals(imageUrl)) {
                    result.setPrimaryImageChanged(true);
                    log.info("用户指定新图片为主图，主图发生变化 - 商品ID: {}, 旧主图: {}, 新主图: {}", 
                            productId, existingPrimaryImageUrl, imageUrl);
                } else if (existingPrimaryImageUrl == null) {
                    // 原来没有主图，现在有了，也算变化（新增商品时）
                    result.setPrimaryImageChanged(true);
                    log.info("新增商品，设置第一张图片为主图 - 商品ID: {}, 主图: {}", productId, imageUrl);
                }
            } else {
                // 非主图
                imageEntity.setIsPrimary(0);
                log.debug("新图片设为非主图 - 商品ID: {}, 图片URL: {}, 当前主图: {}", 
                        productId, imageUrl, existingPrimaryImageUrl);
            }
            
            toSave.add(imageEntity);
        }
        
        // 3. 【修复】如果没有新主图，使用保留的主图，但需要检查主图是否真的变化了
        if (!hasNewPrimary) {
            // 找出toSave中的主图
            ShopProductImages newPrimaryInToSave = null;
            for (ShopProductImages img : toSave) {
                if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                    newPrimaryInToSave = img;
                    break;
                }
            }
            
            if (newPrimaryInToSave != null) {
                // 有主图
                primaryImageUrl = newPrimaryInToSave.getImageUrl();
                // 【修复】检查主图是否真的变化了：对比新主图ID与原始主图ID
                if (originalPrimaryImageId != null && newPrimaryInToSave.getId() != null) {
                    if (!originalPrimaryImageId.equals(newPrimaryInToSave.getId())) {
                        // 主图ID不同，说明主图发生了变化
                        result.setPrimaryImageChanged(true);
                        log.info("检测到主图变化 - 原始主图ID: {}, 新主图ID: {}", originalPrimaryImageId, newPrimaryInToSave.getId());
                    } else {
                        // 主图ID相同，说明主图没有变化
                        result.setPrimaryImageChanged(false);
                        log.debug("主图未变化 - 主图ID: {}", originalPrimaryImageId);
                    }
                } else if (originalPrimaryImageId == null && newPrimaryInToSave.getId() != null) {
                    // 原来没有主图，现在有了，算变化
                    result.setPrimaryImageChanged(true);
                    log.info("检测到主图变化 - 原来没有主图，现在设置主图ID: {}", newPrimaryInToSave.getId());
                } else {
                    // 其他情况，保持原有逻辑
                    if (existingPrimaryImageUrl != null && existingPrimaryImageUrl.equals(primaryImageUrl)) {
                        result.setPrimaryImageChanged(false);
                    } else {
                        result.setPrimaryImageChanged(true);
                    }
                }
            } else if (existingPrimaryImageUrl != null) {
                // 没有找到主图，但existingPrimaryImageUrl不为空（不应该发生）
                primaryImageUrl = existingPrimaryImageUrl;
                result.setPrimaryImageChanged(false);
                log.warn("未找到主图，但existingPrimaryImageUrl不为空 - URL: {}", existingPrimaryImageUrl);
            }
        }
        
        // 4. 如果删除了主图，需要从保留的图片中选择新的主图
        if (isPrimaryDeleted && !hasNewPrimary && existingPrimaryImageUrl == null && !toSave.isEmpty()) {
            // 删除主图后，从保留的图片中选择第一张作为新主图
            ShopProductImages firstImage = toSave.get(0);
            firstImage.setIsPrimary(1);
            primaryImageUrl = firstImage.getImageUrl();
            result.setPrimaryImageChanged(true);  // 主图发生了变化（从旧主图变为新主图）
            log.info("删除主图后，自动设置新主图 - 图片ID: {}, URL: {}", firstImage.getId(), primaryImageUrl);
        }
        
        // 5. 确保至少有一张主图（如果所有图片都被删除，这里会是空的）
        if (primaryImageUrl == null && !toSave.isEmpty()) {
            // 如果没有主图，将第一张设为主图
            ShopProductImages firstImage = toSave.get(0);
            firstImage.setIsPrimary(1);
            primaryImageUrl = firstImage.getImageUrl();
            // 只有在新增商品时（productId为null）才标记为主图变化
            // 编辑商品时，如果原来没有主图，现在有了，也算变化
            if (productId == null || existingPrimaryImageUrl == null) {
                result.setPrimaryImageChanged(true);
            }
            log.info("自动设置主图 - 图片ID: {}, URL: {}", firstImage.getId(), primaryImageUrl);
        }
        
        // 6. 确保只有一个主图（如果有多个主图，只保留第一个）
        boolean foundPrimary = false;
        for (ShopProductImages img : toSave) {
            if (img.getIsPrimary() != null && img.getIsPrimary() == 1) {
                if (foundPrimary) {
                    // 已经有主图了，这个改为非主图
                    img.setIsPrimary(0);
                    log.warn("检测到多个主图，将图片ID {} 改为非主图", img.getId());
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
        
        // 【修复】输出处理结果，便于调试
        log.info("图片处理结果 - 商品ID: {}, 保存: {} 条, 删除: {} 条, 主图URL: {}, 主图变化: {}", 
                productId, toSave.size(), toDelete.size(), primaryImageUrl, result.isPrimaryImageChanged());
        if (log.isDebugEnabled()) {
            log.debug("要保存的图片ID: {}", toSave.stream().map(img -> img.getId()).collect(java.util.stream.Collectors.toList()));
            log.debug("要删除的图片ID: {}", toDelete);
        }
        
        return result;
    }


    /**
     * 【降级处理】保存图片记录（当ProductImageService未注入时使用）
     */
    private void saveProductImagesFallback(Long productId, ProductImageService.ProductImageProcessResult processResult) {
        if (productId == null) {
            log.warn("productId为null，无法保存图片");
            return;
        }
        
        // 1. 删除标记的图片
        if (processResult.getToDelete() != null && !processResult.getToDelete().isEmpty()) {
            log.info("【删除图片】开始删除图片记录 - 商品ID: {}, 要删除的图片ID: {}", productId, processResult.getToDelete());
            for (Long imageId : processResult.getToDelete()) {
                try {
                    shopProductImagesService.removeById(imageId);
                    log.info("【删除图片】成功删除图片 - 图片ID: {}", imageId);
                } catch (Exception e) {
                    log.error("【删除图片】删除图片失败 - 图片ID: {}", imageId, e);
                }
            }
            log.info("【删除图片】删除图片记录完成 - 商品ID: {}, 删除数量: {}", productId, processResult.getToDelete().size());
        } else {
            log.debug("【删除图片】没有需要删除的图片记录");
        }
        
        // 2. 保存/更新图片记录
        if (processResult.getToSave() != null && !processResult.getToSave().isEmpty()) {
            for (ShopProductImages image : processResult.getToSave()) {
                image.setProductId(productId);  // 确保productId正确
                
                if (image.getId() != null) {
                    // 更新
                    shopProductImagesService.update(image);
                } else {
                    // 新增
                    shopProductImagesService.save(image);
                }
            }
            log.debug("保存图片记录: {} 条", processResult.getToSave().size());
        }
    }

    /**
     * 保存商品属性关联
     */
    private void saveProductAttributes(Long productId, String attrValueIdsStr) {
        log.debug("【商品属性保存】saveProductAttributes - 开始处理，商品ID: {}, 属性值ID字符串: {}", 
                productId, attrValueIdsStr != null ? attrValueIdsStr : "null");
        
        if (productId == null) {
            log.warn("【商品属性保存】saveProductAttributes - 商品ID为null，跳过保存");
            return;
        }

        // 1. 先删除该商品的所有现有属性关联
        shopProductAttrService.deleteByProductId(productId);
        log.debug("【商品属性保存】saveProductAttributes - 已删除商品所有现有属性关联，商品ID: {}", productId);

        // 2. 如果有新的属性值，保存新的关联
        if (attrValueIdsStr != null && !attrValueIdsStr.trim().isEmpty()) {
            String[] attrValueIds = attrValueIdsStr.split(",");
            int savedCount = 0;
            for (String attrValueIdStr : attrValueIds) {
                if (attrValueIdStr != null && !attrValueIdStr.trim().isEmpty()) {
                    try {
                        Long attrValueId = Long.parseLong(attrValueIdStr.trim());
                        ShopProductAttr productAttr = new ShopProductAttr();
                        productAttr.setProductId(productId);
                        productAttr.setAttrValueId(attrValueId);
                        shopProductAttrService.save(productAttr);
                        savedCount++;
                        log.debug("【商品属性保存】保存商品属性关联 - 商品ID: {}, 属性值ID: {}", productId, attrValueId);
                    } catch (NumberFormatException e) {
                        log.warn("【商品属性保存】无效的属性值ID: {}", attrValueIdStr, e);
                    }
                }
            }
            log.info("【商品属性保存】完成 - 商品ID: {}, 属性值数量: {}, 成功保存: {}", 
                    productId, attrValueIds.length, savedCount);
        } else {
            log.debug("【商品属性保存】没有属性值，已清空商品属性关联 - 商品ID: {}", productId);
        }
    }

    /**
     * 获取商品的所有图片
     */
    @RequestMapping("getProductImages")
    @ResponseBody
    public JsonListResult<ShopProductImages> getProductImages(Long productId) {
        JsonListResult<ShopProductImages> result = new JsonListResult<>();
        try {
            List<ShopProductImages> images = shopProductImagesService.findByProductId(productId);
            result.setRows(images);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("获取图片失败：" + e.getMessage());
            log.error("获取商品图片失败", e);
        }
        return result;
    }

    /**
     * 获取商品的所有属性（用于编辑页面回显）
     */
    @RequestMapping("getProductAttributes")
    @ResponseBody
    public JsonListResult<Map<String, Object>> getProductAttributes(Long productId) {
        JsonListResult<Map<String, Object>> result = new JsonListResult<>();
        try {
            List<ShopProductAttr> productAttrs = shopProductAttrService.findByProductId(productId);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (ShopProductAttr productAttr : productAttrs) {
                // 查询属性值，获取属性ID
                ShopAttrValue attrValue = shopAttrValueService.get(productAttr.getAttrValueId());
                if (attrValue != null) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("attrId", attrValue.getAttrId());
                    row.put("attrValueId", productAttr.getAttrValueId());
                    rows.add(row);
                }
            }
            result.setRows(rows);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("获取商品属性失败：" + e.getMessage());
            log.error("获取商品属性失败", e);
        }
        return result;
    }

    /**
     * 返回翻译管理弹窗视图
     * 注意：为了性能考虑，这里只传递基本信息，详细数据通过 AJAX 加载
     */
    @RequestMapping("translationDialog")
    public String translationDialog(HttpServletRequest request, Model model) {
        String productIdStr = request.getParameter("productId");
        if (productIdStr != null && !productIdStr.trim().isEmpty()) {
            try {
                Long productId = Long.parseLong(productIdStr.trim());
                ShopProducts product = getEntityService().get(productId);
                if (product != null) {
                    // 只传递基本信息，避免在模板中处理大量数据
                    model.addAttribute("productId", productId);
                    // 商品名称可能包含特殊字符，需要转义，但这里只传递基本信息
                    String productName = product.getName();
                    if (productName != null && productName.length() > 100) {
                        // 如果名称太长，截断（避免模板处理时的问题）
                        productName = productName.substring(0, 100) + "...";
                    }
                    model.addAttribute("productName", productName != null ? productName : "");
                } else {
                    model.addAttribute("productId", 0);
                    model.addAttribute("productName", "");
                    log.warn("商品不存在，productId: {}", productId);
                }
            } catch (NumberFormatException e) {
                log.warn("无效的商品ID: {}", productIdStr);
                model.addAttribute("productId", 0);
                model.addAttribute("productName", "");
            }
        } else {
            model.addAttribute("productId", 0);
            model.addAttribute("productName", "");
        }
        // 不再传递 locales 数据，由前端通过 AJAX 加载
        return "manage/shop/shopProductsTranslationDialog";
    }

    /**
     * 获取支持的语言列表（用于下拉选择）
     */
    @RequestMapping("getSupportedLocales")
    @ResponseBody
    public JsonListResult<ShopI18nLocales> getSupportedLocales() {
        JsonListResult<ShopI18nLocales> result = new JsonListResult<>();
        try {
            List<ShopI18nLocales> locales = shopI18nLocalesService.getActiveLocales();
            result.setRows(locales);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("获取支持的语言列表失败：" + e.getMessage());
            log.error("获取支持的语言列表失败", e);
        }
        return result;
    }

    /**
     * 获取商品的多语言翻译（弹窗打开时调用）
     * 一次性加载该商品的所有翻译，避免多次请求
     */
    @RequestMapping("getProductTranslations")
    @ResponseBody
    public JsonEntityResult<ProductTranslationVO> getProductTranslations(Long productId) {
        JsonEntityResult<ProductTranslationVO> result = new JsonEntityResult<>();
        try {
            if (productId == null) {
                result.setSuccess(false);
                result.setMessage("商品ID不能为空");
                return result;
            }

            // 获取商品信息
            ShopProducts product = getEntityService().get(productId);
            if (product == null) {
                result.setSuccess(false);
                result.setMessage("商品不存在");
                return result;
            }

            // 获取所有启用的语言
            List<ShopI18nLocales> activeLocales = shopI18nLocalesService.getActiveLocales();
            if (activeLocales == null || activeLocales.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("没有启用的语言");
                return result;
            }

            // 获取商品的所有翻译
            List<ShopI18nTranslations> translations = shopI18nTranslationsService.findByProduct(productId);

            // 构建翻译Map：locale -> fieldName -> translation
            Map<String, Map<String, String>> translationMap = new HashMap<>();
            if (translations != null) {
                for (ShopI18nTranslations t : translations) {
                    translationMap.computeIfAbsent(t.getLocale(), k -> new HashMap<>())
                            .put(t.getFieldName(), t.getTranslation());
                }
            }

            // 获取默认语言
            ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
            String defaultLocaleCode = defaultLocale != null ? defaultLocale.getLocaleCode() : null;

            // 构建 LocaleTranslationVO 列表
            List<LocaleTranslationVO> localeVOs = new ArrayList<>();
            for (ShopI18nLocales locale : activeLocales) {
                LocaleTranslationVO localeVO = new LocaleTranslationVO();
                localeVO.setLocaleCode(locale.getLocaleCode());
                localeVO.setLocaleName(locale.getLocaleName());
                localeVO.setIsDefault(locale.getLocaleCode().equals(defaultLocaleCode));

                // 获取该语言的翻译
                Map<String, String> localeTranslations = translationMap.getOrDefault(locale.getLocaleCode(), new HashMap<>());
                localeVO.setTranslations(localeTranslations);

                // 判断是否有翻译
                localeVO.setHasName(localeTranslations.containsKey("name") && 
                        localeTranslations.get("name") != null && 
                        !localeTranslations.get("name").trim().isEmpty());
                localeVO.setHasDescription(localeTranslations.containsKey("description") && 
                        localeTranslations.get("description") != null && 
                        !localeTranslations.get("description").trim().isEmpty());

                localeVOs.add(localeVO);
            }

            // 构建 ProductTranslationVO
            ProductTranslationVO vo = new ProductTranslationVO();
            vo.setProductId(productId);
            vo.setProductName(product.getName());
            vo.setLocales(localeVOs);

            result.setEntity(vo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("获取商品翻译失败：" + e.getMessage());
            log.error("获取商品翻译失败", e);
        }
        return result;
    }

    /**
     * 保存单个翻译（实时保存，无需刷新整个列表）
     */
    @RequestMapping("saveProductTranslation")
    @ResponseBody
    public JsonEntityResult<ShopI18nTranslations> saveProductTranslation(
            @RequestParam Long productId,
            @RequestParam String fieldName,    // name 或 description
            @RequestParam String locale,       // zh_CN, en_US 等
            @RequestParam String translation   // 翻译内容
    ) {
        JsonEntityResult<ShopI18nTranslations> result = new JsonEntityResult<>();
        try {
            if (productId == null) {
                result.setSuccess(false);
                result.setMessage("商品ID不能为空");
                return result;
            }
            if (fieldName == null || (!fieldName.equals("name") && !fieldName.equals("description"))) {
                result.setSuccess(false);
                result.setMessage("字段名称只能是 name 或 description");
                return result;
            }
            if (locale == null || locale.trim().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("语言代码不能为空");
                return result;
            }
            if (translation == null || translation.trim().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("翻译内容不能为空");
                return result;
            }

            // 验证语言代码是否存在且启用
            ShopI18nLocales localeEntity = shopI18nLocalesService.uniqueByLocaleCode(locale);
            if (localeEntity == null || localeEntity.getIsActive() == null || localeEntity.getIsActive() != 1) {
                result.setSuccess(false);
                result.setMessage("语言代码不存在或未启用：" + locale);
                return result;
            }

            // 保存或更新翻译
            shopI18nTranslationsService.saveOrUpdateTranslation("product", productId, fieldName, locale, translation.trim());

            // 返回保存后的翻译对象
            ShopI18nTranslations saved = shopI18nTranslationsService.findByProductAndFieldAndLocale(productId, fieldName, locale);
            result.setEntity(saved);
            result.setSuccess(true);
            result.setMessage("保存成功");
            log.info("保存商品翻译成功 - 商品ID: {}, 字段: {}, 语言: {}", productId, fieldName, locale);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存翻译失败：" + e.getMessage());
            log.error("保存商品翻译失败", e);
        }
        redisUtil.deleteHotResultKeys("product:detail:."+productId.toString());
        return result;
    }

    /**
     * 批量保存翻译（一次性保存多个字段）
     */
    @RequestMapping(value = "batchSaveProductTranslations", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntityResult<String> batchSaveProductTranslations(
            @RequestBody Map<String, Object> requestData
    ) {
        JsonEntityResult<String> result = new JsonEntityResult<>();
        try {
            // 从 requestData 中获取 productId
            Long productId = null;
            if (requestData.get("productId") != null) {
                if (requestData.get("productId") instanceof Number) {
                    productId = ((Number) requestData.get("productId")).longValue();
                } else {
                    try {
                        productId = Long.parseLong(requestData.get("productId").toString());
                    } catch (NumberFormatException e) {
                        log.warn("无法解析productId: {}", requestData.get("productId"));
                    }
                }
            }
            
            if (productId == null) {
                result.setSuccess(false);
                result.setMessage("商品ID不能为空");
                return result;
            }

            // 解析请求数据
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> translationsData = (List<Map<String, Object>>) requestData.get("translations");
            if (translationsData == null || translationsData.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("翻译数据不能为空");
                return result;
            }

            // 转换为 TranslationDTO 列表
            List<TranslationDTO> translations = new ArrayList<>();
            for (Map<String, Object> item : translationsData) {
                TranslationDTO dto = new TranslationDTO();
                dto.setFieldName((String) item.get("fieldName"));
                dto.setLocale((String) item.get("locale"));
                dto.setTranslation((String) item.get("translation"));
                
                // 验证字段
                if (dto.getFieldName() != null && dto.getLocale() != null && dto.getTranslation() != null) {
                    if (!dto.getFieldName().equals("name") && !dto.getFieldName().equals("description")) {
                        log.warn("跳过无效的字段名称: {}", dto.getFieldName());
                        continue;
                    }
                    translations.add(dto);
                }
            }

            if (translations.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("没有有效的翻译数据");
                return result;
            }

            // 获取默认语言代码
            ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
            String defaultLocaleCode = defaultLocale != null ? defaultLocale.getLocaleCode() : null;
            
            // 批量保存翻译
            shopI18nTranslationsService.batchSaveTranslations("product", productId, translations);
            // 【数据同步】如果保存的是默认语言的翻译，需要同步更新商品表的 name 和 description
            if (defaultLocaleCode != null) {
                ShopProducts product = getEntityService().get(productId);
                if (product != null) {
                    boolean needUpdate = false;
                    
                    // 检查是否有默认语言的 name 或 description 翻译
                    for (TranslationDTO dto : translations) {
                        if (defaultLocaleCode.equals(dto.getLocale())) {
                            if ("name".equals(dto.getFieldName()) && dto.getTranslation() != null) {
                                String newName = dto.getTranslation().trim();
                                // 只有当值真正改变时才更新
                                if (product.getName() == null || !newName.equals(product.getName())) {
                                    product.setName(newName);
                                    needUpdate = true;
                                    log.debug("准备同步商品名称 - 商品ID: {}, 新名称: {}", productId, newName);
                                }
                            } else if ("description".equals(dto.getFieldName()) && dto.getTranslation() != null) {
                                String newDescription = dto.getTranslation().trim();
                                // 只有当值真正改变时才更新
                                if (product.getDescription() == null || !newDescription.equals(product.getDescription())) {
                                    product.setDescription(newDescription);
                                    needUpdate = true;
                                    log.debug("准备同步商品描述 - 商品ID: {}, 新描述长度: {}", productId, newDescription.length());
                                }
                            }
                        }
                    }
                    
                    // 如果有更新，保存商品信息
                    if (needUpdate) {
                        getEntityService().update(product);
                        log.info("已同步更新商品默认语言数据到商品表 - 商品ID: {}", productId);
                    } else {
                        log.debug("默认语言翻译数据未变化，无需同步商品表 - 商品ID: {}", productId);
                    }
                }
            }
            redisUtil.deleteHotResultKeys("product:detail:."+productId.toString());
            result.setEntity("批量保存成功");
            result.setSuccess(true);
            result.setMessage("成功保存 " + translations.size() + " 条翻译");
            log.info("批量保存商品翻译成功 - 商品ID: {}, 数量: {}", productId, translations.size());
            CacheShopRedisEvent event = new CacheShopRedisEvent();
            event.setShopProducts(this.getEntityService().get(productId));
            event.setAction("Product");
            eventBus.publish(event);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("批量保存翻译失败：" + e.getMessage());
            log.error("批量保存商品翻译失败", e);
        }
        return result;
    }

    /**
     * 删除翻译
     */
    @RequestMapping("deleteProductTranslation")
    @ResponseBody
    public JsonEntityResult<String> deleteProductTranslation(
            @RequestParam Long productId,
            @RequestParam String fieldName,
            @RequestParam String locale
    ) {
        JsonEntityResult<String> result = new JsonEntityResult<>();
        try {
            if (productId == null) {
                result.setSuccess(false);
                result.setMessage("商品ID不能为空");
                return result;
            }
            if (fieldName == null || (!fieldName.equals("name") && !fieldName.equals("description"))) {
                result.setSuccess(false);
                result.setMessage("字段名称只能是 name 或 description");
                return result;
            }
            if (locale == null || locale.trim().isEmpty()) {
                result.setSuccess(false);
                result.setMessage("语言代码不能为空");
                return result;
            }

            // 删除翻译
            shopI18nTranslationsService.deleteTranslation("product", productId, fieldName, locale);

            result.setEntity("删除成功");
            result.setSuccess(true);
            result.setMessage("删除成功");
            log.info("删除商品翻译成功 - 商品ID: {}, 字段: {}, 语言: {}", productId, fieldName, locale);
            CacheShopRedisEvent event = new CacheShopRedisEvent();
            event.setShopProducts(this.getEntityService().get(productId));
            event.setAction("Product");
            eventBus.publish(event);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("删除翻译失败：" + e.getMessage());
            log.error("删除商品翻译失败", e);
        }
        redisUtil.deleteHotResultKeys("product:detail:."+productId.toString());
        return result;
    }


    /**
     * 【优化】确定最终图片URL：优先OSS WebP，失败降级到本地原图
     * 
     * @param compressResult 压缩结果
     * @param originalUrl 原图URL（降级使用）
     * @param relative 相对路径
     * @param serverRoot 服务器根路径
     * @param storageRoot 存储根路径
     * @return 最终图片URL
     */
    private String determineFinalImageUrl(com.acooly.showcase.shop.dto.ImageProcessResult compressResult,
                                         String originalUrl, String relative,
                                         String serverRoot, String storageRoot) {
        // 1. 优先使用OSS WebP原图URL（用于ShopProducts.imageUrl）
        if (compressResult.getWebpOssUrl() != null) {
            log.info("使用OSS WebP原图URL - URL: {}", compressResult.getWebpOssUrl());
            return compressResult.getWebpOssUrl();
        }
        
        // 2. 降级使用OSS缩略图WebP URL
        if (compressResult.getThumbWebpOssUrl() != null) {
            log.info("降级使用OSS缩略图WebP URL - URL: {}", compressResult.getThumbWebpOssUrl());
            return compressResult.getThumbWebpOssUrl();
        }
        
        // 3. OSS上传失败，降级使用本地原图URL
        log.warn("OSS上传失败，降级使用本地原图 - 相对路径: {}", relative);
        return originalUrl;
    }

    /**
     * 【优化】确定主图URL：优先使用OSS WebP URL
     * 
     * @param primaryImageUrl 主图URL（可能是本地路径）
     * @param request HTTP请求
     * @return 最终主图URL（优先OSS WebP，失败降级到本地原图）
     */
    private String determinePrimaryImageUrl(String primaryImageUrl, HttpServletRequest request) {
        if (primaryImageUrl == null || primaryImageUrl.trim().isEmpty()) {
            return primaryImageUrl;
        }
        
        // 从request attribute获取压缩结果
        com.acooly.showcase.shop.dto.ImageProcessResult compressResult =
                (com.acooly.showcase.shop.dto.ImageProcessResult) request.getAttribute("_imageCompressResult_" + primaryImageUrl);
        
        if (compressResult != null && compressResult.isSuccess()) {
            // 优先使用OSS WebP原图URL
            if (compressResult.getWebpOssUrl() != null) {
                log.info("主图使用OSS WebP原图URL - URL: {}", compressResult.getWebpOssUrl());
                return compressResult.getWebpOssUrl();
            }
            
            // 降级使用OSS缩略图WebP URL
            if (compressResult.getThumbWebpOssUrl() != null) {
                log.info("主图降级使用OSS缩略图WebP URL - URL: {}", compressResult.getThumbWebpOssUrl());
                return compressResult.getThumbWebpOssUrl();
            }
        }
        
        // OSS上传失败，使用本地原图URL
        log.warn("主图OSS上传失败，使用本地原图 - URL: {}", primaryImageUrl);
        return primaryImageUrl;
    }

    /**
     * 【优化】处理上传的图片文件（提取方法，减少嵌套）
     */
    private void processUploadedImageFile(UploadResult ur, Map<String, String> uploadedFileUrls,
                                         String serverRoot, String storageRoot,
                                         HttpServletRequest request) {
        String paramName = ur.getParameterName();
        String relative = ur.getRelativeFile();
        String fullUrl = (serverRoot.isEmpty() ? "" : serverRoot) + relative;
        
        try {
            // 获取本地文件的完整路径
            String fullPath = storageRoot + File.separator + relative.replace("/", File.separator);
            File localFile = new File(fullPath);
            
            if (!localFile.exists() || !localFile.isFile()) {
                log.warn("本地文件不存在或不是文件，跳过处理 - 文件: {}", fullPath);
                // 存储相对路径，供Service使用
                uploadedFileUrls.put(paramName != null ? paramName : "file", relative);
                return;
            }
            
            // 处理图片文件
            if (isImageFile(localFile)) {
                // 存储相对路径，供Service使用（Service会处理压缩和OSS上传）
                uploadedFileUrls.put(paramName != null ? paramName : "file", relative);
            } else {
                // 非图片文件，直接上传到OSS（如果启用）
                uploadNonImageFileToOss(localFile, relative, request);
                // 存储相对路径，供Service使用
                uploadedFileUrls.put(paramName != null ? paramName : "file", relative);
            }
        } catch (Exception e) {
            log.error("处理上传文件异常 - 文件: {}", relative, e);
            uploadedFileUrls.put(paramName != null ? paramName : "file", fullUrl);
        }
    }

    /**
     * 【优化】处理图片文件：压缩、转换、上传OSS
     * 
     * @return 最终图片URL（优先OSS WebP，失败降级到本地原图）
     */
    private String processImageFile(File localFile, String fullUrl, String relative,
                                  String serverRoot, String storageRoot,
                                  HttpServletRequest request) {
        if (imageCompressUtil == null) {
            log.warn("ImageCompressUtil未注入，跳过图片压缩处理");
            return fullUrl;
        }
        
        log.info("开始图片压缩处理 - 文件: {}, 大小: {}KB, 路径: {}", 
                localFile.getName(), localFile.length() / 1024, localFile.getAbsolutePath());
        
        com.acooly.showcase.shop.dto.ImageProcessResult compressResult = imageCompressUtil
                .compressAndConvert(localFile, localFile.getParent(), STORAGE_NAMESPACE);
        
        if (compressResult == null) {
            log.error("图片压缩返回null - 文件: {}", localFile.getName());
            return fullUrl;
        }
        
        log.info("图片压缩处理完成 - 文件: {}, 成功: {}, WebP OSS URL: {}, 缩略图OSS URL: {}", 
                localFile.getName(), 
                compressResult.isSuccess(),
                compressResult.getWebpOssUrl() != null ? compressResult.getWebpOssUrl() : "未上传",
                compressResult.getThumbWebpOssUrl() != null ? compressResult.getThumbWebpOssUrl() : "未上传");
        
        if (compressResult.isSuccess()) {
            // 确定最终URL：优先OSS WebP，失败降级到本地原图
            String finalFullUrl = determineFinalImageUrl(compressResult, fullUrl, relative, serverRoot, storageRoot);
            
            // 保存压缩结果到request attribute（使用最终URL作为key）
            request.setAttribute("_imageCompressResult_" + finalFullUrl, compressResult);
            
            return finalFullUrl;
        } else {
            log.warn("图片压缩失败，使用原图 - {}", relative);
            return fullUrl;
        }
    }

    /**
     * 【优化】上传非图片文件到OSS
     */
    private void uploadNonImageFileToOss(File localFile, String relative, HttpServletRequest request) {
        if (ossStorageService == null) {
            return;
        }
        
        try {
            String ossKey;
            String normalizedRelative = relative.startsWith("/") ? relative.substring(1) : relative;
            
            if (normalizedRelative.startsWith(STORAGE_NAMESPACE + "/")) {
                ossKey = normalizedRelative;
            } else {
                String fileName = normalizedRelative.substring(normalizedRelative.lastIndexOf("/") + 1);
                ossKey = ossStorageService.generateOssKey(STORAGE_NAMESPACE, fileName);
            }
            
            String ossUrl = ossStorageService.uploadToOss(localFile, ossKey);
            if (ossUrl != null) {
                log.info("非图片文件已上传到OSS - 本地: {}, OSS: {}", relative, ossUrl);
            }
        } catch (Exception e) {
            log.error("非图片文件OSS上传异常 - 文件: {}", relative, e);
        }
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
