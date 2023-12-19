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
 * dm_link Entity
 *
 * @author acooly
 * @date 2023-12-14 17:10:52
 */
@Entity
@Table(name = "dm_link")
@Getter
@Setter
public class Link extends AbstractEntity {

    /**
     * 用户id

     */
    private Integer userId;

    /**
     * 持有者

     */
	@NotBlank
	@Size(max = 255)
    private String holder;

    /**
     * 二级域名

     */
	@Size(max = 255)
    private String domain;

    /**
     * region_name

     */
    @Size(max = 255)
    private String regionName;

    @Size(max = 255)
    private String accessAddress;

    @Size(max = 255)
    private String regional;
}
