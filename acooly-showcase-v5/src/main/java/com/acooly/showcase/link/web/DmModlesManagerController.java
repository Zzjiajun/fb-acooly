/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-10-31
*/
package com.acooly.showcase.link.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.link.entity.DmServer;
import com.acooly.showcase.link.service.DmServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmModles;
import com.acooly.showcase.link.service.DmModlesService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_modles 管理控制器
 *
 * @author acooly
 * @date 2024-10-31 23:52:10
 */
@Controller
@RequestMapping(value = "/manage/link/dmModles")
public class DmModlesManagerController extends AbstractJsonEntityController<DmModles, DmModlesService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmModlesService dmModlesService;
	@Autowired
	private DmServerService dmServerService;


	@RequestMapping(value = "addModels")
	@ResponseBody
	public JsonResult addModels(HttpServletRequest request, HttpServletResponse response)throws IOException  {
		JsonResult result = new JsonResult();
		String id = request.getParameter("id");
		try {
			DmModles dmModles = this.getEntityService().get(Long.valueOf(id));
			//TODO 业务逻辑
			dmModles.setIsDelete(0);
			this.getEntityService().update(dmModles);
			//更新redis缓存
			DmServer dmServer = dmServerService.getAll().get(0);
			String urls= dmServer.getDomain()+"/fbVpnStock/dmModles";
			URL url = new URL(urls);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			result.setMessage("启用成功");
		} catch (BusinessException e) {
			result.setMessage("启用失败");
			result.setSuccess(false);
		} catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return result;
	}

	@RequestMapping(value = "deleteModels")
	@ResponseBody
	public JsonResult deleteModels(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JsonResult result = new JsonResult();
		String id = request.getParameter("id");
		try {
			DmModles dmModles = this.getEntityService().get(Long.valueOf(id));
			//TODO 业务逻辑
			dmModles.setIsDelete(1);
			this.getEntityService().update(dmModles);
			//更新redis缓存
			DmServer dmServer = dmServerService.getAll().get(0);
			String urls= dmServer.getDomain()+"/fbVpnStock/dmModles";
			URL url = new URL(urls);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			result.setMessage("禁用成功");
		} catch (BusinessException e) {
			result.setMessage("禁用失败");
			result.setSuccess(false);
		}
		return result;
	}






}
