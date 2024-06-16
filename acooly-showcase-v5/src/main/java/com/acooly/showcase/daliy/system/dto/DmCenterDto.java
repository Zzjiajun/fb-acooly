package com.acooly.showcase.daliy.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class DmCenterDto {
    private String userName;
    private String region;
    private Integer displayOption;
    private Integer totalVisits;
    private Integer totalClicks;

}
