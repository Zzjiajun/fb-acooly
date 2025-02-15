/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-10-31
*/
package com.acooly.showcase.link.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import java.util.Date;

/**
 * dm_modles Entity
 *
 * @author acooly
 * @date 2024-10-31 23:52:11
 */
@Entity
@Table(name = "dm_modles")
@Getter
@Setter
@ExportModel(name = "dm_modles", border = true, headerShow = true)
public class DmModles extends AbstractEntity {

    /**
     * 机型名字
     */
	@Size(max = 255)
    @ExportColumn(header = "机型名字", order = 1)
    private String modelName;

    /**
     * 宽度
     */
    @ExportColumn(header = "宽度", order = 2)
    private Integer screenWidth;

    /**
     * screen_height
     */
    @ExportColumn(header = "screen_height", order = 3)
    private Integer screenHeight;

    /**
     * 像素
     */
    @ExportColumn(header = "像素", order = 4)
    private Integer pixelRatio;

    /**
     * is_delete
     */
    @ExportColumn(header = "is_delete", order = 5)
    private Integer isDelete;


}
