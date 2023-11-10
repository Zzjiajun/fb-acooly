/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-19
 *
 */
package com.acooly.showcase.member.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.member.entity.ShowcaseMember;

/**
 * 会员信息 Service接口
 *
 * @author acooly
 * @date 2021-10-19 14:23:36
 */
public interface ShowcaseMemberService extends EntityService<ShowcaseMember> {

    /**
     * 检测用户名是否重复
     *
     * @param username
     */
    void checkUniqueUsername(String username);

    /**
     * 根据唯一表面查询会员对象
     *
     * @param memberNo
     * @return
     */
    ShowcaseMember getMember(String memberNo);

}
