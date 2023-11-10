/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 */
package com.acooly.showcase.demo.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.member.entity.ShowcaseMemberExtend;
import com.acooly.showcase.member.service.ShowcaseMemberExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ac_showcase_member_extend 管理控制器
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Controller
@RequestMapping(value = "/manage/showcase/demo/dynamicTab")
public class ShowcaseDemoDynamicTabManagerController extends AbstractShowcaseController {


    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/manage/showcase/demo/dynamicTab/index";
    }

    @RequestMapping("view1")
    public String view1(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/manage/showcase/demo/dynamicTab/view1";
    }

    @RequestMapping("view1Save")
    @ResponseBody
    public JsonResult view1Save(HttpServletRequest request, HttpServletResponse response, Model model) {
        return new JsonResult();
    }

    @RequestMapping("view2")
    public String view2(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/manage/showcase/demo/dynamicTab/view2";
    }

    @RequestMapping("view2Save")
    @ResponseBody
    public JsonResult view2Save(HttpServletRequest request, HttpServletResponse response, Model model) {
        return new JsonResult();
    }

}
