/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-08
*/
package com.acooly.showcase.link.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.showcase.daliy.entity.DmCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmClick;
import com.acooly.showcase.link.service.DmClickService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_click 管理控制器
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Controller
@RequestMapping(value = "/manage/link/dmClick")
public class DmClickManagerController extends AbstractJsonEntityController<DmClick, DmClickService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmClickService dmClickService;




	@RequestMapping(value = "buildClickUrl",method = RequestMethod.GET)
	public String buildCanonicalUrl(HttpServletRequest request, HttpServletResponse response , Model model) {
		String centerId = request.getParameter("centerId");
		model.addAttribute("k",centerId);
		return "manage/link/clickUrl";
	}

	@RequestMapping({"listClickUrl"})
	@ResponseBody
	public JsonListResult<DmClick> listClickUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam String centerId) {
		JsonListResult<DmClick> result = new JsonListResult();
		Map<String, Object> searchParams = this.getSearchParams(request);
		searchParams.put("EQ_centerId", centerId);
		PageInfo<DmClick> pageInfo = this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
		result.setTotal(pageInfo.getTotalCount());
		result.setRows(pageInfo.getPageResults());
		result.setHasNext(pageInfo.hasNext());
		result.setPageNo(pageInfo.getCurrentPage());
		result.setPageSize(pageInfo.getCountOfCurrentPage());
		return result;
	}





}
