/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopReviewsService;
import com.acooly.showcase.shop.dao.ShopReviewsDao;
import com.acooly.showcase.shop.entity.ShopReviews;

/**
 * 评论表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopReviewsService")
public class ShopReviewsServiceImpl extends EntityServiceImpl<ShopReviews, ShopReviewsDao> implements ShopReviewsService {

}
