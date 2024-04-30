/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.member.web;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Profiles;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.member.entity.ShowcaseMember;
import com.acooly.showcase.member.service.ShowcaseMemberService;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.member.entity.ShowcaseMemberFile;
import com.acooly.showcase.member.service.ShowcaseMemberFileService;
import com.acooly.showcase.member.common.enums.FileTypeEnum;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员附件 管理控制器
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
@Controller
@RequestMapping(value = "/manage/showcase/member/showcaseMemberFile")
public class ShowcaseMemberFileManagerController extends AbstractShowcaseController<ShowcaseMemberFile, ShowcaseMemberFileService> {


    {
        allowMapping = "*";
    }

    private static final int MAX_FILE_COUNT = 50;

    @SuppressWarnings("unused")
    @Autowired
    private ShowcaseMemberFileService showcaseMemberFileService;

    @Autowired
    private ShowcaseMemberService showcaseMemberService;

    @RequestMapping(value = "uploaderView", method = RequestMethod.GET)
    public String uploaderView(Model model, HttpServletRequest request, HttpServletResponse response) {
        loadMember(model);
        return "/manage/showcase/member/showcaseMemberFileUploader";
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    public JsonResult upload(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult();
        try {
            ShowcaseMember showcaseMember = loadAndCheckMember();
            getUploadConfig().setStorageNameSpace("showcaseMember");
            Map<String, UploadResult> uploadResultMap = doUpload(request);

            List<ShowcaseMemberFile> entities = Lists.newArrayList();
            for (UploadResult uploadResult : uploadResultMap.values()) {
                ShowcaseMemberFile memberFile = new ShowcaseMemberFile();
                memberFile.setObjectId(Ids.did());
                memberFile.setMemberNo(showcaseMember.getMemberNo());
                memberFile.setUsername(showcaseMember.getUsername());
                memberFile.setFileName(uploadResult.getName());
                memberFile.setFileTitle(uploadResult.getName());
                String ext = Files.getFileExtension(uploadResult.getName());
                memberFile.setFileExt(ext);
                memberFile.setFileType(FileTypeEnum.of(ext));
                memberFile.setFilePath(uploadResult.getRelativeFile());
                entities.add(memberFile);
            }
            getEntityService().saves(entities);
            result.setMessage("上传成功");
        } catch (Exception e) {
            handleException(result, "上传失败", e);
        }
        return result;
    }


    /**
     * 简单处理删除数据时候同步删除文件
     * 不严谨：没有在事务中完成，严谨做法应该移入服务方法中
     */
    @Override
    protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
        ShowcaseMemberFile memberFile = getEntityService().get(ids[0]);
//        if (memberFile != null && Strings.isNotBlank(memberFile.getFilePath())) {
//            File file = new File(oFileProperties.getStorageRoot() + memberFile.getFilePath());
//            FileUtils.deleteQuietly(file);
//        }
    }


    @Override
    protected ShowcaseMemberFile onSave(HttpServletRequest request, HttpServletResponse response, Model model, ShowcaseMemberFile entity, boolean isCreate) throws Exception {
        // 因是对外的演示环境，判断如果online时，总数据不能超过50条（50*1M=50M文件）
        if (Profiles.isOnline()) {
            int count = showcaseMemberFileService.count();
            if (count > MAX_FILE_COUNT) {
                throw new BusinessException("MAX_LIMIT_COUNT", "超过最大记录允许数(" + MAX_FILE_COUNT + "),请删除现有演示数据记录，重新添加", "最大允许记录数:" + MAX_FILE_COUNT);
            }
        }
        // 根据文件上传配置，上传文件，并根据文件表单名与属性名匹配约定，自动设置上传文件相对路径到实体对应属性中。
        doUpload(request, entity);
        return super.onSave(request, response, model, entity, isCreate);
    }

    @Override
    protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        loadMember(model);
        super.onCreate(request, response, model);
    }

    @Override
    protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ShowcaseMemberFile entity) {
        loadMember(model);
        super.onEdit(request, response, model, entity);
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allFileTypes", FileTypeEnum.mapping());
//        model.put("serverRoot", oFileProperties.getServerRoot());
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
