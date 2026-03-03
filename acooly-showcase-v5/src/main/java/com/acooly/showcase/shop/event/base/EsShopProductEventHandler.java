package com.acooly.showcase.shop.event.base;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.acooly.module.event.EventHandler;
import com.acooly.showcase.shop.entity.*;
import com.acooly.showcase.shop.event.CreateShopEsEvent;
import com.acooly.showcase.shop.dto.AttrValueTagDTO;
import com.acooly.showcase.shop.dto.ProductDTO;
import com.acooly.showcase.shop.service.*;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@EventHandler
public class EsShopProductEventHandler {

    @Autowired
    private RedisShopUtil redisUtils;
    @Autowired
    private ShopProductImagesService productImagesService;
    @Autowired
    private ShopBrandService shopBrandService;
    @Autowired
    private ShopProductAttrService shopProductAttrService;
    @Autowired
    private ShopAttrValueService shopAttrValueService;
    @Autowired
    private ShopAttrService shopAttrService;
    @Autowired
    private ShopI18nTranslationsService i18nTranslationService;
    
    private static final String INDEX_NAME = "product_index";
    private static final String ACTION_CREATE = "create";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_DELETE = "delete";
    
    private final ElasticsearchClient client;

    public EsShopProductEventHandler(ElasticsearchClient client) {
        this.client = client;
    }

    @Handler(delivery = Invoke.Asynchronously)
    public void handleEsShopProductEventAsynchronously(CreateShopEsEvent event) {
        if (event == null) {
            log.warn("⚠ Received null event, skipping ES handling.");
            return;
        }
        
        try {
            String action = event.getAction();
            if (ACTION_CREATE.equals(action) || ACTION_UPDATE.equals(action)) {
                indexToEs(event);
            } else if (ACTION_DELETE.equals(action)) {
                deleteFromEs(event);
            } else {
                log.warn("⚠ Unknown action: {}, skipping ES handling.", action);
            }
        } catch (Exception e) {
            log.error("🔥 ES event handling failed, action={}, message={}, shopProducts={}",
                    event.getAction(), e.getMessage(), event.getShopProducts(), e);
        } finally {
            // 清理缓存
            if (redisUtils != null) {
                redisUtils.deleteHotResultKeys("hot_result");
            }
        }
    }

