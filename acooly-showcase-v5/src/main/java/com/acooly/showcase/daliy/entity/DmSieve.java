/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-14
*/
package com.acooly.showcase.daliy.entity;


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
 * dm_sieve Entity
 *
 * @author acooly
 * @date 2025-02-14 17:32:44
 */
@Entity
@Table(name = "dm_sieve")
@Getter
@Setter
@ExportModel(name = "dm_sieve", border = true, headerShow = true)
public class DmSieve extends AbstractEntity {

    /**
     * 群名
     */
	@Size(max = 255)
    @ExportColumn(header = "群名", order = 1)
    private String name;

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
     * remark
     */
	@Size(max = 255)
    @ExportColumn(header = "remark", order = 4)
    private String remark;

    /**
     * 电话号
     */
	@Size(max = 255)
    @ExportColumn(header = "电话号", order = 5)
    private String phone;

    /**
     * 是否股民
     */
    @ExportColumn(header = "是否股民", order = 6)
    private Integer decide;

    /**
     * 意向
     */
	@Size(max = 255)
    @ExportColumn(header = "意向", order = 7)
    private String intent;

}
