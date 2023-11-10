package com.acooly.showcase.daliy.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissions extends InfoBase {
    private Long id;
    private String username;
    private String realName;
    private String pinyin;
    private int status;
}
