/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.jpa.customer.entity.Customer;

/**
 * 客户信息表 Service接口
 *
 * <p>Date: 2016-09-23 13:32:34
 *
 * @author acooly
 */
public interface CustomerService extends EntityService<Customer> {

  void checkUniqueUsername(String username);
}
