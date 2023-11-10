package com.acooly.showcase.daliy.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSpending extends InfoBase {
    private Long id;
    private String username;
    private String realName;
    private String spending;
    private String email;
}
