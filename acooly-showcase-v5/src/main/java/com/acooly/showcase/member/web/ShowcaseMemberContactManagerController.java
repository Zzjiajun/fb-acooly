/*
* acooly.cn Inc.
* Copyright (c) 2021 All Rights Reserved.
* create by acooly
* date:2021-10-26
*/
package com.acooly.showcase.member.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.acooly.module.tomcat.TomcatProperties;
import com.acooly.showcase.base.AbstractShowcaseController;
import org.apache.catalina.security.SecurityConfig;
import org.apache.catalina.security.SecurityListener;
import org.apache.jasper.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.SecurityNamespaceHandler;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.member.entity.ShowcaseMemberContact;
import com.acooly.showcase.member.service.ShowcaseMemberContactService;

import com.google.common.collect.Maps;

/**
 * 会员联系信息 管理控制器
 *
 * @author acooly
 * @date 2021-10-26 22:04:38
 */
@Controller
@RequestMapping(value = "/manage/showcase/member/showcaseMemberContact")
public class ShowcaseMemberContactManagerController extends AbstractShowcaseController<ShowcaseMemberContact, ShowcaseMemberContactService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShowcaseMemberContactService showcaseMemberContactService;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Object principal = SecurityUtils.getSubject().getPrincipal();
		model.put("testData",principal);
		super.referenceData(request, model);
	}
}
