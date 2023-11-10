/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-09-23
 *
 */
package com.acooly.showcase.jpa.customer.portal;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.utils.Ids;
import com.acooly.showcase.facade.customer.api.CustomerFacade;
import com.acooly.showcase.facade.customer.dto.CustomerDto;
import com.acooly.showcase.facade.customer.order.CreateCustomerOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author acooly Date: 2016-09-23 13:32:34
 */
@Controller
@RequestMapping(value = "/showcase/dubbo")
public class CustomerFacadeController extends AbstractStandardEntityController {

    @Reference(version = "1.0")
    private CustomerFacade demoFacade;

    @RequestMapping("testCreate")
    @ResponseBody
    public ResultBase testCreate(HttpServletRequest request) {
        CreateCustomerOrder order = new CreateCustomerOrder();
        order.setGid(Ids.gid());
        order.setPartnerId("1");
        CustomerDto customerDto = new CustomerDto();
        customerDto.setRealName("xxx");
        order.setCustomerDto(customerDto);
        return demoFacade.createCustomer(order);
    }

}
