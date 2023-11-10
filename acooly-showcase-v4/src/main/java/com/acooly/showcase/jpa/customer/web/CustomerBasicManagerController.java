/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-10-12
 *
 */
package com.acooly.showcase.jpa.customer.web;

import com.acooly.core.common.web.AbstractManageController;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.showcase.common.enums.EducationLevel;
import com.acooly.showcase.common.enums.HouseStatue;
import com.acooly.showcase.common.enums.IncomeMonth;
import com.acooly.showcase.common.enums.MaritalStatus;
import com.acooly.showcase.jpa.customer.entity.CustomerBasic;
import com.acooly.showcase.jpa.customer.service.CustomerBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户基本信息 管理控制器
 *
 * @author acooly Date: 2016-10-12 18:00:43
 */
@Controller
@RequestMapping(value = "/manage/showcase/customerBasic")
public class CustomerBasicManagerController
    extends AbstractManageController<CustomerBasic, CustomerBasicService> {

  @SuppressWarnings("unused")
  @Autowired
  private CustomerBasicService customerBasicService;

  {
    allowMapping = "*";
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allMaritalStatuss", MaritalStatus.mapping());
    model.put("allEducationLevels", EducationLevel.mapping());
    model.put("allIncomeMonths", IncomeMonth.mapping());
    model.put("allHouseStatues", HouseStatue.mapping());
    model.put("allSocialInsurances", WhetherStatus.mapping());
    model.put("allHouseFunds", WhetherStatus.mapping());
    model.put("allCarStatuss", WhetherStatus.mapping());
  }
}
