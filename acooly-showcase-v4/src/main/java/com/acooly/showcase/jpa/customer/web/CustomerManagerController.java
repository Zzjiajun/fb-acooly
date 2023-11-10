/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.web;

import com.acooly.core.common.web.AbstractManageController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.IdCards;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Strings;
import com.acooly.showcase.common.enums.CustomerType;
import com.acooly.showcase.common.enums.IdcardType;
import com.acooly.showcase.jpa.customer.entity.Customer;
import com.acooly.showcase.jpa.customer.service.CustomerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户信息表 管理控制器
 *
 * @author acooly Date: 2016-09-23 13:32:34
 */
@Controller
@RequestMapping(value = "/manage/showcase/customer")
public class CustomerManagerController extends AbstractManageController<Customer, CustomerService> {

  @Autowired private CustomerService customerService;

  {
    allowMapping = "*";
  }

  @Override
  public JsonEntityResult<Customer> saveJson(HttpServletRequest request, HttpServletResponse response) {
    SecurityUtils.getSubject().checkPermission("customer:create");
    return super.saveJson(request, response);
  }

  @Override
  protected Customer onSave(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      Customer entity,
      boolean isCreate)
      throws Exception {
    String salaryMoney = request.getParameter("salaryMoney");
    Money money = Money.zero();
    if (Strings.isNotBlank(salaryMoney)) {
      money = Money.amout(salaryMoney);
    }
    entity.setSalary(money.getCent());
    return entity;
  }

  /** 导出标题 */
  @Override
  protected List<String> getExportTitles() {
    return Lists.newArrayList(new String[] {"编号", "用户名", "姓名", "生日", "性别", "手机"});
  }

  /** 导出处理 */
  @Override
  protected List<String> doExportEntity(Customer entity) {
    List<String> row = Lists.newArrayList();
    row.add(String.valueOf(entity.getId()));
    row.add(entity.getUsername());
    row.add(entity.getRealName());
    // 如果不是必填数据项，请判断是否为空
    row.add(entity.getBirthday() == null ? "" : Dates.format(entity.getBirthday(), "yyyy-MM-dd"));
    row.add(OptionMapping.allGenders.get(entity.getGender()));
    row.add(entity.getMobileNo());
    return row;
  }

  /**
   * 导入处理
   *
   * <p>导入前，需要准备模板，用户更加模板上传后，在这里处理每行数据转换为entity
   *
   * <p>列说明（按顺序）：用户名,姓名,身份证号码,手机,邮箱
   */
  @Override
  protected Customer doImportEntity(List<String> fields) {

    String certNo = fields.get(2);
    IdCards.IdCardInfo cardInfo = IdCards.parse(certNo);
    Customer customer = new Customer();
    customer.setIdcardType(IdcardType.cert);
    customer.setIdcardNo(certNo);
    Date birthday = Dates.parse(cardInfo.getBirthday(), "yyyyMMdd");
    customer.setBirthday(birthday);
    customer.setAge(Dates.getYear(new Date()) - Dates.getYear(birthday));
    customer.setGender(
        Strings.equals(cardInfo.getGender().code(), "male") ? Customer.GENDER_MAN : Customer.GENDER_HUMAN);

    customer.setUsername(Strings.trimToEmpty(fields.get(0)));
    customer.setRealName(Strings.trimToEmpty(fields.get(1)));
    customer.setMobileNo(Strings.trimToEmpty(fields.get(3)));
    customer.setMail(Strings.trimToEmpty(fields.get(4)));

    customer.setSalary(0l);
    customer.setCustomerType(CustomerType.normal);
    customer.setFee(1.2);
    customer.setStatus(Customer.STATUS_ENABLE);

    return customer;
  }

  /**
   * 验证用户名是否重复
   *
   * @param request
   * @param response
   * @return JsonResult.success() == true: OK
   */
  @RequestMapping("checkUniqueUsername")
  @ResponseBody
  public JsonResult checkUniqueUsername(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    try {
      String username = request.getParameter("username");
      if (Strings.isBlank(username)) {
        throw new IllegalArgumentException("用户名不能为空");
      }
      getEntityService().checkUniqueUsername(username);
    } catch (Exception e) {
      handleException(result, "用户名验重", e);
    }
    return result;
  }

  /**
   * 解析身份证号码
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("parseIdCard")
  @ResponseBody
  public JsonEntityResult<IdCards.IdCardInfo> parseIdCard(
      HttpServletRequest request, HttpServletResponse response) {
    JsonEntityResult<IdCards.IdCardInfo> result = new JsonEntityResult();
    try {
      String idcard = request.getParameter("idcard");
      if (Strings.isBlank(idcard)) {
        throw new IllegalArgumentException("身份证号码不能为空");
      }

      IdCards.IdCardInfo idCardInfo = IdCards.parse(idcard);
      result.setEntity(idCardInfo);
    } catch (Exception e) {
      handleException(result, "身份证号码解析", e);
    }
    return result;
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allGenders", OptionMapping.allGenders);
    model.put("allIdcardTypes", IdcardType.mapping());
    model.put("allCustomerTypes", CustomerType.mapping());
    model.put("allStatuss", OptionMapping.allStatuss);
  }

  public static class OptionMapping {

    public static Map<Integer, String> allGenders = Maps.newLinkedHashMap();
    public static Map<Integer, String> allStatuss = Maps.newLinkedHashMap();

    static {
      allGenders.put(Customer.GENDER_MAN, "男");
      allGenders.put(Customer.GENDER_HUMAN, "女");
    }

    static {
      allStatuss.put(Customer.STATUS_DISABLE, "禁用");
      allStatuss.put(Customer.STATUS_FROZEN, "冻结");
      allStatuss.put(Customer.STATUS_PAUSE, "暂停");
      // 以下是正常的
      allStatuss.put(Customer.STATUS_ENABLE, "正常");
      allStatuss.put(Customer.STATUS_GOOD, "优秀");
      allStatuss.put(Customer.STATUS_EXCITED, "亢奋");
    }
  }
}
