/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-10-28
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopContactMessagesService;
import com.acooly.showcase.shop.dao.ShopContactMessagesDao;
import com.acooly.showcase.shop.entity.ShopContactMessages;

/**
 * 联系消息表 Service实现
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Service("shopContactMessagesService")
public class ShopContactMessagesServiceImpl extends EntityServiceImpl<ShopContactMessages, ShopContactMessagesDao> implements ShopContactMessagesService {

}
