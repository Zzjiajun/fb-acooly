package com.acooly.showcase.shop.service;

import com.acooly.showcase.shop.entity.ShopProductImages;
import com.acooly.showcase.shop.utils.RedisShopUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ImageCacheService {

    @Autowired
    private RedisShopUtil redisUtil;

    @Value("${image.cache.metadata.ttl:3600}")
    private long metadataTtl;

    @Value("${image.cache.primary.ttl:1800}")
    private long primaryTtl;

    private static final String CACHE_PREFIX = "product.image";

    /**
     * 缓存商品图片元数据
     */
    public void cacheImageMetadata(Long productId, List<ShopProductImages> images) {
        if (productId == null || images == null) {
            return;
        }

        String key = redisUtil.buildKey(CACHE_PREFIX, "meta", String.valueOf(productId));
        try {
            redisUtil.set(key, images, metadataTtl, TimeUnit.SECONDS);
            log.debug("缓存图片元数据 - 商品ID: {}, 图片数: {}", productId, images.size());
        } catch (Exception e) {
            log.error("缓存图片元数据失败 - 商品ID: {}", productId, e);
        }
    }

    /**
     * 获取商品图片元数据（从缓存）
     */
    @SuppressWarnings("unchecked")
    public List<ShopProductImages> getImageMetadata(Long productId) {
        if (productId == null) {
            return null;
        }

        String key = redisUtil.buildKey(CACHE_PREFIX, "meta", String.valueOf(productId));
        try {
            Object cached = redisUtil.get(key);
            if (cached instanceof List) {
                return (List<ShopProductImages>) cached;
            }
        } catch (Exception e) {
            log.error("获取图片元数据失败 - 商品ID: {}", productId, e);
        }
        return null;
    }

    /**
     * 缓存商品主图信息
     */
    public void cachePrimaryImage(Long productId, ShopProductImages primaryImage) {
        if (productId == null) {
            return;
        }

        String key = redisUtil.buildKey(CACHE_PREFIX, "primary", String.valueOf(productId));
        try {
            redisUtil.set(key, primaryImage, primaryTtl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存主图信息失败 - 商品ID: {}", productId, e);
        }
    }

    /**
     * 获取商品主图（从缓存）
     */
    public ShopProductImages getPrimaryImage(Long productId) {
        if (productId == null) {
            return null;
        }

        String key = redisUtil.buildKey(CACHE_PREFIX, "primary", String.valueOf(productId));
        try {
            Object cached = redisUtil.get(key);
            if (cached instanceof ShopProductImages) {
                return (ShopProductImages) cached;
            }
        } catch (Exception e) {
            log.error("获取主图信息失败 - 商品ID: {}", productId, e);
        }
        return null;
    }

    /**
     * 清除商品图片缓存
     */
    public void invalidateCache(Long productId) {
        if (productId == null) {
            return;
        }

        try {
            // 删除元数据缓存
            String metaKey = redisUtil.buildKey(CACHE_PREFIX, "meta", String.valueOf(productId));
            redisUtil.del(metaKey);

            // 删除主图缓存
            String primaryKey = redisUtil.buildKey(CACHE_PREFIX, "primary", String.valueOf(productId));
            redisUtil.del(primaryKey);

            log.debug("清除图片缓存 - 商品ID: {}", productId);
        } catch (Exception e) {
            log.error("清除图片缓存失败 - 商品ID: {}", productId, e);
        }
    }

    /**
     * 批量清除缓存
     */
    public void batchInvalidateCache(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }

        for (Long productId : productIds) {
            invalidateCache(productId);
        }
    }
}
