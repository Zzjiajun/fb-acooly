/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-28
*/
package com.acooly.showcase.link.entity;


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
 * link_int Entity
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Entity
@Table(name = "link_int")
@Getter
@Setter
@ExportModel(name = "link_int", border = true, headerShow = true)
public class LinkInt extends AbstractEntity {

    /**
     * 轮询目前的数量
     */
	@NotNull
    @ExportColumn(header = "轮询目前的数量", order = 1)
    private Integer countLink;

    /**
     * 用户名
     */
	@Size(max = 255)
    @ExportColumn(header = "用户名", order = 2)
    private String userName;

    /**
     * 域名
     */
	@Size(max = 255)
    @ExportColumn(header = "域名", order = 3)
    private String domain;

}
