package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dm_permissions")
@Getter
@Setter
public class Permissions extends AbstractEntity {
    private String userName;
}
