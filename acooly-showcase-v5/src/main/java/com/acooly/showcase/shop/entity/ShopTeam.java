/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-12
*/
package com.acooly.showcase.shop.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
 * shop_team Entity
 *
 * @author acooly
 * @date 2025-12-12 21:26:31
 */
@Entity
@Table(name = "shop_team")
@Getter
@Setter
@ExportModel(name = "shop_team", border = true, headerShow = true)
public class ShopTeam extends AbstractEntity {

    /**
     * 团队分享链接
     */
	@Size(max = 255)
    @ExportColumn(header = "团队分享链接", order = 1)
    private String teamLink;

    /**
     * 团队名称
     */
	@Size(max = 255)
    @ExportColumn(header = "团队名称", order = 2)
    private String teamName;

    /**
     * user_ids
     */
	@Size(max = 255)
    @ExportColumn(header = "用户IDList", order = 3)
    private String userIds;
    private String creator;
    private String code;
    private String teamRegisterLink;

    /**
     * 团队成员用户名（临时字段，用于显示）
     */
    @Transient
    private String memberNames;

}
