package com.acooly.showcase.shop.event;

import com.acooly.showcase.shop.entity.ShopProducts;
import lombok.Data;

@Data
public class CacheShopRedisEvent {
    private ShopProducts shopProducts;
    private Long brandId;
    private Long attrId;
    private Long attrValueId;
    private Long categoryId;
    private Boolean isSubCategory;
    private String action;
}
