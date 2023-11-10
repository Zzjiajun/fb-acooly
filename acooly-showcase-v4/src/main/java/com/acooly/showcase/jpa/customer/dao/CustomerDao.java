/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.showcase.jpa.customer.entity.Customer;

/**
 * 客户信息表 JPA Dao
 *
 * <p>Date: 2016-09-23 13:32:34
 *
 * @author Acooly Code Generator
 */
public interface CustomerDao extends EntityJpaDao<Customer, Long> {

  Customer findByUsername(String username);
}
