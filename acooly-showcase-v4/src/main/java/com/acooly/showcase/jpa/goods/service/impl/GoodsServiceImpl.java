/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.jpa.goods.dao.GoodsDao;
import com.acooly.showcase.jpa.goods.entity.Goods;
import com.acooly.showcase.jpa.goods.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * 商品信息 Service实现
 *
 * <p>Date: 2017-09-12 22:42:00
 *
 * @author acooly
 */
@Service("goodsService")
public class GoodsServiceImpl extends EntityServiceImpl<Goods, GoodsDao> implements GoodsService {}
