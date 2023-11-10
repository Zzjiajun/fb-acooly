/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.member.service.ShowcaseMemberExtendService;
import com.acooly.showcase.member.dao.ShowcaseMemberExtendDao;
import com.acooly.showcase.member.entity.ShowcaseMemberExtend;

/**
 * ac_showcase_member_extend Service实现
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Service("showcaseMemberExtendService")
public class ShowcaseMemberExtendServiceImpl extends EntityServiceImpl<ShowcaseMemberExtend, ShowcaseMemberExtendDao> implements ShowcaseMemberExtendService {

}
