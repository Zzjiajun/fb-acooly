/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-13
 *
 */
package com.acooly.showcase.jpa.customer.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.jpa.customer.dao.CustomerExtendDao;
import com.acooly.showcase.jpa.customer.entity.CustomerExtend;
import com.acooly.showcase.jpa.customer.service.CustomerExtendService;
import org.springframework.stereotype.Service;

/**
 * dm_customer_extend Service实现
 *
 * <p>Date: 2016-10-13 00:03:03
 *
 * @author acooly
 */
@Service("customerExtendService")
public class CustomerExtendServiceImpl extends EntityServiceImpl<CustomerExtend, CustomerExtendDao>
    implements CustomerExtendService {}
