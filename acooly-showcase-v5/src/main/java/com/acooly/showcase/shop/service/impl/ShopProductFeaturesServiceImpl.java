/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopProductFeaturesService;
import com.acooly.showcase.shop.dao.ShopProductFeaturesDao;
import com.acooly.showcase.shop.entity.ShopProductFeatures;

/**
 * 商品特性表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopProductFeaturesService")
public class ShopProductFeaturesServiceImpl extends EntityServiceImpl<ShopProductFeatures, ShopProductFeaturesDao> implements ShopProductFeaturesService {

}
