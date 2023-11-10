//package com.acooly.showcase.test.dubbo;
//
//import com.acooly.core.common.boot.Apps;
//import com.acooly.core.common.facade.ResultBase;
//import com.acooly.core.common.facade.ResultCode;
//import com.acooly.core.utils.Ids;
//import com.acooly.core.utils.enums.ResultStatus;
//import com.acooly.module.test.meta.DubboTest;
//import com.acooly.showcase.Main;
//import com.acooly.showcase.facade.customer.api.CustomerFacade;
//import com.acooly.showcase.facade.customer.dto.CustomerDto;
//import com.acooly.showcase.facade.customer.order.CreateCustomerOrder;
//import com.alibaba.dubbo.config.annotation.Reference;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///** @author qiubo@yiji.com */
//@RunWith(SpringRunner.class)
//@DubboTest
//@Slf4j
//public class CustomerFacadeTest {
//
//  static {
//    Apps.setProfileIfNotExists(Main.PROFILE);
//  }
//
//  @Reference(version = "1.0")
//  private CustomerFacade demoFacade;
//
//  @Test
//  public void testCreate() {
//    CreateCustomerOrder order = new CreateCustomerOrder();
//    order.setGid(Ids.gid());
//    order.setPartnerId("1");
//    CustomerDto customerDto = new CustomerDto();
//    customerDto.setRealName("xxx");
//    order.setCustomerDto(customerDto);
//    ResultBase result = demoFacade.createCustomer(order);
//    assertThat(result).isNotNull();
//    assertThat(result.getStatus()).isEqualTo(ResultStatus.failure);
//    assertThat(result.getCode()).isEqualTo(ResultCode.PARAMETER_ERROR.code());
//  }
//}
