/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-04-26
*/
package com.acooly.showcase.link.web;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.link.entity.DmStencil;
import com.acooly.showcase.link.service.DmStencilService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmServer;
import com.acooly.showcase.link.service.DmServerService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_server 管理控制器
 *
 * @author acooly
 * @date 2024-04-26 04:43:12
 */
@Controller
@RequestMapping(value = "/manage/link/dmServer")
public class DmServerManagerController extends AbstractJsonEntityController<DmServer, DmServerService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmServerService dmServerService;
	@Autowired
	private RedisUtils redisUtil;
	@Autowired
	private DmStencilService dmStencilService;



	@RequestMapping(value = "eliminate")
	@ResponseBody
	public JsonResult eliminate(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			String type = request.getParameter("type");
			if ("redisServer".equals(type)){
				String builtKeyIp = redisUtil.buildKey("acooly","Ip");
				DmServer dmServer = dmServerService.get(1l);
				LinkedList<DmServer> dmServers = new LinkedList<>();
				dmServers.add(dmServer);
				redisUtil.set(builtKeyIp,new Gson().toJson(dmServers));
			}else {
				String builtKey = redisUtil.buildKey("acooly","pathNameCil");
				String builtKey1 = redisUtil.buildKey("acooly","pathNameCil1");
				DmStencil dmStencil = dmStencilService.get(1l);
				DmStencil dmStencil2 = dmStencilService.get(2l);
				redisUtil.set(builtKey, dmStencil.getPathName());
				redisUtil.set(builtKey1, dmStencil2.getPathName());
			}
		} catch (BusinessException e) {
			jsonResult.setMessage("更新失败");
			jsonResult.setSuccess(false);
		}
		jsonResult.setMessage("更新缓存成功");
		return jsonResult;
	}




}
