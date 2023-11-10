/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by zhangpu@acooly.cn
* date:2021-12-19
*/
package com.acooly.showcase.demo.usergroup.entity;


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
 * 用户动态分组 Entity
 *
 * @author zhangpu@acooly.cn
 * @date 2021-12-19 16:45:03
 */
@Entity
@Table(name = "ac_showcase_user_group")
@Getter
@Setter
public class UserGroup extends AbstractEntity {

    /**
     * 名称

     */
	@NotBlank
	@Size(max = 45)
    private String name;

    /**
     * 描述

     */
	@Size(max = 225)
    private String descn;

    /**
     * 用户名

     */
	@Size(max = 512)
    private String users;

    /**
     * 备注

     */
	@Size(max = 255)
    private String comments;

}
