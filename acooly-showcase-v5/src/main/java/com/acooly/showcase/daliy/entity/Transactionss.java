package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dm_transactions")
@Getter
@Setter
public class Transactionss extends AbstractEntity {
    private String outAccount;
    private String inAccount;
    private Float amount;
    private String userName;
    private String accountManager;
}
