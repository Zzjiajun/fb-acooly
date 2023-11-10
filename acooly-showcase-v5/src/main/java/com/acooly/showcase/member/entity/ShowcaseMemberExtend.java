/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;

import java.util.Date;

/**
 * ac_showcase_member_extend Entity
 * 与ac_showcase_member主表共主键1v1
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Entity
@Table(name = "ac_showcase_member_extend")
@Getter
@Setter
@NoArgsConstructor
public class ShowcaseMemberExtend extends AbstractEntity {

    public ShowcaseMemberExtend(Long id, String context) {
        setId(id);
        this.context = context;
    }

    /**
     * context
     */
    private String context;

    /**
     * comments
     */
    @Size(max = 255)
    private String comments;

}
