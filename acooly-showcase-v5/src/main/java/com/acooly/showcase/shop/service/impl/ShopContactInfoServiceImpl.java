/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-12-01
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopContactInfoService;
import com.acooly.showcase.shop.dao.ShopContactInfoDao;
import com.acooly.showcase.shop.entity.ShopContactInfo;

/**
 * shop_contact_info Service实现
 *
 * @author acooly
 * @date 2025-12-01 20:10:49
 */
@Service("shopContactInfoService")
public class ShopContactInfoServiceImpl extends EntityServiceImpl<ShopContactInfo, ShopContactInfoDao> implements ShopContactInfoService {

}
