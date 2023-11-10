package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "dm_account")
@Getter
@Setter
public class Accounts extends AbstractEntity {

    @NotBlank
    @Size(max = 255)
    private String accountName;
    @NotBlank
    @Size(max = 64)
    private String mail;
    @NotBlank
    @Size(max = 255)
    private String holder;
    private Float balance;
    @NotBlank
    @Size(max = 32)
    private String mobileNo;
    private int status;
    private String comments;
    private Date deprecatedTime;
}
