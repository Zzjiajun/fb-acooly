package com.acooly.showcase.shop.event.base;

import com.acooly.module.event.EventHandler;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopSubCategories;
import com.acooly.showcase.shop.event.CacheShopRedisEvent;
import com.acooly.showcase.shop.service.ShopSubCategoriesService;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@EventHandler
public class CacheShopRedisEventHandler {
    @Autowired
    private ShopSubCategoriesService shopSubCategoriesService;
    @Autowired
    private RedisShopUtil redisUtil;
    public static final String CATEGORY_TREE = "category:tree:all";

    // 事件动作类型常量
    private static final String ACTION_PRODUCT = "Product";
    private static final String ACTION_TRANSLATE = "Translate";
    private static final String ACTION_BRAND = "Brand";
    private static final String ACTION_ATTR = "Attr";
    private static final String ACTION_CATEGORY = "Category";

    @Handler(delivery = Invoke.Asynchronously)
    public void handleCacheShopRedisEventSynchronously(CacheShopRedisEvent event) {
        if (event == null) {
            log.warn("⚠ Received null event, ignoring...");
            return;
        }

        try {
            String action = event.getAction();
            if (action == null) {
                log.warn("⚠ Received event with null action, ignoring...");
                return;
            }

            switch (action) {
                case ACTION_PRODUCT:
                    if (event.getShopProducts() != null) {
                        evictProductCache(event.getShopProducts());
                    } else {
                        log.warn("⚠ Product action but shopProducts is null");
                    }
                    break;
                case ACTION_TRANSLATE:
                    if (event.getShopProducts() != null) {
                        evictProductTranslationCache(event.getShopProducts());
                    } else {
                        log.warn("⚠ Translate action but shopProducts is null");
                    }
                    break;
                case ACTION_BRAND:
                    if (event.getBrandId() != null) {
                        evictBrandCache(event.getBrandId());
                    } else {
                        log.warn("⚠ Brand action but brandId is null");
                    }
                    break;
                case ACTION_ATTR:
                    evictAttrCache(event.getAttrId(), event.getAttrValueId());
                    break;
                case ACTION_CATEGORY:
                    if (event.getCategoryId() != null && event.getIsSubCategory() != null) {
                        evictCategoryCache(event.getCategoryId(), event.getIsSubCategory());
                    } else {
                        log.warn("⚠ Category action but categoryId or isSubCategory is null");
                    }
                    break;
                default:
                    log.warn("⚠ Unknown action: {}, ignoring...", action);
                    break;
            }
        } catch (Exception e) {
            log.error("🔥 Cache eviction event handling failed, action={}, message={}",
                    event.getAction(), e.getMessage(), e);
        }
    }
    /**
     * 商品修改/新增/删除后清除相关缓存（完整归类版）
     *
     * 清除范围：
     * 1. 商品基础信息缓存（详情、列表、筛选、图片、品牌、属性值标签、评论、相关商品、总数）
     * 2. 翻译缓存（i18n:product:{productId}:*）
     * 3. 分类相关缓存（该商品所属分类的商品列表、商品数量）
     * 4. 筛选相关缓存（筛选选项、品牌计数、属性值计数）
     * 5. 首页相关缓存（首页数据、分类树）
     * 6. 搜索相关缓存（搜索建议、热门结果）
     *
     */
    public void evictProductCache(ShopProducts product) {
        if (product == null || product.getId() == null) {
            log.warn("⚠ Product is null or productId is null, skipping cache eviction");
            return;
        }

        try {
            // 获取商品信息（用于确定分类）
            Long productId = product.getId();
            Long subCategoryId = product.getSubCategoryId();
            Long parentCategoryId = null;

            // 查询子分类信息以获取父分类ID
            if (subCategoryId != null) {
                ShopSubCategories subCategory = shopSubCategoriesService.get(subCategoryId);
                if (subCategory != null) {
                    parentCategoryId = subCategory.getParentId();
                } else {
                    log.warn("SubCategory ID {} not found for product ID {}", subCategoryId, productId);
                }
            } else {
                log.warn("Product ID {} has null subCategoryId", productId);
            }

            log.info("Evicting cache for productId: {}, subCategoryId: {}, parentCategoryId: {}",
                    productId, subCategoryId, parentCategoryId);

            // 收集所有需要清除的模式（批量处理）
            List<String> patterns = new ArrayList<>();

            // ========== 1. 商品基础信息缓存（所有语言） ==========
            addProductBasicCachePatterns(patterns, productId);

            // ========== 2. 翻译缓存 ==========
            addTranslationCachePatterns(patterns, productId);

            // ========== 3. 分类相关缓存（所有语言） ==========
            if (subCategoryId != null) {
                addSubCategoryCachePatterns(patterns, subCategoryId);
            }
            if (parentCategoryId != null) {
                addParentCategoryCachePatterns(patterns, parentCategoryId);
            }

            // ========== 4. 筛选相关缓存（所有语言） ==========
            addFilterCachePatterns(patterns);

            // ========== 5. 首页相关缓存（所有语言） ==========
            addHomePageCachePatterns(patterns);

            // ========== 6. 搜索相关缓存 ==========
            addSearchCachePatterns(patterns);

            // 批量清除所有模式（异步执行，不阻塞主流程）
            evictByPatternsAsync(patterns);
            evictByPattern("product:list*");
            evictByPattern("products:list*");
            log.info("Cache eviction initiated for productId: {} (async, {} patterns)", productId, patterns.size());

        } catch (Exception e) {
            log.error("Failed to evict product cache, productId: {}", product.getId(), e);
        }
    }

