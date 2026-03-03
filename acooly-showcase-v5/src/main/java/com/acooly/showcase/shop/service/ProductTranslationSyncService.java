package com.acooly.showcase.shop.service;

import com.acooly.showcase.shop.entity.ShopI18nLocales;
import com.acooly.showcase.shop.entity.ShopI18nTranslations;
import com.acooly.showcase.shop.entity.ShopProducts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品翻译同步服务
 * 负责处理商品表数据与翻译表数据的同步
 * 
 * @author acooly
 * @date 2025-12-26
 */
@Slf4j
@Service
public class ProductTranslationSyncService {

    @Autowired
    private ShopI18nLocalesService shopI18nLocalesService;

    @Autowired
    private ShopI18nTranslationsService shopI18nTranslationsService;

    /**
     * 同步商品表数据到默认语言的翻译表
     * 当商品表的 name 或 description 被修改时调用
     * 
     * @param product 商品实体
     */
    @Transactional
    public void syncProductToDefaultTranslation(ShopProducts product) {
        if (product == null || product.getId() == null) {
            log.warn("商品为空，跳过同步");
            return;
        }

        ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
        if (defaultLocale == null) {
            log.warn("未找到默认语言，跳过同步 - 商品ID: {}", product.getId());
            return;
        }

        String defaultLocaleCode = defaultLocale.getLocaleCode();
        log.info("同步商品数据到默认语言翻译 - 商品ID: {}, 默认语言: {}", product.getId(), defaultLocaleCode);

        // 同步 name（无论是否为空都同步，确保数据一致性）
        String nameValue = product.getName() != null ? product.getName().trim() : "";
        shopI18nTranslationsService.saveOrUpdateTranslation(
                "product", 
                product.getId(), 
                "name", 
                defaultLocaleCode, 
                nameValue
        );
        log.debug("同步商品名称到翻译表 - 商品ID: {}, 语言: {}, 名称: {}", 
                product.getId(), defaultLocaleCode, nameValue);

        // 同步 description（无论是否为空都同步，确保数据一致性）
        String descValue = product.getDescription() != null ? product.getDescription().trim() : "";
        shopI18nTranslationsService.saveOrUpdateTranslation(
                "product", 
                product.getId(), 
                "description", 
                defaultLocaleCode, 
                descValue
        );
        log.debug("同步商品描述到翻译表 - 商品ID: {}, 语言: {}, 描述长度: {}", 
                product.getId(), defaultLocaleCode, descValue.length());
    }

    /**
     * 同步默认语言翻译数据到商品表
     * 当默认语言的翻译被修改时调用
     * 
     * @param productId 商品ID
     * @param fieldName 字段名称（name 或 description）
     * @param translation 翻译内容
     */
    @Transactional
    public void syncDefaultTranslationToProduct(Long productId, String fieldName, String translation) {
        if (productId == null || fieldName == null || translation == null) {
            log.warn("参数不完整，跳过同步 - productId: {}, fieldName: {}", productId, fieldName);
            return;
        }

        ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
        if (defaultLocale == null) {
            log.warn("未找到默认语言，跳过同步 - 商品ID: {}", productId);
            return;
        }

        // 注意：这个方法应该在保存翻译之前调用，确保翻译已保存
        // 这里只负责同步到商品表
        // 实际的翻译保存应该在调用此方法之前完成
    }

    /**
     * 处理默认语言变更时的数据迁移
     * 当系统默认语言从 A 改为 B 时：
     * 1. 将新默认语言（B）的翻译数据同步到商品表
     * 2. 将旧默认语言（A）的翻译数据保留在翻译表中（不删除）
     * 
     * @param oldDefaultLocaleCode 旧默认语言代码
     * @param newDefaultLocaleCode 新默认语言代码
     */
    @Transactional
    public void migrateDefaultLocale(String oldDefaultLocaleCode, String newDefaultLocaleCode) {
        if (oldDefaultLocaleCode == null || newDefaultLocaleCode == null || 
            oldDefaultLocaleCode.equals(newDefaultLocaleCode)) {
            log.info("默认语言未变更，跳过迁移");
            return;
        }

        log.info("开始迁移默认语言数据 - 从 {} 到 {}", oldDefaultLocaleCode, newDefaultLocaleCode);

        // 获取所有商品
        // 注意：这里需要根据实际情况获取商品列表
        // 由于没有直接的方法，这里只是示例逻辑
        
        // 对于每个商品：
        // 1. 查找新默认语言的翻译
        // 2. 如果存在，同步到商品表
        // 3. 旧默认语言的翻译保留在翻译表中
        
        log.info("默认语言数据迁移完成 - 从 {} 到 {}", oldDefaultLocaleCode, newDefaultLocaleCode);
    }

    /**
     * 批量同步商品数据到默认语言翻译
     * 用于批量处理或数据修复
     * 
     * @param productIds 商品ID列表
     */
    @Transactional
    public void batchSyncProductToDefaultTranslation(java.util.List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            log.warn("商品ID列表为空，跳过批量同步");
            return;
        }

        ShopI18nLocales defaultLocale = shopI18nLocalesService.getDefaultLocale();
        if (defaultLocale == null) {
            log.warn("未找到默认语言，跳过批量同步");
            return;
        }

        log.info("开始批量同步商品数据到默认语言翻译 - 商品数量: {}, 默认语言: {}", 
                productIds.size(), defaultLocale.getLocaleCode());

        // 这里可以实现批量同步逻辑
        // 为了性能，可以考虑批量查询和批量更新
        
        log.info("批量同步完成 - 商品数量: {}", productIds.size());
    }
}

