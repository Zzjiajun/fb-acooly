package com.acooly.showcase.jpa.customer.facade;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.module.appservice.AppService;
import com.acooly.showcase.jpa.customer.entity.Customer;
import com.acooly.showcase.jpa.customer.service.CustomerService;
import com.acooly.showcase.facade.customer.api.CustomerFacade;
import com.acooly.showcase.facade.customer.order.CreateCustomerOrder;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-20 16:21
 */
@Service(version = "1.0")
public class CustomerFacadeImpl implements CustomerFacade {
  @Autowired private CustomerService customerService;

  @AppService(logPrefix = "创建会员服务")
  @Override
  public ResultBase createCustomer(CreateCustomerOrder createCustomerOrder) {
    Customer customer = new Customer();
    customer.from(createCustomerOrder.getCustomerDto());
    customerService.save(customer);
    return new ResultBase();
  }
}
