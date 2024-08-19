/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-02
 */
package com.acooly.showcase.link.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.entity.DmRegion;
import com.acooly.showcase.daliy.entity.Regname;
import com.acooly.showcase.daliy.service.DmRegionService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RegnameService;
import com.acooly.showcase.link.service.DmCountryService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.service.DmConditionService;

import com.google.common.collect.Maps;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private DmCountryService dmCountryService;
    @Autowired
    private PermissionsService permissionsService;
    @Autowired
    private DmRegionService dmRegionService;


    @Override
    protected PageInfo<DmCondition> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
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
        // 使用 Stream API 简化代码
        Map<String, String> ipMap = dmRegionService.getAll().stream()
                .collect(Collectors.toMap(DmRegion::getRegion, DmRegion::getCode));
//
//
//        ipMap.put("美国", "US");
//        ipMap.put("泰国", "TH");
//        ipMap.put("台湾", "TW");
//        ipMap.put("加拿大","CA");
//        ipMap.put("印度", "IN");
//        ipMap.put("日本", "JP");
//        ipMap.put("巴西", "BR");
//        ipMap.put("澳大利亚","AU");
//		ipMap.put("英国", "GB");
//		ipMap.put("韩国","KR");
//		ipMap.put("德国","DE");
        model.put("ipMap", ipMap);
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query3 = userService.query(mapQuery, null);
		Map<String, String> mapName = query3.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("map1Name", mapName);
    }

    @Override
    protected DmCondition onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmCondition entity, boolean isCreate) throws Exception {
        dmCountryService.dmConditionRedis();
        return super.onSave(request, response, model, entity, isCreate);
    }



    @Override
    public String save(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        try {
            dmCountryService.dmConditionRedis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.save(request, response, model, redirectAttributes);
    }
}
