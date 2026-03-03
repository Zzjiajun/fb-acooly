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
import java.util.Date;

/**
 * 评论表 Entity
 *
 * @author acooly
 * @date 2025-10-28 00:50:14
 */
@Entity
@Table(name = "shop_reviews")
@Getter
@Setter
@ExportModel(name = "评论表", border = true, headerShow = true)
public class ShopReviews extends AbstractEntity {

    /**
     * 商品ID
     */
	@NotNull
    @ExportColumn(header = "商品ID", order = 1)
    private Long productId;

    /**
     * 用户ID
     */
	@NotNull
    @ExportColumn(header = "用户ID", order = 2)
    private Long userId;

    /**
     * 评分（1-5）
     */
	@NotNull
    @ExportColumn(header = "评分（1-5）", order = 3)
    private Integer rating;

    /**
     * 评论内容
     */
    @ExportColumn(header = "评论内容", order = 4)
    private String comment;

    /**
     * 是否验证购买
     */
    @ExportColumn(header = "是否验证购买", order = 5)
    private Integer isVerified;

    /**
     * 有用数量
     */
    @ExportColumn(header = "有用数量", order = 6)
    private Integer helpfulCount;


}
