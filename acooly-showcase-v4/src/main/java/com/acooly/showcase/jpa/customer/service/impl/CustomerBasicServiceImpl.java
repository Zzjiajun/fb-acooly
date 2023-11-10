/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-12
 *
 */
package com.acooly.showcase.jpa.customer.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.jpa.customer.dao.CustomerBasicDao;
import com.acooly.showcase.jpa.customer.entity.CustomerBasic;
import com.acooly.showcase.jpa.customer.service.CustomerBasicService;
import org.springframework.stereotype.Service;

/**
 * 客户基本信息 Service实现
 *
 * <p>Date: 2016-10-12 18:00:43
 *
 * @author acooly
 */
@Service("customerBasicService")
public class CustomerBasicServiceImpl extends EntityServiceImpl<CustomerBasic, CustomerBasicDao>
    implements CustomerBasicService {}
