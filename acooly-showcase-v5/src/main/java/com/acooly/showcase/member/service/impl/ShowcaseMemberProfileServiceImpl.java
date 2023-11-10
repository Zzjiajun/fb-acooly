/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-27
 */
package com.acooly.showcase.member.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.member.service.ShowcaseMemberProfileService;
import com.acooly.showcase.member.dao.ShowcaseMemberProfileDao;
import com.acooly.showcase.member.entity.ShowcaseMemberProfile;

/**
 * 会员配置信息 Service实现
 *
 * @author acooly
 * @date 2021-10-27 13:24:15
 */
@Service("showcaseMemberProfileService")
public class ShowcaseMemberProfileServiceImpl extends EntityServiceImpl<ShowcaseMemberProfile, ShowcaseMemberProfileDao> implements ShowcaseMemberProfileService {
    @Override
    public Integer count() {
        return getEntityDao().count();
    }
}
