/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-27
 *
 */
package com.acooly.showcase.member.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.member.entity.ShowcaseMemberProfile;

/**
 * 会员配置信息 Service接口
 *
 * @author acooly
 * @date 2021-10-27 13:24:15
 */
public interface ShowcaseMemberProfileService extends EntityService<ShowcaseMemberProfile> {

    /**
     * 数据总数
     *
     * @return
     */
    Integer count();
}
