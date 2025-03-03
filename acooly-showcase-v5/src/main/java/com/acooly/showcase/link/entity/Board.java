/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-19
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
 * dm_board Entity
 *
 * @author acooly
 * @date 2025-02-19 18:45:30
 */
@Entity
@Table(name = "dm_board")
@Getter
@Setter
@ExportModel(name = "dm_board", border = true, headerShow = true)
public class Board extends AbstractEntity {

    /**
     * 管理员名字
     */
	@Size(max = 255)
    @ExportColumn(header = "管理员名字", order = 1)
    private String manageName;

    /**
     * 被管理员工id
     */
	@Size(max = 255)
    @ExportColumn(header = "被管理员工id", order = 2)
    private String attachedName;
}
