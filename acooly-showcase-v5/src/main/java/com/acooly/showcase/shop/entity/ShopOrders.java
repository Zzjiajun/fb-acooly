/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-10-28
*/
package com.acooly.showcase.shop.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_orders")
@Getter
@Setter
@ExportModel(name = "订单表", border = true, headerShow = true)
public class ShopOrders extends AbstractEntity {

    /**
     * 订单编号
     */
	@Size(max = 100)
    @ExportColumn(header = "订单编号", order = 1)
    private String orderId;

    /**
     * 用户ID
     */
	@NotNull
    @ExportColumn(header = "用户ID", order = 2)
    private Long userId;

    /**
     * 订单总金额
     */
	@NotNull
    @ExportColumn(header = "订单总金额", order = 3)
    private BigDecimal totalPrice;

    /**
     * 订单状态：PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "订单状态：PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED", order = 4)
    private String status;

    /**
     * 收货地址
     */
	@Size(max = 500)
    @ExportColumn(header = "收货地址", order = 5)
    private String shippingAddress;

    /**
     * 收货城市
     */
	@Size(max = 100)
    @ExportColumn(header = "收货城市", order = 6)
    private String shippingCity;

    /**
     * 邮政编码
     */
	@Size(max = 20)
    @ExportColumn(header = "邮政编码", order = 7)
    private String shippingZipCode;

    /**
     * 收货国家
     */
	@Size(max = 100)
    @ExportColumn(header = "收货国家", order = 8)
    private String shippingCountry;

    /**
     * 联系电话
     */
	@Size(max = 20)
    @ExportColumn(header = "联系电话", order = 9)
    private String contactPhone;

    /**
     * 支付方式：CREDIT_CARD, PAYPAL, STRIPE, ALIPAY, WECHAT_PAY, BANK_TRANSFER
     */
	@Size(max = 50)
    @ExportColumn(order = 10)
    private String paymentMethod;

    private String thirdPartyPaymentId;

    /**
     * 取消原因
     */
	@Size(max = 500)
    @ExportColumn(header = "取消原因", order = 11)
    private String cancelReason;

    private String ccNumber;

    private String ccName;

    private Long couponId;


}
