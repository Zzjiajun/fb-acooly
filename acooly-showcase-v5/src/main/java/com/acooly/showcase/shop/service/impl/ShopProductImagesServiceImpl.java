/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopProductImagesService;
import com.acooly.showcase.shop.dao.ShopProductImagesDao;
import com.acooly.showcase.shop.entity.ShopProductImages;

/**
 * 商品图片表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopProductImagesService")
public class ShopProductImagesServiceImpl extends EntityServiceImpl<ShopProductImages, ShopProductImagesDao> implements ShopProductImagesService {

}
