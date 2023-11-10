package com.acooly.showcase.daliy.dto;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class parseDto extends InfoBase {
    private List<String> photo;
}
