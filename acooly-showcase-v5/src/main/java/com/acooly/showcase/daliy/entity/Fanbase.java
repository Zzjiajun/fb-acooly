package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dm_fanbase")
@Getter
@Setter
public class Fanbase extends AbstractEntity {
    private Integer groupId;
    private String groupName;
    private Integer groupQuantity;
}