    /**
     * 商品翻译内容修改后清除相关缓存（完整归类版）
     *
     * 清除范围：
     * 1. 翻译缓存（i18n:product:{productId}:*）
     * 2. 商品基础信息缓存（详情、列表、筛选，所有语言）
     * 3. 分类相关缓存（该商品所属分类的商品列表，所有语言）
     * 4. 首页相关缓存（首页数据、分类树，所有语言）
     * 5. 搜索相关缓存（搜索建议、热门结果）
     *
     */
    public void evictProductTranslationCache(ShopProducts product) {
        if (product == null || product.getId() == null) {
            log.warn("⚠ Product is null or productId is null, skipping translation cache eviction");
            return;
        }

        try {
            // 获取商品信息（用于确定分类）
            Long productId = product.getId();
            Long subCategoryId = product.getSubCategoryId();
            Long parentCategoryId = null;

            // 查询子分类信息以获取父分类ID
            if (subCategoryId != null) {
                ShopSubCategories subCategory = shopSubCategoriesService.get(subCategoryId);
                if (subCategory != null && subCategory.getParentId() != null) {
                    parentCategoryId = subCategory.getParentId();
                }
            }

            log.info("Evicting translation cache for productId: {}, subCategoryId: {}, parentCategoryId: {}",
                    productId, subCategoryId, parentCategoryId);

            List<String> patterns = new ArrayList<>();

            // ========== 1. 翻译缓存 ==========
            addTranslationCachePatterns(patterns, productId);

            // ========== 2. 商品基础信息缓存（所有语言） ==========
            addProductBasicCachePatterns(patterns, productId);

            // ========== 3. 分类相关缓存（所有语言） ==========
            if (subCategoryId != null) {
                addSubCategoryCachePatterns(patterns, subCategoryId);
            }
            if (parentCategoryId != null) {
                addParentCategoryCachePatterns(patterns, parentCategoryId);
            }

            // ========== 4. 首页相关缓存（所有语言） ==========
            addHomePageCachePatterns(patterns);

            // ========== 5. 搜索相关缓存 ==========
            addSearchCachePatterns(patterns);

            // 批量清除所有模式（异步执行）
            evictByPatternsAsync(patterns);

            log.info("Translation cache eviction initiated for productId: {} (async, {} patterns)", productId, patterns.size());

        } catch (Exception e) {
            log.error("Failed to evict product translation cache, productId: {}", product.getId(), e);
        }
    }
    /**
     * 品牌修改/新增/删除后清除相关缓存（完整归类版）
     *
     * 清除范围：
     * 1. 品牌相关的商品缓存（所有语言）
     * 2. 筛选相关缓存（筛选选项、品牌计数，所有语言）
     * 3. 商品列表缓存（所有分页，所有语言）
     * 4. 商品筛选缓存（所有语言）
     * 5. 分类相关缓存（所有语言）
     * 6. 首页相关缓存（首页数据、分类树，所有语言）
     * 7. 搜索相关缓存（搜索建议）
     *
     */
    public void evictBrandCache(Long brandId) {
        if (brandId == null) {
            log.warn("⚠ BrandId is null, skipping cache eviction");
            return;
        }

        try {
            log.info("Evicting cache for brandId: {}", brandId);

            List<String> patterns = new ArrayList<>();

            // ========== 1. 品牌相关的商品缓存（所有语言） ==========
            addBrandProductCachePatterns(patterns);

            // ========== 2. 筛选相关缓存（所有语言） ==========
            addFilterCachePatterns(patterns);

            // ========== 3. 商品列表缓存（所有分页，所有语言） ==========
            addProductListCachePatterns(patterns);

            // ========== 4. 商品筛选缓存（所有语言） ==========
            addProductFilterCachePatterns(patterns);

            // ========== 5. 分类相关缓存（所有语言） ==========
            addAllCategoryCachePatterns(patterns);

            // ========== 6. 首页相关缓存（所有语言） ==========
            addHomePageCachePatterns(patterns);

            // ========== 7. 搜索相关缓存 ==========
            addSearchCachePatterns(patterns);

            // 批量清除所有模式（异步执行）
            evictByPatternsAsync(patterns);

            log.info("Cache eviction initiated for brandId: {} (async, {} patterns)", brandId, patterns.size());

        } catch (Exception e) {
            log.error("Failed to evict brand cache, brandId: {}", brandId, e);
        }
    }

