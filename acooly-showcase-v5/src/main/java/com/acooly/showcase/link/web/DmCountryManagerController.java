/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-06-06
*/
package com.acooly.showcase.link.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.daliy.Utils.HttpUtil;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.link.entity.DmServer;
import com.acooly.showcase.link.entity.DmStencil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmCountry;
import com.acooly.showcase.link.service.DmCountryService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * dm_country 管理控制器
 *
 * @author acooly
 * @date 2024-06-06 04:06:20
 */
@Controller
@RequestMapping(value = "/manage/link/dmCountry")
public class DmCountryManagerController extends AbstractJsonEntityController<DmCountry, DmCountryService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmCountryService dmCountryService;
	@Autowired
	private RedisUtils redisUtil;



	@RequestMapping(value = "countryRedis")
	@ResponseBody
	public JsonResult countryRedis(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonResult jsonResult = null;
		try {
			jsonResult = new JsonResult();
			URL url = new URL("https://xword.cyou/hotel/countryRedis");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String ipAddress = reader.readLine();
			JSONObject jsonObject = new JSONObject(ipAddress);
			Map<String, Object> map = jsonObject.toMap();
			jsonResult.setMessage((String) map.get("message"));
			jsonResult.setSuccess((Boolean) map.get("success"));
		} catch (IOException e) {
			jsonResult.setMessage("更新失败");
			jsonResult.setSuccess(false);
		}


//		try {
//			String builtKey = redisUtil.buildKey("acooly","countryVpn");
//			List<DmCountry> countryList = this.getEntityService().getAll();
//			List<String> stringList = countryList.stream()
//					.map(DmCountry::getCountry)
//					.collect(Collectors.toList());
//			redisUtil.set(builtKey,new Gson().toJson(stringList));
//		} catch (BusinessException e) {
//			jsonResult.setMessage("更新失败");
//			jsonResult.setSuccess(false);
//		}
//        jsonResult.setMessage("更新缓存成功");
		return jsonResult;
	}





}
