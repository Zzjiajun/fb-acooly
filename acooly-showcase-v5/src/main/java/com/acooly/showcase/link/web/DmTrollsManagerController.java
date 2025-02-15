/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by jj
* date:2024-09-25
*/
package com.acooly.showcase.link.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Encodes;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.web.DmCenterManagerController;
import com.acooly.showcase.link.entity.DmAccess;
import com.acooly.showcase.member.entity.ShowcaseMember;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmTrolls;
import com.acooly.showcase.link.service.DmTrollsService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_trolls 管理控制器
 *
 * @author jj
 * @date 2024-09-25 04:30:32
 */
@Controller
@RequestMapping(value = "/manage/link/dmTrolls")
public class DmTrollsManagerController extends AbstractJsonEntityController<DmTrolls, DmTrollsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmTrollsService dmTrollsService;
	@Autowired
	private DmCenterService dmCenterService;


	@RequestMapping(value = "buildTrollsUrl",method = RequestMethod.GET)
	public String buildAccessUrl(HttpServletRequest request, HttpServletResponse response , Model model) {
		String centerId = request.getParameter("centerId");
		model.addAttribute("k",centerId);
		return "manage/link/trollsUrl";
	}

	@RequestMapping({"listTrollsUrl"})
	@ResponseBody
	public JsonListResult<DmTrolls> listAccessUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam String centerId) {
		JsonListResult<DmTrolls> result = new JsonListResult();
		Map<String, Object> searchParams = this.getSearchParams(request);
		searchParams.put("EQ_centerId", centerId);
		PageInfo<DmTrolls> pageInfo = this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
		result.setTotal(pageInfo.getTotalCount());
		result.setRows(pageInfo.getPageResults());
		result.setHasNext(pageInfo.hasNext());
		result.setPageNo(pageInfo.getCurrentPage());
		result.setPageSize(pageInfo.getCountOfCurrentPage());
		return result;
	}



	@RequestMapping({"showTrollsUrl"})
	public String show(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.allow(request, response, MappingMethod.show);

		try {
			model.addAllAttributes(this.referenceData(request));
			DmTrolls entity = this.loadEntity(request);
			if (entity == null) {
				throw new AppConfigException("LoadEntity failure.");
			}

			this.onShow(request, response, model, entity);
			model.addAttribute(this.getEntityName(), entity);
		} catch (Exception var5) {
			this.handleException("查看", var5, request);
		}

		return "manage/link/dmTrollsShow";
	}


	@Override
	protected List<String> getExportTitles() {
		return Lists.newArrayList( "IP地址", "访问国家", "访问路径", "机型", "来源",
				"访问设备", "访客类型", "失败详情");
	}

	@Override
	protected List<Object> doExportRow(DmTrolls entity) {
//		DmCenter dmCenter = dmCenterService.get(Long.valueOf(entity.getCenterId()));
		String region ="";
		if("0".equals(entity.getVisitorType())){
			region="新点击访客";
		}else if("1".equals(entity.getVisitorType())){
			region="旧点击访客";
		}else {
			region="其他";
		}
		String deviceType ="";
		if("0".equals(entity.getTrollsDevice())){
			deviceType="手机";
		}else if("1".equals(entity.getTrollsDevice())){
			deviceType="PC";
		}else {
			deviceType="其他";
		}
		return Lists.newArrayList(entity.getIp(),entity.getRegion(),entity.getTrollsPath(),
				entity.getModels(),entity.getSource(),deviceType,region,entity.getDetails());
	}

	@Override
	protected void doExportExcelHeader(HttpServletRequest request, HttpServletResponse response) {
		String fileName = this.getExportFileName(request);
		String centerId = request.getParameter("search_EQ_centerId");
		DmCenter dmCenter = dmCenterService.get(Long.valueOf(centerId));
		String domainName = dmCenter.getDomain()+"/"+dmCenter.getSecondaryDomain()+"的水军记录数据Excel";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Content-Disposition", "filename=\"" + Encodes.urlEncode(domainName) + ".xlsx\"");
	}
}
