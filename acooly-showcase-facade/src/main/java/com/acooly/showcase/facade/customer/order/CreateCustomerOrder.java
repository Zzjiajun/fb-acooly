package com.acooly.showcase.facade.customer.order;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.showcase.facade.customer.dto.CustomerDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-20 16:18
 */
@Getter
@Setter
public class CreateCustomerOrder extends OrderBase {
  @Valid @NotNull private CustomerDto customerDto;
}
