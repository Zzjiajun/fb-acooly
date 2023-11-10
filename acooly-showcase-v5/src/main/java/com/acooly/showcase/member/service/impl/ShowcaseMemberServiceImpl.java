/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-19
 */
package com.acooly.showcase.member.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.showcase.common.ShowcaseResultCode;
import com.acooly.showcase.member.common.enums.IdcardTypeEnum;
import com.acooly.showcase.member.entity.ShowcaseMemberExtend;
import com.acooly.showcase.member.service.ShowcaseMemberExtendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.member.service.ShowcaseMemberService;
import com.acooly.showcase.member.dao.ShowcaseMemberDao;
import com.acooly.showcase.member.entity.ShowcaseMember;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 会员信息 Service实现
 *
 * @author acooly
 * @date 2021-10-19 14:23:36
 */
@Slf4j
@Service("showcaseMemberService")
public class ShowcaseMemberServiceImpl extends EntityServiceImpl<ShowcaseMember, ShowcaseMemberDao> implements ShowcaseMemberService {

    @Autowired
    private ShowcaseMemberExtendService showcaseMemberExtendService;

    @Override
    public void checkUniqueUsername(String username) {
        ShowcaseMember customer = getEntityDao().findByUsername(username);
        if (customer != null) {
            log.warn("用户名：{}，已经存在}", username);
            throw new BusinessException(ShowcaseResultCode.USERNAME_DOES_EXIST, username);
        }
    }

    @Override
    public ShowcaseMember getMember(String memberNo) {
        ShowcaseMember showcaseMember = getEntityDao().findByMemberNo(memberNo);
        loadExtendInfo(showcaseMember);
        return showcaseMember;
    }

    @Override
    public ShowcaseMember get(Serializable id) throws BusinessException {
        ShowcaseMember showcaseMember = super.get(id);
        loadExtendInfo(showcaseMember);
        return showcaseMember;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(ShowcaseMember o) throws BusinessException {
        if (Strings.isBlank(o.getMemberNo())) {
            o.setMemberNo(Ids.did());
        }
        if (o.getIdcardType() == null) {
            o.setIdcardType(IdcardTypeEnum.cert);
        }
        super.save(o);
        ShowcaseMemberExtend showcaseMemberExtend = showcaseMemberExtendService.get(o.getId());
        if (showcaseMemberExtend == null) {
            showcaseMemberExtend = new ShowcaseMemberExtend(o.getId(), o.getContext());
        }
        showcaseMemberExtendService.save(showcaseMemberExtend);
    }

    @Override
    public void removeById(Serializable id) throws BusinessException {
        super.removeById(id);
    }

    @Override
    public void update(ShowcaseMember o) throws BusinessException {
        super.update(o);
        ShowcaseMemberExtend showcaseMemberExtend = showcaseMemberExtendService.get(o.getId());
        if (showcaseMemberExtend == null) {
            showcaseMemberExtend = new ShowcaseMemberExtend(o.getId(), o.getContext());
        } else {
            showcaseMemberExtend.setContext(o.getContext());
        }
        showcaseMemberExtendService.saveOrUpdate(showcaseMemberExtend);
    }

    protected void loadExtendInfo(ShowcaseMember showcaseMember) {
        if (showcaseMember != null) {
            ShowcaseMemberExtend showcaseMemberExtend = showcaseMemberExtendService.get(showcaseMember.getId());
            if (showcaseMemberExtend != null) {
                showcaseMember.setContext(showcaseMemberExtend.getContext());
            }
        }
    }
}
