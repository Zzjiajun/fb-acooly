/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.member.service.ShowcaseMemberContactService;
import com.acooly.showcase.member.dao.ShowcaseMemberContactDao;
import com.acooly.showcase.member.entity.ShowcaseMemberContact;

/**
 * 会员联系信息 Service实现
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Service("showcaseMemberContactService")
public class ShowcaseMemberContactServiceImpl extends EntityServiceImpl<ShowcaseMemberContact, ShowcaseMemberContactDao> implements ShowcaseMemberContactService {

}
