package com.acooly.showcase.facade.customer.api;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.showcase.facade.customer.order.CreateCustomerOrder;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-20 15:54
 */
public interface CustomerFacade {

  ResultBase createCustomer(CreateCustomerOrder createCustomerOrder);
}