    /**
     * 新增 / 更新处理 → ES 自动 upsert
     */
    private void indexToEs(CreateShopEsEvent event) {
        ShopProducts product = event.getShopProducts();
        if (product == null || product.getId() == null) {
            log.warn("⚠ Received null product / missing ID, skipping ES indexing.");
            return;
        }
        try {
            // 转换为DTO并补充关联数据
            ProductDTO productDTO = convertToProductDTO(product);
            if (productDTO == null) {
                log.warn("⚠ Failed to convert product to DTO, productId={}", product.getId());
                return;
            }
            // 补充品牌信息
            enrichBrandInfo(productDTO);
            // 补充属性值信息
            enrichAttrValueTags(productDTO);
            // 索引到ES（使用原始product对象，因为ES需要完整实体）
            client.index(i -> i
                    .index(INDEX_NAME)
                    .id(productDTO.getId().toString())
                    .document(productDTO)
            );
            log.info("✔ ES indexed document ID={}, action={}", product.getId(), event.getAction());
        } catch (IOException e) {
            log.error("❌ ES index failed for ID={}, action={}, reason={}",
                    product.getId(), event.getAction(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ Failed to process product for ES indexing, productId={}, reason={}",
                    product.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 补充品牌信息
     */
    private void enrichBrandInfo(ProductDTO productDTO) {
        if (productDTO.getBrandId() == null) {
            return;
        }
        
        // 如果品牌信息已存在，跳过查询
        if (productDTO.getBrandName() != null && !productDTO.getBrandName().isEmpty()) {
            return;
        }
        
        try {
            ShopBrand brand = shopBrandService.get(productDTO.getBrandId());
            if (brand != null) {
                productDTO.setBrandName(brand.getName());
                productDTO.setBrandLogo(brand.getLogo());
            }
        } catch (Exception e) {
            log.warn("⚠ Failed to load brand info, brandId={}, reason={}", 
                    productDTO.getBrandId(), e.getMessage());
        }
    }
    
    /**
     * 补充属性值标签信息
     */
    private void enrichAttrValueTags(ProductDTO productDTO) {
        if (productDTO.getId() == null) {
            return;
        }
        
        // 如果属性值信息已存在，跳过查询
        if (productDTO.getAttrValueTags() != null && !productDTO.getAttrValueTags().isEmpty()) {
            return;
        }
        
        try {
            List<AttrValueTagDTO> attrValueTags = getAttrValueTagsByProductId(productDTO.getId());
            productDTO.setAttrValueTags(attrValueTags);
        } catch (Exception e) {
            log.warn("⚠ Failed to load attr value tags, productId={}, reason={}", 
                    productDTO.getId(), e.getMessage());
        }
    }

    /**
     * 删除处理（支持单 ID 和多 ID）
     */
    private void deleteFromEs(CreateShopEsEvent event) {
        Serializable[] ids = event.getIds();
        if (ids == null || ids.length == 0) {
            log.warn("⚠ No IDs found for deletion, event ignored.");
            return;
        }

        // 转换为Long列表
        List<Long> idList = Arrays.stream(ids)
                .filter(id -> id != null && id instanceof Long)
                .map(id -> (Long) id)
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            log.warn("⚠ No valid Long IDs found for deletion, event ignored.");
            return;
        }

        // 单ID直接删除，多ID批量删除
        if (idList.size() == 1) {
            deleteById(idList.get(0));
        } else {
            deleteAll(idList);
        }
    }

    public void deleteById(Long id) {
        if (id == null) {
            log.warn("⚠ Cannot delete ES doc: ID is null");
            return;
        }
        
        try {
            client.delete(d -> d.index(INDEX_NAME).id(String.valueOf(id)));
            log.info("🗑 Deleted ES doc ID={}", id);
        } catch (IOException e) {
            log.error("❌ Failed to delete ES doc ID={}, reason={}", id, e.getMessage(), e);
        }
    }

    public void deleteAll(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            log.warn("⚠ Cannot delete ES docs: ID list is null or empty");
            return;
        }

        BulkRequest.Builder br = new BulkRequest.Builder();
        ids.stream()
                .filter(id -> id != null)
                .forEach(id -> br.operations(op -> op.delete(d -> d.index(INDEX_NAME).id(id.toString()))));

        try {
            BulkResponse response = client.bulk(br.build());
            if (response.errors()) {
                int errorCount = 0;
                for (var item : response.items()) {
                    if (item.error() != null) {
                        errorCount++;
                        log.error("❌ Bulk delete fail ID={}, reason={}", item.id(), item.error().reason());
                    }
                }
                log.warn("⚠ Bulk delete completed with {} errors out of {} docs", errorCount, ids.size());
            } else {
                log.info("🗑✔ Bulk delete success, deleted {} docs", ids.size());
            }
        } catch (IOException e) {
            log.error("🔥 Bulk delete failed, ids={}, reason={}", ids, e.getMessage(), e);
        }
    }



    /**
     * 将商品实体转换为DTO
     */
    private ProductDTO convertToProductDTO(ShopProducts product) {
        if (product == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCategoryId(product.getSubCategoryId());
        dto.setPrice(product.getPrice());
        dto.setReviewCount(product.getReviewCount());
        dto.setImageUrl(product.getImageUrl());
        dto.setSerialNumber(product.getSerialNumber());
        dto.setBrandId(product.getBrandId()); // 补充品牌ID
        dto.setFeatured(product.getFeatured());
        dto.setFreeShipping(product.getFreeShipping());
        // 获取商品主图（WebP格式）
        if (product.getId() != null && productImagesService != null) {
            try {
                ShopProductImages productImages = productImagesService.findPrimaryByProductId(product.getId());
                if (productImages != null) {
                    dto.setThumbWebpUrl(productImages.getThumbWebpUrl());
                    dto.setWebpUrl(productImages.getWebpUrl());
                } else {
                    // 如果没有主图，使用默认图片URL
                    String defaultImageUrl = product.getImageUrl();
                    dto.setThumbWebpUrl(defaultImageUrl);
                    dto.setWebpUrl(defaultImageUrl);
                }
            } catch (Exception e) {
                log.warn("⚠ Failed to load product images, productId={}, reason={}", 
                        product.getId(), e.getMessage());
                // 使用默认图片URL
                String defaultImageUrl = product.getImageUrl();
                dto.setThumbWebpUrl(defaultImageUrl);
                dto.setWebpUrl(defaultImageUrl);
            }
        } else {
            // 使用默认图片URL
            String defaultImageUrl = product.getImageUrl();
            dto.setThumbWebpUrl(defaultImageUrl);
            dto.setWebpUrl(defaultImageUrl);
        }
        List<String> locales = Arrays.asList("en_US", "zh_CN", "it_IT", "ja_JP", "zh_HK");
        String defaultLocale = "en_US";

        // 获取默认语言翻译（英文）
        Map<String, String> defaultTranslations = i18nTranslationService.getTranslations(
                "product", product.getId(), defaultLocale);

        // 设置英文原始字段
        String englishName = getTranslationSafe(defaultTranslations, "name", defaultLocale, product.getName());
        String englishDescription = getTranslationSafe(defaultTranslations, "description", defaultLocale, product.getDescription());
        dto.setName(englishName);
        dto.setDescription(englishDescription);
        // 填充其他语言 i18n_other
        Map<String, String> nameI18nOther = new HashMap<>();
        Map<String, String> descriptionI18nOther = new HashMap<>();

        for (String locale : locales) {
            if (locale.equals(defaultLocale)) continue;

            Map<String, String> translations = i18nTranslationService.getTranslations("product", product.getId(), locale);

            String name = getTranslationSafe(translations, "name", locale, englishName);
            String description = getTranslationSafe(translations, "description", locale, englishDescription);

            nameI18nOther.put(locale, name);
            descriptionI18nOther.put(locale, description);
        }

        dto.setName_i18n_other(nameI18nOther);
        dto.setDescription_i18n_other(descriptionI18nOther);
        return dto;
    }

    private String getTranslationSafe(Map<String, ?> translations,
                                      String field,
                                      String locale,
                                      String defaultValue) {
        if (translations == null) {
            return defaultValue;
        }

        Object value = translations.get(field);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof String) {
            return (String) value;
        }

        // 如果是 Map，取 locale 对应的值
        if (value instanceof Map) {
            Map<String, Object> mapValue = (Map<String, Object>) value;
            Object v = mapValue.get(locale);
            return v != null ? v.toString() : defaultValue;
        }

        // 兜底转换
        return value.toString();
    }

    /**
     * 根据商品ID获取属性值标签列表（优化：使用批量查询，避免全量查询）
     */
    private List<AttrValueTagDTO> getAttrValueTagsByProductId(Long productId) {
        if (productId == null) {
            return Collections.emptyList();
        }
        
        try {
            // 1. 查询商品属性关联
            Map<String, Object> params = new HashMap<>();
            params.put("EQ_productId", productId);
            List<ShopProductAttr> productAttrs = shopProductAttrService.query(params, null);
            
            if (productAttrs == null || productAttrs.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 2. 提取属性值ID列表
            List<Long> attrValueIds = productAttrs.stream()
                    .map(ShopProductAttr::getAttrValueId)
                    .filter(id -> id != null)
                    .distinct()
                    .collect(Collectors.toList());
            
            if (attrValueIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 3. 批量查询属性值（使用IN查询，避免全量查询）
            Map<String, Object> attrValueParams = new HashMap<>();
            attrValueParams.put("IN_id", attrValueIds);
            List<ShopAttrValue> attrValues = shopAttrValueService.query(attrValueParams, null);
            
            if (attrValues == null || attrValues.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 4. 批量查询属性（用于获取属性名称）
            Set<Long> attrIds = attrValues.stream()
                    .map(ShopAttrValue::getAttrId)
                    .filter(id -> id != null)
                    .collect(Collectors.toSet());
            
            Map<Long, String> attrNameMap = new HashMap<>();
            if (!attrIds.isEmpty()) {
                Map<String, Object> attrParams = new HashMap<>();
                attrParams.put("IN_id", new ArrayList<>(attrIds));
                List<ShopAttr> attrs = shopAttrService.query(attrParams, null);
                if (attrs != null) {
                    attrNameMap = attrs.stream()
                            .collect(Collectors.toMap(
                                    ShopAttr::getId,
                                    ShopAttr::getName,
                                    (a, b) -> a
                            ));
                }
            }
            
            // 5. 构建DTO列表
            final Map<Long, String> finalAttrNameMap = attrNameMap;
            return attrValues.stream()
                    .map(attrValue -> {
                        AttrValueTagDTO dto = new AttrValueTagDTO();
                        dto.setId(attrValue.getId());
                        dto.setValue(attrValue.getValue());
                        // 从Map获取属性名称，避免N+1查询
                        String attrName = finalAttrNameMap.getOrDefault(attrValue.getAttrId(), "未知属性");
                        dto.setAttrName(attrName);
                        return dto;
                    })
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("❌ Failed to get attr value tags, productId={}, reason={}", 
                    productId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}

