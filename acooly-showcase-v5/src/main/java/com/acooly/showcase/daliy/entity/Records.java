package com.acooly.showcase.daliy.entity;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "dm_record")
@Getter
@Setter
public class Records extends AbstractEntity {
    @NotBlank
    @Size(max = 255)
    private String userName;
    private BigDecimal spending;
    private String recharge;
    private String region;
    private Integer grades;
    @NotBlank
    @Size(max = 255)
    private String accountInformation;
    private Date buildTime;
}
