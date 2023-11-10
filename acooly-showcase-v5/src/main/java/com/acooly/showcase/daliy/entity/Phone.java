package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dm_phone")
@Getter
@Setter
public class Phone extends AbstractEntity {
    private String groupName;
    private String phoneNumber;
    private Integer repeatCount;
}
