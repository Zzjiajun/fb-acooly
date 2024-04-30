/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-19
 */
package com.acooly.showcase.member.web;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.IdCards;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.member.common.enums.CustomerTypeEnum;
import com.acooly.showcase.member.common.enums.IdcardTypeEnum;
import com.acooly.showcase.member.entity.ShowcaseMember;
import com.acooly.showcase.member.service.ShowcaseMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 会员信息 管理控制器
 *
 * @author acooly
 * @date 2021-10-19 14:23:36
 */
@Controller
@RequestMapping(value = "/manage/showcase/member/showcaseMember")
public class ShowcaseMemberManagerController extends AbstractShowcaseController<ShowcaseMember, ShowcaseMemberService> {

    private static Map<Integer, String> allNumStatuss = Maps.newLinkedHashMap();

    static {
        allNumStatuss.put(1, "A");
        allNumStatuss.put(2, "B");
        allNumStatuss.put(3, "C类型");
    }

    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ShowcaseMemberService showcaseMemberService;


    @Override
    public List<String> getExportTitles() {
        return Lists.newArrayList("ID", "编码", "用户名", "姓名", "身份证号码", "手机号码",
                "客户类型", "完成度", "薪水", "注册时间", "状态");
    }

    @Override
    protected List<Object> doExportRow(ShowcaseMember entity) {
        return Lists.newArrayList(entity.getId(), entity.getMemberNo(), entity.getUsername(), entity.getRealName(),
                entity.getIdcardNo(), entity.getMobileNo(),
                entity.getCustomerType(), entity.getDoneRatio(), entity.getSalary(), entity.getCreateTime(), entity.getStatus());
    }

    /**
     * 验证用户名是否重复
     *
     * @param request
     * @param response
     * @return JsonResult.success() == true: OK
     */
    @RequestMapping("checkUniqueUsername")
    @ResponseBody
    public JsonResult checkUniqueUsername(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            String username = request.getParameter("username");
            if (Strings.isBlank(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            getEntityService().checkUniqueUsername(username);
        } catch (Exception e) {
            handleException(result, "用户名验重", e);
        }
        return result;
    }

    /**
     * 动态生成ID
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("did")
    @ResponseBody
    public JsonResult idGenerate(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            result.appendData("id", Ids.did());
        } catch (Exception e) {
            handleException(result, "ID生成器", e);
        }
        return result;
    }


    /**
     * 解析身份证号码
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("parseIdCard")
    @ResponseBody
    public JsonEntityResult<IdCards.IdCardInfo> parseIdCard(
            HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<IdCards.IdCardInfo> result = new JsonEntityResult();
        try {
            String idcard = request.getParameter("idcard");
            if (Strings.isBlank(idcard)) {
                throw new IllegalArgumentException("身份证号码不能为空");
            }

            IdCards.IdCardInfo idCardInfo = IdCards.parse(idcard);
            result.setEntity(idCardInfo);
        } catch (Exception e) {
            handleException(result, "身份证号码解析", e);
        }
        return result;
    }


    @RequestMapping("kindEditor")
    @ResponseBody
    public Map<String, Object> kindEditor(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            Map<String, UploadResult> uploadResultMap = doUpload(request);
            UploadResult uploadResult = Collections3.getFirst(uploadResultMap.values());
            result.put("error", 0);
//            result.put("url", oFileProperties.getServerRoot() + uploadResult.getRelativeFile());
        } catch (Exception e) {
            result.put("error", 1);
            result.put("message", "文件上传失败:" + e.getMessage());
        }
        return result;
    }


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        model.put("testData",principal);
        model.put("allGenders", Gender.mapping());
        model.put("allAnimals", AnimalSign.mapping());
        model.put("allIdcardTypes", IdcardTypeEnum.mapping());
        model.put("allCustomerTypes", CustomerTypeEnum.mapping());
        model.put("allRegistryChannels", ChannelEnum.mapping());
        model.put("allPushAdvs", WhetherStatus.mapping());
        model.put("allNumStatuss", allNumStatuss);
        model.put("allStatuss", SimpleStatus.mapping());
    }


}
