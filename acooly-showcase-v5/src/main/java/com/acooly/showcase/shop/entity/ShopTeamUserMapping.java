/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-12-12
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
 * shop_team_user_mapping Entity
 *
 * @author acooly
 * @date 2025-12-12 21:26:30
 */
@Entity
@Table(name = "shop_team_user_mapping")
@Getter
@Setter
@ExportModel(name = "shop_team_user_mapping", border = true, headerShow = true)
public class ShopTeamUserMapping extends AbstractEntity {

    /**
     * team_id
     */
	@NotNull
    @ExportColumn(header = "team_id", order = 1)
    private Long teamId;

    /**
     * user_id
     */
	@NotNull
    @ExportColumn(header = "user_id", order = 2)
    private Long userId;

}
