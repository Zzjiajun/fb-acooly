/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-01
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
import java.util.Date;

/**
 * shop_order_items Entity
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Entity
@Table(name = "shop_order_items")
@Getter
@Setter
@ExportModel(name = "shop_order_items", border = true, headerShow = true)
public class ShopOrderItems extends AbstractEntity {

    /**
     * order_id
     */
	@NotNull
    @ExportColumn(header = "order_id", order = 1)
    private Integer orderId;

    /**
     * product_id
     */
	@NotNull
    @ExportColumn(header = "product_id", order = 2)
    private Integer productId;

    /**
     * product_name
     */
	@Size(max = 255)
    @ExportColumn(header = "product_name", order = 3)
    private String productName;

    /**
     * quantity
     */
    @ExportColumn(header = "quantity", order = 4)
    private Integer quantity;

    /**
     * price
     */
	@Size(max = 12)
    @ExportColumn(header = "price", order = 5)
    private String price;

    /**
     * total_price
     */
	@Size(max = 12)
    @ExportColumn(header = "total_price", order = 6)
    private String totalPrice;


    private String productImage; // 商品图片（冗余字段）


}
