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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车表 Entity
 *
 * @author acooly
 * @date 2025-11-01 23:25:13
 */
@Entity
@Table(name = "shop_cart_item")
@Getter
@Setter
@ExportModel(name = "购物车表", border = true, headerShow = true)
public class ShopCartItem extends AbstractEntity {

    /**
     * cart_id
     */
    @ExportColumn(header = "cart_id", order = 1)
    private Integer cartId;

    /**
     * 用户ID
     */
	@NotNull
    @ExportColumn(header = "用户ID", order = 2)
    private Long userId;

    /**
     * 商品ID
     */
	@NotNull
    @ExportColumn(header = "商品ID", order = 3)
    private Long productId;

    /**
     * 商品数量
     */
	@NotNull
    @ExportColumn(header = "商品数量", order = 4)
    private Integer quantity;

    /**
     * price
     */
    @ExportColumn(header = "price", order = 5)
    private BigDecimal price;

    /**
     * total_price
     */
    @ExportColumn(header = "total_price", order = 6)
    private BigDecimal totalPrice;

    private String productImage;

}
