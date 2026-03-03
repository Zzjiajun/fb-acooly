/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-11-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopConfigService;
import com.acooly.showcase.shop.dao.ShopConfigDao;
import com.acooly.showcase.shop.entity.ShopConfig;

/**
 * shop_config Service实现
 *
 * @author acooly
 * @date 2025-11-28 23:22:47
 */
@Service("shopConfigService")
public class ShopConfigServiceImpl extends EntityServiceImpl<ShopConfig, ShopConfigDao> implements ShopConfigService {

}
