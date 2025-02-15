/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.web;

import com.acooly.module.security.domain.User;
import com.acooly.showcase.stemp.entity.EmFileds;
import com.acooly.showcase.stemp.service.EmFiledsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.stemp.entity.EmStamp;
import com.acooly.showcase.stemp.service.EmStampService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * em_stamp 管理控制器
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Controller
@RequestMapping(value = "/manage/stemp/emStamp")
public class EmStampManagerController extends AbstractJsonEntityController<EmStamp, EmStampService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private EmStampService emStampService;
	@Autowired
	private EmFiledsService emFiledsService;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<EmFileds> filedsList = emFiledsService.getAll();

		Map<String, String> nameRemarkMap = filedsList.stream().collect(Collectors.toMap(EmFileds::getName, EmFileds::getRemark));
		model.put("nameMap", nameRemarkMap);
	}


//	@RequestMapping({"emStampFile"})
//	public String create(HttpServletRequest request, HttpServletResponse response, Model model) {
//		return StringUtils.isNotBlank(this.editView) ? this.editView : this.getRequestMapperValue() + "File";
//	}
}
