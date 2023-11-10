/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-13
 *
 */
package com.acooly.showcase.jpa.customer.web;

import com.acooly.core.common.web.AbstractManageController;
import com.acooly.showcase.jpa.customer.entity.CustomerExtend;
import com.acooly.showcase.jpa.customer.service.CustomerExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * dm_customer_extend 管理控制器
 *
 * @author acooly Date: 2016-10-13 00:03:03
 */
@Controller
@RequestMapping(value = "/manage/showcase/customerExtend")
public class CustomerExtendManagerController
    extends AbstractManageController<CustomerExtend, CustomerExtendService> {

  @SuppressWarnings("unused")
  @Autowired
  private CustomerExtendService customerExtendService;

  {
    allowMapping = "*";
  }
}
