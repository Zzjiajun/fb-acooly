/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-02
 */
package com.acooly.showcase.link.web;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.entity.DmRegion;
import com.acooly.showcase.daliy.entity.Regname;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.service.DmRegionService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RegnameService;
import com.acooly.showcase.link.entity.Board;
import com.acooly.showcase.link.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.DmCondition;

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

    private static final Logger logger = LoggerFactory.getLogger(AbstractStandardEntityController.class);

    @SuppressWarnings("unused")
    @Autowired
    private DmConditionService dmConditionService;
    @Autowired
    private DmCenterService dmCenterService;
	@Autowired
	private UserService userService;
    @Autowired
    private DmCountryService dmCountryService;
    @Autowired
    private PermissionsService permissionsService;
    @Autowired
    private DmRegionService dmRegionService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private DmObserverPermissionService dmObserverPermissionService;
    @Autowired
    private DmModlesService dmModlesService;


    @Override
    protected PageInfo<DmCondition> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Map<String, Object> searchParams = this.getSearchParams(request);
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> mapQuery = Maps.newHashMap();
        mapQuery.put("EQ_userName", principal.getUsername());
        if (!(permissionsService.query(mapQuery, null).size() > 0)) {
            searchParams.put("EQ_userName",principal.getUsername());
        }
        Map<String, Object> map1Query = Maps.newHashMap();
        map1Query.put("EQ_manageName", principal.getUsername());
        List<Board> boardList = boardService.query(map1Query, null);
        if (boardList.size() > 0){
            String attachedName = boardList.get(0).getAttachedName();
            List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();
            // 删除键为 "EQ_userName" 的条目
            searchParams.remove("EQ_userName");
            searchParams.put("IN_userName", gatherList);
        }

        return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
    }


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        boolean observer = dmObserverPermissionService.isObserver(principal.getId());
        model.put("observer", observer);
        Map<String, Integer> statusMap = Maps.newLinkedHashMap();
        statusMap.put("关闭", 0);
        statusMap.put("开启", 1);
        model.put("statusMap", statusMap);
        Map<String, String> conMap = Maps.newLinkedHashMap();
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
        model.put("ipMap", ipMap);
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query3 = userService.query(mapQuery, null);
		Map<String, String> mapName = query3.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("map1Name", mapName);

        // 添加iOS系统版本号映射
        Map<String, String> iosVersionMap = Maps.newLinkedHashMap();
        iosVersionMap.put("iOS 17及以下", "17.0.0");
        iosVersionMap.put("iOS16.9及以下", "16.9.0");
        iosVersionMap.put("iOS16.8及以下", "16.8.0");
        iosVersionMap.put("iOS16.7及以下", "16.7.0");
        iosVersionMap.put("iOS16.6及以下", "16.6.0");
        iosVersionMap.put("iOS16.5及以下", "16.5.0");
        iosVersionMap.put("iOS16.4及以下", "16.4.0");
        iosVersionMap.put("iOS16.3及以下", "16.3.0");
        iosVersionMap.put("iOS16.2及以下", "16.2.0");
        iosVersionMap.put("iOS16.1及以下", "16.1.0");
        iosVersionMap.put("iOS 16及以下", "16.0.0");
        iosVersionMap.put("iOS 15及以下", "15.0.0");
        iosVersionMap.put("iOS 14及以下", "14.0.0");
        iosVersionMap.put("iOS 13及以下", "13.0.0");
        iosVersionMap.put("iOS 12及以下", "12.0.0");
        iosVersionMap.put("iOS 11及以下", "11.0.0");
        iosVersionMap.put("iOS 10及以下", "10.0.0");
        iosVersionMap.put("iOS 9及以下", "9.0.0");
        iosVersionMap.put("iOS 8及以下", "8.0.0");
        iosVersionMap.put("无限制", "0.0.0");
        model.put("iosVersionMap", iosVersionMap);

        // 添加Android系统版本号映射
        Map<String, String> androidVersionMap = Maps.newLinkedHashMap();
        androidVersionMap.put("And 14及以下", "14.0.0");
        androidVersionMap.put("And 13及以下", "13.0.0");
        androidVersionMap.put("And 12及以下", "12.0.0");
        androidVersionMap.put("And 11及以下", "11.0.0");
        androidVersionMap.put("And 10及以下", "10.0.0");
        androidVersionMap.put("And 9及以下", "9.0.0");
        androidVersionMap.put("And 8及以下", "8.0.0");
        androidVersionMap.put("And 7及以下", "7.0.0");
        androidVersionMap.put("And 6及以下", "6.0.0");
        androidVersionMap.put("And 5及以下", "5.0.0");
        androidVersionMap.put("无限制", "0.0.0");
        model.put("androidVersionMap", androidVersionMap);

        // 添加语言映射
        Map<String, String> languageMap = Maps.newLinkedHashMap();
        languageMap.put("中文", "zh");
        languageMap.put("简体中文(大陆)", "zh-CN");
        languageMap.put("简体中文(新加坡)", "zh-SG");
        languageMap.put("繁體中文(台灣)", "zh-TW");
        languageMap.put("繁體中文(香港)", "zh-HK");
        languageMap.put("繁體中文(澳門)", "zh-MO");
        languageMap.put("英文", "en");
        languageMap.put("日文", "ja");          // 修改为标准 jp -> ja
        languageMap.put("韩文", "ko");          // 修改为标准 kr -> ko
        languageMap.put("墨西哥(西班牙语)", "es-MX");
        languageMap.put("西班牙语", "es");
        languageMap.put("法语", "fr");
        languageMap.put("德语", "de");
        languageMap.put("葡萄牙语", "pt");
        languageMap.put("意大利语", "it");
        languageMap.put("俄语", "ru");
        languageMap.put("阿拉伯语", "ar");
        languageMap.put("印地语", "hi");
        languageMap.put("孟加拉语", "bn");
        languageMap.put("乌尔都语", "ur");
        languageMap.put("泰语", "th");
        languageMap.put("越南语", "vi");
        languageMap.put("土耳其语", "tr");
        languageMap.put("荷兰语", "nl");
        languageMap.put("瑞典语", "sv");
        languageMap.put("挪威语", "no");
        languageMap.put("丹麦语", "da");
        languageMap.put("芬兰语", "fi");
        languageMap.put("希腊语", "el");
        languageMap.put("波兰语", "pl");
        languageMap.put("匈牙利语", "hu");
        languageMap.put("捷克语", "cs");
        languageMap.put("斯洛伐克语", "sk");
        languageMap.put("希伯来语", "he");
        languageMap.put("印尼语", "id");
        languageMap.put("马来语", "ms");
        languageMap.put("菲律宾语", "tl");
        languageMap.put("韩语", "ko"); // 保留
        languageMap.put("日语", "ja"); // 保留
        model.put("languageMap", languageMap);

        //手机型号列表 待开发

    }

    @Override
    protected DmCondition onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmCondition entity, boolean isCreate) throws Exception {
        dmCountryService.dmWarmupRedis();
        dmCountryService.dmConditionRedis();
        if (!isCreate){
            User principal = (User) SecurityUtils.getSubject().getPrincipal();
            entity.setUpdateBy(principal.getUsername());
        }
//        if (!isCreate) {
//            Map<String, String> map = dmRegionService.getAll().stream().
//                    collect(Collectors.toMap(DmRegion::getCode, DmRegion::getTimeZone));
//            entity.setTimeContinent(map.get(entity.getIpCountry()));
//        }
//        User principal = (User) SecurityUtils.getSubject().getPrincipal();
//        // 检查当前用户是否有权限查看此记录
//        if (dmObserverPermissionService.isObserver(principal.getId())) {
//            request.setAttribute("message", "您没有权限操作");
//            throw new AccessDeniedException("您没有权限操作");
//        }
        return super.onSave(request, response, model, entity, isCreate);
    }

    @Override
    public JsonEntityResult<DmCondition> updateJson(HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<DmCondition> result = new JsonEntityResult();
        this.allow(request, response, MappingMethod.create);
        try {
            User principal = (User) SecurityUtils.getSubject().getPrincipal();
            // 检查当前用户是否有权限查看此记录
            if (dmObserverPermissionService.isObserver(principal.getId())) {
                result.setSuccess(false);
                result.setMessage("您没有权限操作");
            }else {
                result.setEntity(this.doSave(request, response, (Model)null, false));
                result.setMessage("更新成功");
            }
        } catch (Exception var5) {
            this.handleException(result, "更新", var5);
        }
        return result;
    }

    @RequestMapping({"editCenter"})
    public String editTwo(HttpServletRequest request, HttpServletResponse response, Model model) throws AccessDeniedException {
        this.allow(request, response, MappingMethod.update);
        try {
            model.addAllAttributes(this.referenceData(request));
            String id = request.getParameter("id");
            DmCenter dmCenter = dmCenterService.get(Long.valueOf(id));
            DmCondition entity = dmConditionService.get(dmCenter.getConditionId());
            model.addAttribute("action", "edit");
            model.addAttribute(this.getEntityName(), entity);
            this.onEdit(request, response, model, entity);
        } catch (Exception var5) {
            logger.warn(this.getExceptionMessage("edit", var5), var5);
            this.handleException("编辑", var5, request);
        }

        String editView1 = this.getEditView();
        return this.getEditView();
    }



//    @Override
//    public String save(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            dmCountryService.dmCenterRedis();
//            dmCountryService.dmConditionRedis();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return super.save(request, response, model, redirectAttributes);
//    }


//    @Override
//    protected DmCondition doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate) throws Exception {
//        try {
//            dmCountryService.dmCenterRedis();
//            dmCountryService.dmConditionRedis();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return super.doSave(request, response, model, isCreate);
//    }
}
