/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-10-26
*/
package com.acooly.showcase.member.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.acooly.showcase.base.AbstractShowcaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.member.entity.ShowcaseMemberExtend;
import com.acooly.showcase.member.service.ShowcaseMemberExtendService;

import com.google.common.collect.Maps;

/**
 * ac_showcase_member_extend 管理控制器
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Controller
@RequestMapping(value = "/manage/showcase/member/showcaseMemberExtend")
public class ShowcaseMemberExtendManagerController extends AbstractShowcaseController<ShowcaseMemberExtend, ShowcaseMemberExtendService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShowcaseMemberExtendService showcaseMemberExtendService;




}
