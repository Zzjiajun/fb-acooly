/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-22
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
 * dm_domain Entity
 *
 * @author acooly
 * @date 2024-03-22 17:23:50
 */
@Entity
@Table(name = "dm_domain")
@Getter
@Setter
@ExportModel(name = "dm_domain", border = true, headerShow = true)
public class DmDomain extends AbstractEntity {

    /**
     * domain_name
     */
	@Size(max = 255)
    @ExportColumn(header = "domain_name", order = 1)
    private String domainName;

    /**
     * link
     */
	@Size(max = 255)
    @ExportColumn(header = "link", order = 2)
    private String link;

    /**
     * user_name
     */
	@Size(max = 255)
    @ExportColumn(header = "user_name", order = 3)
    private String userName;

}
