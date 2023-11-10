/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-10-26
*/
package com.acooly.showcase.member.entity;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.showcase.member.common.enums.FileTypeEnum;
import java.util.Date;

/**
 * 会员附件 Entity
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
@Entity
@Table(name = "ac_showcase_member_file")
@Getter
@Setter
public class ShowcaseMemberFile extends AbstractEntity {

    /**
     * 文件ID

     */
	@NotBlank
	@Size(max = 64)
    private String objectId;

    /**
     * 用户编码

     */
	@NotBlank
	@Size(max = 64)
    private String memberNo;

    /**
     * 用户名

     */
	@NotBlank
	@Size(max = 32)
    private String username;

    /**
     * 文件标题

     */
	@Size(max = 45)
    private String fileTitle;

    /**
     * 文件名

     */
	@NotBlank
	@Size(max = 45)
    private String fileName;

    /**
     * 文件路径

     */
	@NotBlank
	@Size(max = 128)
    private String filePath;

    /**
     * 文件扩展名

     */
	@Size(max = 16)
    private String fileExt;

    /**
     * 文件类型

     */
    @Enumerated(EnumType.STRING)
	@NotNull
    private FileTypeEnum fileType;

    /**
     * 备注

     */
	@Size(max = 128)
    private String comments;

}
