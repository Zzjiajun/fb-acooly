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
 * link_srcs Entity
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Entity
@Table(name = "link_srcs")
@Getter
@Setter
@ExportModel(name = "link_srcs", border = true, headerShow = true)
public class LinkSrcs extends AbstractEntity {

    /**
     * link_src
     */
	@Size(max = 255)
    @ExportColumn(header = "link_src", order = 1)
    private String linkSrc;

    /**
     * user_name
     */
	@Size(max = 255)
    @ExportColumn(header = "user_name", order = 2)
    private String userName;

    /**
     * domain
     */
	@Size(max = 255)
    @ExportColumn(header = "domain", order = 3)
    private String domain;

}
