///*
// * www.yiji.com Inc.
// * Copyright (c) 2017 All Rights Reserved
// */
//
///*
// * 修订记录:
// * qiubo@yiji.com 2017-02-19 22:11 创建
// */
//package com.acooly.showcase.test.openapi;
//
//import com.acooly.core.utils.Money;
//import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
//import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
//import com.acooly.showcase.misc.openapi.PayOrderRequest;
//import com.acooly.showcase.misc.openapi.PayOrderResponse;
//import org.junit.Test;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///** @author qiubo@yiji.com */
//public class PayOrderApiServiceTest extends AbstractApiServieTests {
//  {
//    gatewayUrl = "http://localhost:8081/gateway.do";
//    partnerId = "test";
//  }
//
//  @Test
//  public void testPayOrder() throws Exception {
//    service = "payOrder";
//    Money amount = Money.amout("1000.00");
//    PayOrderRequest request = new PayOrderRequest();
//    request.setRequestNo(UUID.randomUUID().toString());
//    request.setAmount(amount);
//    request.setPayerUserId("09876543211234567890");
//    request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
//    PayOrderResponse response = request(request, PayOrderResponse.class);
//    assertThat(response).isNotNull();
//    assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PROCESSING.code());
//  }
//}
