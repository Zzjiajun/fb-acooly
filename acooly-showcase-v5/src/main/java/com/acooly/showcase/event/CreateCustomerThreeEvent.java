package com.acooly.showcase.event;

import lombok.Data;

@Data
public class CreateCustomerThreeEvent {
    private String filePath;
    private String newLink;
    private String host;
    private String username;
    private String password;
    //分割
    private String ipCountry;
    private Integer timeZone;
    private String timeContinent;
    private Integer isChinese;
    private Integer isMobile;
    private Integer isSpecificDevice;
    private Integer isFbclid;
    private Integer isIp;
    private Integer isVpn;
    private Integer vpnCode;
}
