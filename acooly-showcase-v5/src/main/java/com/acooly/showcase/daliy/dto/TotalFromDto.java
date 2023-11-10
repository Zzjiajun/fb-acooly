package com.acooly.showcase.daliy.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TotalFromDto extends InfoBase {
    private String accountName;
    private Float balance;
    private Float spending;
    private int status;
    private Float recharge;
    private String outAmount;
    private String inAmount;
    private String outStr;
    private String inStr;
    private Date createTime;
    private Date deprecatedTime;
}
