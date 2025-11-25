/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.dao.ShopProductsDao;
import com.acooly.showcase.shop.entity.ShopProducts;

/**
 * 商品表（关联子级分类） Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopProductsService")
public class ShopProductsServiceImpl extends EntityServiceImpl<ShopProducts, ShopProductsDao> implements ShopProductsService {

}
