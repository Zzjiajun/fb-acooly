/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 *
 */
package com.acooly.showcase.member.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.member.entity.ShowcaseMemberFile;

/**
 * 会员附件 Service接口
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
public interface ShowcaseMemberFileService extends EntityService<ShowcaseMemberFile> {

    /**
     * 数据总数
     *
     * @return
     */
    Integer count();

}
