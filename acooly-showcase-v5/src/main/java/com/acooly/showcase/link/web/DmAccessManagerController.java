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
import com.acooly.core.utils.Encodes;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.link.entity.DmClick;
import com.acooly.showcase.link.entity.DmTrolls;
import com.google.common.collect.Lists;
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
	@Autowired
	private DmCenterService dmCenterService;


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


	@Override
	protected List<String> getExportTitles() {
		return Lists.newArrayList( "IP地址", "访问国家", "访问路径", "机型", "来源",
				"访问设备", "访客类型", "是否通过");
	}

	@Override
	protected List<Object> doExportRow(DmAccess entity) {
		String region ="";
		if("0".equals(entity.getVisitorType())){
			region="新点击访客";
		}else if("1".equals(entity.getVisitorType())){
			region="旧点击访客";
		}else {
			region="其他";
		}
		String deviceType ="";
		if("0".equals(entity.getAccessDevice())){
			deviceType="手机";
		}else if("1".equals(entity.getAccessDevice())){
			deviceType="PC";
		}else {
			deviceType="其他";
		}
		String isPass ="";
		if("0".equals(entity.getPassed())){
			isPass="通过";
		}else if("1".equals(entity.getPassed())){
			isPass="未通过";
		}else {
			isPass = "其他";
		}
		return Lists.newArrayList(entity.getIp(),entity.getRegion(),entity.getAccessPath(),
				entity.getModels(),entity.getSource(),deviceType,region,isPass);
	}

	@Override
	protected void doExportExcelHeader(HttpServletRequest request, HttpServletResponse response) {
		String fileName = this.getExportFileName(request);
		String centerId = request.getParameter("search_EQ_centerId");
		DmCenter dmCenter = dmCenterService.get(Long.valueOf(centerId));
		String domainName = dmCenter.getDomain()+"/"+dmCenter.getSecondaryDomain()+"的域名访问统计Excel数据";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Content-Disposition", "filename=\"" + Encodes.urlEncode(domainName) + ".xlsx\"");
	}
}
