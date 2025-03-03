/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-15
*/
package com.acooly.showcase.stemp.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.link.entity.Board;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.stemp.entity.EmFileds;
import com.acooly.showcase.stemp.entity.EmSumdata;
import com.acooly.showcase.stemp.service.EmFiledsService;
import com.acooly.showcase.stemp.service.EmSumdataService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.stemp.entity.EmStamp;
import com.acooly.showcase.stemp.service.EmStampService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
	@Autowired
	private EmSumdataService emSumdataService;
	@Autowired
	private PermissionsService permissionsService;



	@Override
	protected PageInfo<EmStamp> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_userName",principal.getUsername());
		}

		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<EmFileds> filedsList = emFiledsService.getAll();

		Map<String, String> nameRemarkMap = filedsList.stream().collect(Collectors.toMap(EmFileds::getName, EmFileds::getRemark));
		model.put("nameMap", nameRemarkMap);
	}

	@Override
	protected EmStamp onSave(HttpServletRequest request, HttpServletResponse response, Model model, EmStamp entity, boolean isCreate) throws Exception {
		if (isCreate){
			User principal = (User) SecurityUtils.getSubject().getPrincipal();
			entity.setUserName(principal.getUsername());
			// 检查 name 和 user_name 是否重复
			if (!checkTableNameAndCreator(entity)) {
				// 如果重复，抛出异常并返回错误信息给前端
				throw new IllegalArgumentException("该用户名下的类型表名称已存在，请选择其他名称。");
			}
		}
		return entity;
	}

	//检查表名和创建者组合是否重复
	private boolean checkTableNameAndCreator(EmStamp emStamp) {
		List<EmStamp> list = this.getEntityService().getAll();
		for (EmStamp stamp : list) {
			if (stamp.getName().equals(emStamp.getName()) && stamp.getUserName().equals(emStamp.getUserName())) {
				return false; // 找到重复，返回 false
			}
		}
		return true; // 没有找到重复，返回 true
	}


	@Override
	public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
		Serializable[] idList = this.getRequestIds(request);
		for (Serializable id : idList){
			//删除关联的sumdata
			Map<String, Object> map = Maps.newHashMap();
			map.put("EQ_stampId", id);
			List<EmSumdata> sumdataList = emSumdataService.query(map, null);
			sumdataList.forEach(sumdata -> {
				sumdata.setIsDelete(1);
				emSumdataService.update(sumdata);
			});
		}
		return super.deleteJson(request, response);
	}
}
