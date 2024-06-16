/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-08
*/
package com.acooly.showcase.link.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.showcase.link.entity.DmClick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmAccess;
import com.acooly.showcase.link.service.DmAccessService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_access 管理控制器
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Controller
@RequestMapping(value = "/manage/link/dmAccess")
public class DmAccessManagerController extends AbstractJsonEntityController<DmAccess, DmAccessService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmAccessService dmAccessService;


	@RequestMapping(value = "buildAccessUrl",method = RequestMethod.GET)
	public String buildAccessUrl(HttpServletRequest request, HttpServletResponse response , Model model) {
		String centerId = request.getParameter("centerId");
		model.addAttribute("k",centerId);
		return "manage/link/accessUrl";
	}

	@RequestMapping(value = "buildAccess",method = RequestMethod.GET)
	public String buildAccess(HttpServletRequest request, HttpServletResponse response , Model model) {
		String centerId = request.getParameter("centerId");
		model.addAttribute("k",centerId);
		return "manage/link/dmTabl/index";
	}




	@RequestMapping({"listAccessUrl"})
	@ResponseBody
	public JsonListResult<DmAccess> listAccessUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam String centerId) {
		JsonListResult<DmAccess> result = new JsonListResult();
		Map<String, Object> searchParams = this.getSearchParams(request);
		searchParams.put("EQ_centerId", centerId);
		PageInfo<DmAccess> pageInfo = this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
		result.setTotal(pageInfo.getTotalCount());
		result.setRows(pageInfo.getPageResults());
		result.setHasNext(pageInfo.hasNext());
		result.setPageNo(pageInfo.getCurrentPage());
		result.setPageSize(pageInfo.getCountOfCurrentPage());
		return result;
	}




}
