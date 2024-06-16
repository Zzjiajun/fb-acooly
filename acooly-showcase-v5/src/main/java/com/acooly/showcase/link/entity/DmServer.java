/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-26
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
 * dm_server Entity
 *
 * @author acooly
 * @date 2024-04-26 04:43:12
 */
@Entity
@Table(name = "dm_server")
@Getter
@Setter
@ExportModel(name = "dm_server", border = true, headerShow = true)
public class DmServer extends AbstractEntity {

    /**
     * ip地址
     */
	@Size(max = 255)
    @ExportColumn(header = "ip地址", order = 1)
    private String ip;

    /**
     * 用户名
     */
	@Size(max = 255)
    @ExportColumn(header = "用户名", order = 2)
    private String username;

    /**
     * 密码
     */
	@Size(max = 255)
    @ExportColumn(header = "密码", order = 3)
    private String password;

    /**
     * 端口号
     */
    @ExportColumn(header = "端口号", order = 4)
    private Integer host;

    /**
     * 备注
     */
	@Size(max = 255)
    @ExportColumn(header = "备注", order = 5)
    private String reg;

}
