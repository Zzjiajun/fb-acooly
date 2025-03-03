/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.entity;


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

import java.math.BigInteger;
import java.util.Date;

/**
 * em_sumdata Entity
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Entity
@Table(name = "em_sumdata")
@Getter
@Setter
@ExportModel(name = "em_sumdata", border = true, headerShow = true)
public class EmSumdata extends AbstractEntity {

    /**
     * 群名
     */
	@Size(max = 255)
    @ExportColumn(header = "群名", order = 1)
    private String groupName;

    /**
     * 业务
     */
	@Size(max = 255)
    @ExportColumn(header = "业务", order = 2)
    private String business;

    /**
     * 国家
     */
	@Size(max = 255)
    @ExportColumn(header = "国家", order = 3)
    private String country;

    /**
     * 备注
     */
	@Size(max = 255)
    @ExportColumn(header = "备注", order = 4)
    private String remark;

    /**
     * 电话
     */
	@Size(max = 255)
    @ExportColumn(header = "电话", order = 5)
    private String phone;

    /**
     * 股民
     */
	@Size(max = 255)
    @ExportColumn(header = "股民", order = 6)
    private String share;

    /**
     * 意向
     */
	@Size(max = 255)
    @ExportColumn(header = "意向", order = 7)
    private String intent;
    /**
     * 类型表id
     */
    @ExportColumn(header = "类型表id", order = 8)
    private BigInteger stampId;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @ExportColumn(header = "姓名", order = 10)
    private String name;

    @ExportColumn(header = "邮箱", order = 11)
    private String email;
}
