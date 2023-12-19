/*
* acooly.cn Inc.
* Copyright (c) 2023 All Rights Reserved.
* create by acooly
* date:2023-12-14
*/
package com.acooly.showcase.daliy.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * dm_regname Entity
 *
 * @author acooly
 * @date 2023-12-14 17:39:27
 */
@Entity
@Table(name = "dm_regname")
@Getter
@Setter
public class Regname extends AbstractEntity {

    /**
     * user_id

     */
	@NotNull
    private Integer userId;

    /**
     * name

     */
	@Size(max = 255)
    private String name;

    /**
     * region_name

     */
	@Size(max = 255)
    private String regionName;

}
