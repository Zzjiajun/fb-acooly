package com.acooly.showcase.daliy.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PerformanceDto extends InfoBase {
    private String region;
    private String spending;
    private String recharge;
    private Integer grades;
    private Date createTime;
}
