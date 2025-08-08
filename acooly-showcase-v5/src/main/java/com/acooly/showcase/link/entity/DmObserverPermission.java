/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-06-20
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
 * dm_observer_permission Entity
 *
 * @author acooly
 * @date 2025-06-20 21:45:07
 */
@Entity
@Table(name = "dm_observer_permission")
@Getter
@Setter
@ExportModel(name = "dm_observer_permission", border = true, headerShow = true)
public class DmObserverPermission extends AbstractEntity {

    /**
     * 观察者用户ID
     */
    @ExportColumn(header = "观察者用户ID", order = 1)
    private Long userId;

    /**
     * dmCenter记录ID
     */
	@NotNull
    @ExportColumn(header = "dmCenter记录ID", order = 2)
    private Long dmCenterId;

    /**
     * 授权时间
     */
	@NotNull
    @ExportColumn(header = "授权时间", order = 3)
    private Date grantTime;

    /**
     * 授权人
     */
	@NotBlank
	@Size(max = 50)
    @ExportColumn(header = "授权人", order = 4)
    private String grantBy;

    /**
     * 状态：1=有效，0=无效
     */
    @ExportColumn(header = "状态：1=有效，0=无效", order = 5)
    private Integer status;

}