    /**
     * 属性/属性值修改/新增/删除后清除相关缓存（完整归类版）
     *
     * 清除范围：
     * 1. 属性值标签缓存（所有商品）
     * 2. 筛选相关缓存（筛选选项、属性值计数，所有语言）
     * 3. 商品列表缓存（所有分页，所有语言）
     * 4. 商品筛选缓存（所有语言）
     * 5. 商品详情缓存（所有商品，所有语言）
     * 6. 分类相关缓存（所有语言）
     * 7. 首页相关缓存（首页数据、分类树，所有语言）
     *
     * @param attrId 属性ID（可选，如果为null则清除所有属性相关缓存）
     * @param attrValueId 属性值ID（可选，如果为null则清除所有属性值相关缓存）
     */
    public void evictAttrCache(Long attrId, Long attrValueId) {
        if (attrId == null && attrValueId == null) {
            log.warn("⚠ Both attrId and attrValueId are null, skipping cache eviction");
            return;
        }

        try {
            log.info("Evicting cache for attrId: {}, attrValueId: {}", attrId, attrValueId);

            List<String> patterns = new ArrayList<>();

            // ========== 1. 属性值标签缓存（所有商品） ==========
            addAttrValueTagsCachePatterns(patterns);

            // ========== 2. 筛选相关缓存（所有语言） ==========
            addFilterCachePatterns(patterns);

            // ========== 3. 商品列表缓存（所有分页，所有语言） ==========
            addProductListCachePatterns(patterns);

            // ========== 4. 商品筛选缓存（所有语言） ==========
            addProductFilterCachePatterns(patterns);

            // ========== 5. 商品详情缓存（所有商品，所有语言） ==========
            addAllProductDetailCachePatterns(patterns);

            // ========== 6. 分类相关缓存（所有语言） ==========
            addAllCategoryCachePatterns(patterns);

            // ========== 7. 首页相关缓存（所有语言） ==========
            addHomePageCachePatterns(patterns);

            // 批量清除所有模式（异步执行）
            evictByPatternsAsync(patterns);

            log.info("Cache eviction initiated for attrId: {}, attrValueId: {} (async, {} patterns)",
                    attrId, attrValueId, patterns.size());

        } catch (Exception e) {
            log.error("Failed to evict attr cache, attrId: {}, attrValueId: {}", attrId, attrValueId, e);
        }
    }
    /**
     * 分类修改/新增/删除后清除相关缓存（完整归类版）
     *
     * 清除范围：
     * 1. 分类树缓存（所有语言）
     * 2. 分类详情缓存（所有语言）
     * 3. 分类商品列表缓存（所有分页，所有语言）
     * 4. 分类商品数量缓存（所有语言）
     * 5. 筛选相关缓存（筛选选项、品牌计数、属性值计数，所有语言）
     * 6. 商品列表缓存（所有分页，所有语言）
     * 7. 商品筛选缓存（所有语言）
     * 8. 首页相关缓存（首页数据，所有语言）
     * 9. 商品总数缓存
     *
     * @param categoryId 分类ID
     * @param isSubCategory true=子分类, false=父分类
     */
    public void evictCategoryCache(Long categoryId, Boolean isSubCategory) {
        if (categoryId == null || isSubCategory == null) {
            log.warn("⚠ CategoryId or isSubCategory is null, skipping cache eviction");
            return;
        }

        try {
            log.info("Evicting cache for categoryId: {}, isSubCategory: {}", categoryId, isSubCategory);

            List<String> patterns = new ArrayList<>();

            // ========== 1. 分类树缓存（所有语言，支持混合格式） ==========
            // 匹配：category:tree:all.en_US.en_US（冒号开头，点号连接）
            addCachePattern(patterns, null, CATEGORY_TREE + "*"); // 匹配混合格式（冒号开头，点号连接）
            addCachePattern(patterns, null, CATEGORY_TREE + ":*"); // 匹配纯冒号格式
            addCachePattern(patterns, "category.tree.*", null); // 匹配纯点号格式
            addCachePattern(patterns, null, CATEGORY_TREE); // 精确匹配

            if (isSubCategory) {
                // ========== 子分类相关缓存（所有语言） ==========
                addSubCategoryCachePatterns(patterns, categoryId);

                // 查询父分类ID，清除父分类相关缓存
                ShopSubCategories subCategory = shopSubCategoriesService.get(categoryId);
                if (subCategory != null && subCategory.getParentId() != null) {
                    Long parentId = subCategory.getParentId();
                    addParentCategoryCachePatterns(patterns, parentId);
                }
            } else {
                // ========== 父分类相关缓存（所有语言） ==========
                addParentCategoryCachePatterns(patterns, categoryId);

                // 清除该父分类下所有子分类的商品列表缓存（所有语言）
                addCachePattern(patterns, "sub_category.products.*.*.*", "sub_category:products:*:*:*");
                addCachePattern(patterns, "sub_category_with_products.*.*.*", "sub_category_with_products:*:*:*");
                addCachePattern(patterns, "sub_category.product_count.*.*", "sub_category:product_count:*:*");
                // 兼容旧格式
                addCachePattern(patterns, "sub_category.products.*", "sub_category:products:*");
                addCachePattern(patterns, "sub_category_with_products.*", "sub_category_with_products:*");
                addCachePattern(patterns, "sub_category.product_count.*", "sub_category:product_count:*");
            }

            // ========== 2. 筛选相关缓存（所有语言） ==========
            addFilterCachePatterns(patterns);

            // ========== 3. 商品列表缓存（所有分页，所有语言） ==========
            addProductListCachePatterns(patterns);

            // ========== 4. 商品筛选缓存（所有语言） ==========
            addProductFilterCachePatterns(patterns);

            // ========== 5. 首页相关缓存（所有语言） ==========
            addHomePageCachePatterns(patterns);

            // ========== 6. 商品总数缓存 ==========
            addCachePattern(patterns, "products.count", "products:count:");

            // 批量清除所有模式（异步执行）
            evictByPatternsAsync(patterns);

            log.info("Cache eviction initiated for categoryId: {}, isSubCategory: {} (async, {} patterns)",
                    categoryId, isSubCategory, patterns.size());

        } catch (Exception e) {
            log.error("Failed to evict category cache, categoryId: {}, isSubCategory: {}",
                    categoryId, isSubCategory, e);
        }
    }
    /**
     * 添加商品基础信息缓存模式
     * 
     * 注意：由于buildKey使用点号连接，但常量使用冒号，导致实际key是混合格式
     * 例如：product:detail:.27.en_US（冒号开头，点号连接，语言代码在末尾）
     * 
     * Redis SCAN的*通配符可以匹配任意字符（包括分隔符），所以：
     * - product:detail:27* 可以匹配 product:detail:27.en_US 和 product:detail:.27.en_US（*匹配.27.en_US）
     * - product:detail* 可以匹配所有以product:detail开头的key（最通用）
     */
    private void addProductBasicCachePatterns(List<String> patterns, Long productId) {
        if (productId == null) {
            return;
        }

        String productIdStr = String.valueOf(productId);

        // ========== 商品详情缓存（支持混合格式） ==========
        // 匹配：product:detail:.27.en_US, product:detail:.27, product.detail.27.en_US
        addCachePattern(patterns, "product.detail." + productIdStr + ".*", "product:detail:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式（*可以跨分隔符）
        addCachePattern(patterns, "product.detail." + productIdStr, "product:detail:" + productIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "product:detail:." + productIdStr + "*"); // 明确匹配混合格式（冒号开头，点号连接）
        addCachePattern(patterns, null, "product:detail*"); // 最通用：匹配所有以product:detail开头的key（包括混合格式）

        // ========== 商品图片缓存（支持混合格式） ==========
        // 匹配：product:image:list:.27
        addCachePattern(patterns, "product.image.list." + productIdStr, "product:image:list:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "product:image:list:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:image:list*"); // 最通用：匹配所有以product:image:list开头的key

        // 匹配：product:primary_image:.27, product.primary_image.27
        addCachePattern(patterns, "product.primary_image." + productIdStr, "product:primary_image:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "product:primary_image:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:primary_image*"); // 最通用

        // 匹配：lock:product:image:.27, lock.product.image.27
        addCachePattern(patterns, "lock.product.image." + productIdStr, "lock:product:image:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "lock:product:image:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "lock:product:image*"); // 最通用

        // ========== 商品品牌缓存（支持混合格式） ==========
        // 匹配：product:brand:.27.en_US, product:brand:.27
        addCachePattern(patterns, "product.brand." + productIdStr + ".*", "product:brand:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "product.brand." + productIdStr, "product:brand:" + productIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "product:brand:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:brand*"); // 最通用：匹配所有以product:brand开头的key

        // ========== 商品属性值标签缓存（支持混合格式） ==========
        // 匹配：product:attr_value_tags:.27
        addCachePattern(patterns, "product.attr_value_tags." + productIdStr, "product:attr_value_tags:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "product:attr_value_tags:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:attr_value_tags*"); // 最通用：匹配所有以product:attr_value_tags开头的key

        // ========== 商品评论缓存（支持混合格式） ==========
        // 匹配：product:reviews:.27
        addCachePattern(patterns, "product.reviews." + productIdStr, "product:reviews:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "product:reviews:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:reviews*"); // 最通用：匹配所有以product:reviews开头的key

        // ========== 商品相关商品缓存（支持混合格式） ==========
        // 匹配：product:related:.27, product:related:.27.en_US
        addCachePattern(patterns, "product.related." + productIdStr + ".*", "product:related:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "product.related." + productIdStr, "product:related:" + productIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "product:related:." + productIdStr + "*"); // 明确匹配混合格式
        addCachePattern(patterns, null, "product:related*"); // 最通用：匹配所有以product:related开头的key

    }
    /**
     * 添加翻译缓存模式
     */
    private void addTranslationCachePatterns(List<String> patterns, Long productId) {
        if (productId == null) {
            return;
        }
        String productIdStr = String.valueOf(productId);
        // 翻译缓存（所有语言）
        // 支持格式：i18n:product:27:*, i18n:product:27, i18n:product:.27:*
        addCachePattern(patterns, null, "i18n:product:" + productIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "i18n:product:" + productIdStr); // 精确匹配
        addCachePattern(patterns, null, "i18n:product:." + productIdStr + "*"); // 明确匹配混合格式
    }

    /**
     * 添加商品列表缓存模式
     * 
     * 注意：实际key格式可能是混合的，例如：
     * - product:list:full_result.product:filter:brand:1,2:price:100-1000:sort:price:asc:page:1:size:20.en_US
     * - products:list:1.20.zh_CN
     * - products:list:.1.20.zh_CN（混合格式，ID前有点号）
     */
    private void addProductListCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "products.list.*", "products:list:*");
        addCachePattern(patterns, "product.list.*", "product:list:*");
        addCachePattern(patterns, "product.list.full_result.*", "product:list:full_result*"); // 匹配混合格式（冒号开头，点号连接）
        
        // 匹配混合格式（冒号开头，点号连接）
        addCachePattern(patterns, null, "product:list:full_result*"); // 匹配 product:list:full_result.xxx 和 product:list:full_result.product:filter:xxx
        addCachePattern(patterns, null, "product:list:full_result.product:filter*"); // 明确匹配 product:list:full_result.product:filter:xxx 格式
        
        // 匹配 products:list 的混合格式（ID前有点号）
        addCachePattern(patterns, null, "products:list:.*"); // 明确匹配 products:list:.1.20.zh_CN 格式（ID前有点号）
        
        // 最通用：匹配所有以 products:list 开头的key（包括混合格式）
        addCachePattern(patterns, null, "products:list*"); // 匹配 products:list:xxx、products:list.xxx 和 products:list:.xxx
    }

    /**
     * 添加商品筛选缓存模式
     * 
     * 注意：实际key格式可能是混合的，例如：
     * - product:list:full_result.product:filter:brand:1,2:price:100-1000:sort:price:asc:page:1:size:20.en_US
     */
    private void addProductFilterCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "product.filter.*", "product:filter:*");
        
        // 匹配纯冒号格式
        addCachePattern(patterns, null, "product:filter:post:*");
        
        // 匹配混合格式（作为product:list:full_result的一部分）
        addCachePattern(patterns, null, "product:list:full_result.product:filter:*"); // 匹配混合格式
        
        // 最通用：匹配所有以product:filter开头的key
        addCachePattern(patterns, null, "product:filter*");
    }

    /**
     * 添加筛选相关缓存模式
     */
    private void addFilterCachePatterns(List<String> patterns) {
        // 筛选选项缓存（所有语言）
        addCachePattern(patterns, "filter.options.*.*", "filter:options:*:*");
        addCachePattern(patterns, "filter.simple.*.*", "filter:simple:*:*");
        // 兼容旧格式
        addCachePattern(patterns, "filter.options.*", "filter:options:*");
        addCachePattern(patterns, "filter.simple.*", "filter:simple:*");

        // 品牌计数缓存（所有语言）
        addCachePattern(patterns, "filter.brand.count.*.*", "filter:brand:count:*:*");
        addCachePattern(patterns, "filter.brand.count.*", "filter:brand:count:*"); // 兼容旧格式

        // 属性值计数缓存（所有语言）
        addCachePattern(patterns, "filter.attr_value.count.*.*", "filter:attr_value:count:*:*");
        addCachePattern(patterns, "filter.attr_value.count.*", "filter:attr_value:count:*"); // 兼容旧格式
    }

    /**
     * 添加子分类相关缓存模式
     */
    private void addSubCategoryCachePatterns(List<String> patterns, Long subCategoryId) {
        if (subCategoryId == null) {
            return;
        }

        String categoryIdStr = String.valueOf(subCategoryId);

        // 子分类详情缓存（所有语言）
        // 支持格式：sub_category.detail.14.*, sub_category:detail:14:*, sub_category:detail:.14.*
        addCachePattern(patterns, "sub_category.detail." + categoryIdStr + ".*", "sub_category:detail:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "sub_category.detail." + categoryIdStr, "sub_category:detail:" + categoryIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "sub_category:detail:." + categoryIdStr + "*"); // 明确匹配混合格式

        // 子分类商品列表缓存（所有分页和所有语言）
        // 支持格式：sub_category.products.14.*.*, sub_category:products:14:*:*, sub_category:products:.14.*
        addCachePattern(patterns, "sub_category.products." + categoryIdStr + ".*", "sub_category:products:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "sub_category_with_products." + categoryIdStr + ".*", "sub_category_with_products:" + categoryIdStr + "*");
        addCachePattern(patterns, "sub.all.products.*", "sub:all:products:*");
        // 匹配复杂格式：product:list:full_result.product:filter:sub:14:...
        addCachePattern(patterns, null, "product:list:full_result*sub:" + categoryIdStr + "*");
        addCachePattern(patterns, null, "product:list:full_result*sub:." + categoryIdStr + "*");

        // 子分类商品数量缓存（所有语言）
        addCachePattern(patterns, "sub_category.product_count." + categoryIdStr + ".*", "sub_category:product_count:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "parent_category.product_count." + categoryIdStr + ".*", "parent_category:product_count:" + categoryIdStr + "*");
        // 兼容旧格式
        addCachePattern(patterns, "sub_category.product_count." + categoryIdStr, "sub_category:product_count:" + categoryIdStr);
        addCachePattern(patterns, "parent_category.product_count." + categoryIdStr, "parent_category:product_count:" + categoryIdStr);
    }

    /**
     * 添加父分类相关缓存模式
     */
    private void addParentCategoryCachePatterns(List<String> patterns, Long parentCategoryId) {
        if (parentCategoryId == null) {
            return;
        }

        String categoryIdStr = String.valueOf(parentCategoryId);

        // 父分类详情缓存（所有语言）
        // 支持格式：parent_category.detail.1.*, parent_category:detail:1:*, parent_category:detail:.1.*
        addCachePattern(patterns, "parent_category.detail." + categoryIdStr + ".*", "parent_category:detail:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "parent_category.detail." + categoryIdStr, "parent_category:detail:" + categoryIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "parent_category:detail:." + categoryIdStr + "*"); // 明确匹配混合格式

        // 父分类商品列表缓存（所有分页和所有语言）
        addCachePattern(patterns, "parent_category.products." + categoryIdStr + ".*", "parent_category:products:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "parent_category_with_products." + categoryIdStr + ".*", "parent_category_with_products:" + categoryIdStr + "*");
        // 兼容旧格式
        addCachePattern(patterns, "parent_category.products." + categoryIdStr, "parent_category:products:" + categoryIdStr);
        addCachePattern(patterns, "parent_category_with_products." + categoryIdStr, "parent_category_with_products:" + categoryIdStr);
        addCachePattern(patterns, null, "parent_category:products:." + categoryIdStr + "*"); // 明确匹配混合格式

        // 父分类商品总数缓存（所有语言）
        addCachePattern(patterns, "parent_category.total_products." + categoryIdStr + ".*", "parent_category:total_products:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "parent_category.total_products." + categoryIdStr, "parent_category:total_products:" + categoryIdStr); // 兼容旧格式
        addCachePattern(patterns, null, "parent_category:total_products:." + categoryIdStr + "*"); // 明确匹配混合格式

        // 父分类下的子分类列表缓存（所有语言）
        addCachePattern(patterns, "parent_category.sub_categories." + categoryIdStr + ".*", "parent_category:sub_categories:" + categoryIdStr + "*"); // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, "product.category_sub.*", "product:category_sub:*");
        // 兼容旧格式
        addCachePattern(patterns, "parent_category.sub_categories." + categoryIdStr, "parent_category:sub_categories:" + categoryIdStr);
        addCachePattern(patterns, "product.category_sub", "product:category_sub");
        addCachePattern(patterns, null, "parent_category:sub_categories:." + categoryIdStr + "*"); // 明确匹配混合格式
    }

    /**
     * 添加所有分类相关缓存模式
     */
    private void addAllCategoryCachePatterns(List<String> patterns) {
        addCachePattern(patterns, "sub_category.products.*.*.*", "sub_category:products:*:*:*");
        addCachePattern(patterns, "parent_category.products.*.*.*", "parent_category:products:*:*:*");
        addCachePattern(patterns, "sub_category_with_products.*.*.*", "sub_category_with_products:*:*:*");
        addCachePattern(patterns, "parent_category_with_products.*.*.*", "parent_category_with_products:*:*:*");
        // 兼容旧格式
        addCachePattern(patterns, "sub_category.products.*", "sub_category:products:*");
        addCachePattern(patterns, "parent_category.products.*", "parent_category:products:*");
        addCachePattern(patterns, "sub_category_with_products.*", "sub_category_with_products:*");
        addCachePattern(patterns, "parent_category_with_products.*", "parent_category_with_products:*");
    }

    /**
     * 添加首页相关缓存模式
     * 
     * 注意：实际key格式可能是混合的，例如：
     * - home_page_data.1.20.zh_CN（点号分隔）
     * - home:content.en_US（冒号开头，点号连接语言代码）
     */
    private void addHomePageCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "home_page_data.*", "home_page_data:*");
        addCachePattern(patterns, "home.page.data.*", "home:page:data:*");
        addCachePattern(patterns, null, CATEGORY_TREE + "*"); // 匹配混合格式（冒号开头，点号连接）
        // 匹配纯冒号格式
        addCachePattern(patterns, null, "home:page:data:*");
        
        // 匹配混合格式（冒号开头，点号连接）
        addCachePattern(patterns, null, "home:content*"); // 匹配 home:content.en_US
        addCachePattern(patterns, null, "home:*"); // 匹配所有 home: 开头的key（最通用）
    }

    /**
     * 添加搜索相关缓存模式
     */
    private void addSearchCachePatterns(List<String> patterns) {
        addCachePattern(patterns, "search.suggestions.*", "search:suggestions:*");
        addCachePattern(patterns, "hot_result.*", "hot:result:*");
    }

    // ============================================================================================================
    // ========================================== 七、缓存清除工具方法 ==========================================
    // ============================================================================================================

    /**
     * 使用模式匹配清除缓存（安全扫描方式，避免阻塞）
     *
     * 支持三种缓存键格式：
     * 1. 点号分隔：product.detail.123（buildKey生成）
     * 2. 冒号分隔：product:detail:123（直接字符串拼接）
     * 3. 混合格式：product:detail:.123.en_US（冒号开头，点号连接）
     *
     * 注意：Redis SCAN的*通配符可以匹配任意字符（包括分隔符），所以：
     * - product:detail:27* 可以匹配 product:detail:27.en_US 和 product:detail:.27.en_US（*匹配.27.en_US）
     * - product:detail* 可以匹配所有以product:detail开头的key（最通用）
     *
     * @param pattern Redis key模式（支持*通配符）
     *                 - 点号格式：product.detail.*
     *                 - 冒号格式：product:detail:*
     *                 - 混合格式：product:detail*（前缀匹配，匹配所有以product:detail开头的key）
     */
    private void evictByPattern(String pattern) {
        try {
            // 如果模式末尾没有通配符，添加 * 以匹配所有以该前缀开头的key
            String scanPattern = pattern;
            if (!scanPattern.endsWith("*") && !scanPattern.endsWith(":")) {
                scanPattern = scanPattern + "*";
            }

            // 使用scan安全扫描匹配的key
            Set<String> keys = redisUtil.scan(scanPattern);
            if (keys != null && !keys.isEmpty()) {
                // 批量删除，减少Redis网络往返
                long deletedCount = redisUtil.delete(keys.toArray(new String[0]));
                if (deletedCount > 0) {
                    log.debug("Evicted {} cache keys matching pattern: {}", deletedCount, pattern);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to evict cache by pattern: {}", pattern, e);
        }
    }

    /**
     * 批量清除多个模式的缓存（优化：合并扫描结果，减少Redis压力）
     *
     * @param patterns 模式列表
     */
    private void evictByPatterns(List<String> patterns) {
        if (patterns == null || patterns.isEmpty()) {
            return;
        }

        try {
            // 收集所有匹配的key
            Set<String> allKeys = new HashSet<>();
            for (String pattern : patterns) {
                String scanPattern = pattern;
                if (!scanPattern.endsWith("*") && !scanPattern.endsWith(":")) {
                    scanPattern = scanPattern + "*";
                }
                Set<String> keys = redisUtil.scan(scanPattern);
                if (keys != null && !keys.isEmpty()) {
                    allKeys.addAll(keys);
                }
            }

            // 一次性批量删除所有key
            if (!allKeys.isEmpty()) {
                long deletedCount = redisUtil.delete(allKeys.toArray(new String[0]));
                log.info("Evicted {} cache keys from {} patterns", deletedCount, patterns.size());
            }
        } catch (Exception e) {
            log.warn("Failed to evict cache by patterns: {}", patterns, e);
        }
    }

    /**
     * 批量清除缓存（同步执行，但方法名保留Async以保持兼容性）
     * 注意：当前实现是同步的，如需真正的异步执行，可以使用 @Async 注解
     *
     * @param patterns 模式列表
     */
    private void evictByPatternsAsync(List<String> patterns) {
        if (patterns == null || patterns.isEmpty()) {
            return;
        }
        evictByPatterns(patterns);
    }

    // ============================================================================================================
    // ========================================== 缓存模式添加辅助方法 ==========================================
    // ============================================================================================================

    /**
     * 添加品牌相关的商品缓存模式
     */
    private void addBrandProductCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "product.brand.*", "product:brand:*"); // 兼容旧格式
        
        // 匹配纯冒号格式和混合格式
        addCachePattern(patterns, null, "product:brand*"); // 最通用：匹配所有以product:brand开头的key
    }

    /**
     * 添加属性值标签缓存模式
     */
    private void addAttrValueTagsCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "product.attr_value_tags.*", "product:attr_value_tags:*"); // 兼容旧格式
        
        // 匹配混合格式（冒号开头，点号连接）
        addCachePattern(patterns, null, "product:attr_value_tags:.*"); // 匹配 product:attr_value_tags:.27 格式
        
        // 最通用：匹配所有以product:attr_value_tags开头的key
        addCachePattern(patterns, null, "product:attr_value_tags*");
    }

    /**
     * 添加所有商品详情缓存模式
     */
    private void addAllProductDetailCachePatterns(List<String> patterns) {
        // 匹配纯点号格式
        addCachePattern(patterns, "product.detail.*", "product:detail:*"); // 兼容旧格式
        
        // 匹配混合格式（冒号开头，点号连接）
        addCachePattern(patterns, null, "product:detail:.*"); // 匹配 product:detail:.27.* 格式
        
        // 最通用：匹配所有以product:detail开头的key
        addCachePattern(patterns, null, "product:detail*");
    }

    /**
     * 添加缓存模式（同时添加点号和冒号格式）
     *
     * @param patterns 模式列表
     * @param dotPattern 点号格式模式（如：product.detail.*）
     * @param colonPattern 冒号格式模式（如：product:detail:*）
     */
    private void addCachePattern(List<String> patterns, String dotPattern, String colonPattern) {
        if (dotPattern != null && !dotPattern.isEmpty()) {
            patterns.add(dotPattern);
        }
        if (colonPattern != null && !colonPattern.isEmpty()) {
            patterns.add(colonPattern);
        }
    }

    /**
     * 添加缓存模式（点号格式）
     */
    private void addCachePattern(List<String> patterns, String pattern) {
        if (pattern != null && !pattern.isEmpty()) {
            patterns.add(pattern);
        }
    }

}
