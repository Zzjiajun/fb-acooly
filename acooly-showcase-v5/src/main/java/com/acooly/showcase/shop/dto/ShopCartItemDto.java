package com.acooly.showcase.shop.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShopCartItemDto {
    private Long id;
    private Integer cartId;
    private Long userId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String productImage;
    private Date createTime ;
}
