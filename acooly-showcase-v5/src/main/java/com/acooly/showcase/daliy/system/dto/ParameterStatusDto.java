package com.acooly.showcase.daliy.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ParameterStatusDto {
    private String region ;
    private int allCount;
    private int landingCount;
    private int formsCount;
}
