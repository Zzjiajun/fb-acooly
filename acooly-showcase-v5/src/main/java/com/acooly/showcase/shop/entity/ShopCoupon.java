/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-11-07
*/
package com.acooly.showcase.shop.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * shop_coupon Entity
 *
 * @author acooly
 * @date 2025-11-07 17:01:53
 */
@Entity
@Table(name = "shop_coupon")
@Getter
@Setter
@ExportModel(name = "shop_coupon", border = true, headerShow = true)
public class ShopCoupon extends AbstractEntity {

    /**
     * code
     */
	@NotBlank
	@Size(max = 32)
    @ExportColumn(header = "code", order = 1)
    private String code;

    /**
     * 'percent' or 'amount'
     */
	@NotBlank
	@Size(max = 10)
    @ExportColumn(header = "'percent' or 'amount'", order = 2)
    private String discountType;

    /**
     * discount_value
     */
	@NotNull
    @ExportColumn(header = "discount_value", order = 3)
    private BigDecimal discountValue;

    /**
     * 券可被全局使用的总次数，0 表示不限
     */
    @ExportColumn(header = "券可被全局使用的总次数，0 表示不限", order = 4)
    private Integer totalUses;

    /**
     * 已被使用次数
     */
    @ExportColumn(header = "已被使用次数", order = 5)
    private Integer usedCount;

    /**
     * 每个用户最多使用次数，0 表示不限
     */
    @ExportColumn(header = "每个用户最多使用次数，0 表示不限", order = 6)
    private Integer perUserLimit;

    /**
     * ACTIVE/INACTIVE/EXPIRED
     */
	@Size(max = 16)
    @ExportColumn(header = "ACTIVE/INACTIVE/EXPIRED", order = 7)
    private String status;

    /**
     * 有效期开始
     */
    @ExportColumn(header = "有效期开始", order = 8)
    private Date validFrom;

    /**
     * 有效期结束
     */
    @ExportColumn(header = "有效期结束", order = 9)
    private Date validTo;

    /**
     * created_by
     */
    @ExportColumn(header = "created_by", order = 10)
    private String createdBy;

    @ExportColumn(header = "支付成功总金额", order = 11)
    private BigDecimal paySuccessCount;

}
