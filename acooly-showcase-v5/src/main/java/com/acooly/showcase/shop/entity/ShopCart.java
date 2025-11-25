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
 * shop_cart Entity
 *
 * @author acooly
 * @date 2025-11-01 23:21:31
 */
@Entity
@Table(name = "shop_cart")
@Getter
@Setter
@ExportModel(name = "shop_cart", border = true, headerShow = true)
public class ShopCart extends AbstractEntity {

    /**
     * user_id
     */
    @ExportColumn(header = "user_id", order = 1)
    private Integer userId;

    /**
     * total_price
     */
	@Size(max = 12)
    @ExportColumn(header = "total_price", order = 2)
    private String totalPrice;

}
