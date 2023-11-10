/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.common.ShowcaseResultCode;
import com.acooly.showcase.jpa.customer.dao.CustomerDao;
import com.acooly.showcase.jpa.customer.entity.Customer;
import com.acooly.showcase.jpa.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 客户信息表 Service实现
 *
 * <p>Date: 2016-09-23 13:32:34
 *
 * @author acooly
 */
@Slf4j
@Service("customerService")
public class CustomerServiceImpl extends EntityServiceImpl<Customer, CustomerDao>
    implements CustomerService {

  @Override
  public void checkUniqueUsername(String username) {
    try {
      Customer customer = getEntityDao().findByUsername(username);
      if (customer != null) {
        log.warn("用户名：{}，已经存在}", username);
        throw new BusinessException(ShowcaseResultCode.USERNAME_DOES_EXIST);
      }
    } catch (BusinessException be) {
      throw be;
    } catch (Exception e) {
      throw new BusinessException("用户名已存在");
    }
  }
}
