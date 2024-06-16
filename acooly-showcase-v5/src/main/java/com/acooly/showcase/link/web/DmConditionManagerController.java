/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-02
 */
package com.acooly.showcase.link.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.service.DmConditionService;

import com.google.common.collect.Maps;

/**
 * dm_condition 管理控制器
 *
 * @author acooly
 * @date 2024-06-02 19:10:44
 */
@Controller
@RequestMapping(value = "/manage/link/dmCondition")
public class DmConditionManagerController extends AbstractJsonEntityController<DmCondition, DmConditionService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private DmConditionService dmConditionService;
	@Autowired
	private UserService userService;


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        Map<String, Integer> statusMap = Maps.newHashMap();
        statusMap.put("关闭", 0);
        statusMap.put("开启", 1);
        model.put("statusMap", statusMap);
        Map<String, String> conMap = Maps.newHashMap();
        conMap.put("美洲", "America");
        conMap.put("非洲", "Africa");
        conMap.put("南极洲", "Antarctica");
        conMap.put("亚洲", "Asia");
        conMap.put("大西洋", "Atlantic");
        conMap.put("澳大利亚", "Australia");
        conMap.put("欧洲", "Europe");
        conMap.put("印度洋", "Indian");
        conMap.put("太平洋", "Pacific");
        model.put("conMap", conMap);
        Map<String, String> ipMap = Maps.newHashMap();
        ipMap.put("美国", "US");
        ipMap.put("加拿大","CA");
        ipMap.put("印度", "IN");
        ipMap.put("日本", "JP");
        ipMap.put("巴西", "BR");
        ipMap.put("澳大利亚","AU");
		ipMap.put("英国", "GB");
		ipMap.put("韩国","KR");
		ipMap.put("德国","DE");
        model.put("ipMap", ipMap);
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query3 = userService.query(mapQuery, null);
		Map<String, String> mapName = query3.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("map1Name", mapName);
    }


}
