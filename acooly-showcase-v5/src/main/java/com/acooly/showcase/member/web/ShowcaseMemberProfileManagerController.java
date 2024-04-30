/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-27
 */
package com.acooly.showcase.member.web;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.Profiles;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.member.common.enums.EmailStatusEnum;
import com.acooly.showcase.member.common.enums.MobileNoStatusEnum;
import com.acooly.showcase.member.common.enums.RealNameStatusEnum;
import com.acooly.showcase.member.common.enums.SmsSendStatusEnum;
import com.acooly.showcase.member.entity.ShowcaseMember;
import com.acooly.showcase.member.entity.ShowcaseMemberProfile;
import com.acooly.showcase.member.service.ShowcaseMemberProfileService;
import com.acooly.showcase.member.service.ShowcaseMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 会员配置信息 管理控制器
 *
 * @author acooly
 * @date 2021-10-27 13:24:15
 */
@Controller
@RequestMapping(value = "/manage/showcase/member/showcaseMemberProfile")
public class ShowcaseMemberProfileManagerController extends AbstractShowcaseController<ShowcaseMemberProfile, ShowcaseMemberProfileService> {


    {
        allowMapping = "*";
    }

    private static final int MAX_FILE_COUNT = 20;

    @Autowired
    private ShowcaseMemberProfileService showcaseMemberProfileService;
    @Autowired
    private ShowcaseMemberService showcaseMemberService;

    @Autowired
    private UserService userService;

    @Override
    protected ShowcaseMemberProfile onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShowcaseMemberProfile entity, boolean isCreate) throws Exception {


        if (isCreate) {

            // 因是对外的演示环境，判断如果online时，总数据不能超过50条（50*1M=50M文件）
            if (Profiles.isOnline()) {
                int count = showcaseMemberProfileService.count();
                if (count > MAX_FILE_COUNT) {
                    throw new BusinessException("MAX_LIMIT_COUNT", "超过最大记录允许数(" + MAX_FILE_COUNT + "),请删除现有演示数据记录，重新添加", "最大允许记录数:" + MAX_FILE_COUNT);
                }
            }

            ShowcaseMember showcaseMember = loadAndCheckMember();
            entity.setMemberNo(showcaseMember.getMemberNo());
            entity.setUsername(showcaseMember.getUsername());
        }
        doUpload(request, entity);
        return super.onSave(request, response, model, entity, isCreate);
    }


    @Override
    protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        super.onCreate(request, response, model);
        // 所有客户经理
        loadUsers(model);
        // 所有会员
        loadMembers(model);
        // 加载传入memberNo的会员
        loadMember(model);
    }

    @Override
    protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ShowcaseMemberProfile entity) {
        super.onEdit(request, response, model, entity);
        // 所有客户经理
        loadUsers(model);
        // 所有会员
        loadMembers(model);
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allEmailStatuss", EmailStatusEnum.mapping());
        model.put("allMobileNoStatuss", MobileNoStatusEnum.mapping());
        model.put("allRealNameStatuss", RealNameStatusEnum.mapping());
        model.put("allSmsSendStatuss", SmsSendStatusEnum.mapping());
//        model.put("serverRoot", oFileProperties.getServerRoot());
    }

    private void loadUsers(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
    }

    private void loadMembers(Model model) {
        model.addAttribute("members", showcaseMemberService.getAll());
    }

    private void loadMember(Model model) {
        String memberNo = Servlets.getParameter("memberNo");
        if (Strings.isNotBlank(memberNo)) {
            ShowcaseMember showcaseMember = showcaseMemberService.getMember(memberNo);
            if (showcaseMember != null) {
                model.addAttribute("member", showcaseMember);
            }
        }
    }

    protected ShowcaseMember loadAndCheckMember() {
        String memberNo = Servlets.getParameter("memberNo");
        if (Strings.isNotBlank(memberNo)) {
            ShowcaseMember showcaseMember = showcaseMemberService.getMember(memberNo);
            if (showcaseMember != null) {
                return showcaseMember;
            }
        }
        throw new BusinessException(CommonErrorCodes.OBJECT_NOT_EXIST, "会员不存在");
    }

}
