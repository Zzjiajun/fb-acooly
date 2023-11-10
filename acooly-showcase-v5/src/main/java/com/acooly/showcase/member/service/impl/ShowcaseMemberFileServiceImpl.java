/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.member.service.ShowcaseMemberFileService;
import com.acooly.showcase.member.dao.ShowcaseMemberFileDao;
import com.acooly.showcase.member.entity.ShowcaseMemberFile;

/**
 * 会员附件 Service实现
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
@Service("showcaseMemberFileService")
public class ShowcaseMemberFileServiceImpl extends EntityServiceImpl<ShowcaseMemberFile, ShowcaseMemberFileDao> implements ShowcaseMemberFileService {

    @Override
    public Integer count() {
        return getEntityDao().count();
    }
}
