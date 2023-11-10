/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by zhangpu@acooly.cn
 * date:2021-12-19
 */
package com.acooly.showcase.demo.usergroup.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.demo.usergroup.entity.UserGroup;
import com.acooly.showcase.demo.usergroup.service.UserGroupService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户动态分组 管理控制器
 *
 * @author zhangpu@acooly.cn
 * @date 2021-12-19 16:45:03
 */
@Controller
@RequestMapping(value = "/manage/demo/usergroup/userGroup")
public class UserGroupManagerController extends AbstractShowcaseController<UserGroup, UserGroupService> {


    {
        allowMapping = "*";
    }

    private DefaultConversionService conversionService = new DefaultConversionService();


    @SuppressWarnings("unused")
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "batchCreate", method = RequestMethod.GET)
    public String batchCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            model.addAttribute("users", userService.getAll());
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("批量创建", e, request);
        }
        return "/manage/demo/usergroup/userGroupBatch";
    }

    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult batchSave(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            Map<String, Boolean> sortMap = Maps.newHashMap();
            sortMap.put("id", true);
            List<UserGroup> userGroups = userGroupService.query(Maps.newHashMap(), sortMap);
            userGroups = mergeRequestEntities(request, userGroups);
            getEntityService().batchSave(userGroups);
            result.setMessage("批量保存成功");
        } catch (Exception e) {
            handleException(result, "批量保存", e);
        }
        return result;
    }

    /**
     * 合并请求的条件数据
     *
     * @param request 请求Request
     * @param ugs     已持久化的数据
     * @return
     */
    private List<UserGroup> mergeRequestEntities(HttpServletRequest request, List<UserGroup> ugs) {

        Map<String, String> params = getParameters(request);
        Map<Integer, Map<String, String>> entityMap = Maps.newHashMap();

        // 清洗数据：Map<index -> entityDataMap>
        for (Map.Entry<String, String> entry : params.entrySet()) {
            // 批量处理的所有表单名称都是：表单名称_index
            if (Strings.contains(entry.getKey(), "_")) {
                Pair<String, Integer> pair = parseParamName(entry.getKey());
                Integer index = pair.getRight();
                if (index == null) {
                    continue;
                }
                if (entityMap.get(index) == null) {
                    entityMap.put(index, new HashMap<String, String>());
                }
                entityMap.get(index).put(pair.getLeft(), entry.getValue());
            }
        }

        // 设置数据：无下标的参数，每个对象共用
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (Strings.contains(entry.getKey(), "_")) {
                continue;
            }
            for (Map<String, String> entityDataMap : entityMap.values()) {
                entityDataMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 通过bind转换 entityDataMap为Entity对象
        Map<Long, UserGroup> existing = Maps.newHashMap();
        for (UserGroup ug : ugs) {
            existing.put(ug.getId(), ug);
        }

        List<UserGroup> merged = Lists.newArrayList();
        for (Map<String, String> entityDataMap : entityMap.values()) {
            String reqIdStr = entityDataMap.get("id");

            // 新增：新行
            if (Strings.isBlank(reqIdStr)) {
                merged.add(doBindEntity(new UserGroup(), entityDataMap));
                continue;
            }
            // 修改：已存在的
            Long requestId = Long.valueOf(reqIdStr);
            if (requestId != null && existing.get(requestId) != null) {
                merged.add(doBindEntity(existing.get(requestId), entityDataMap));
            }
        }
        return merged;
    }


    private Map<String, String> getParameters(ServletRequest request) {
        Validate.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> params = Maps.newLinkedHashMap();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String key = paramName;
            String[] values = request.getParameterValues(paramName);
            String value = null;
            if (values != null) {
                value = values.length == 1 ? values[0] : Strings.joinWith(",", values);
            }
            if (StringUtils.isBlank(value)) {
                continue;
            }
            params.put(key, value);
        }
        return params;
    }

    /**
     * 解析请求参数名称
     * 格式：propertyName_${index} -> Pair<propertyName,Index>
     *
     * @param key
     * @return
     */
    private Pair<String, Integer> parseParamName(String key) {
        Integer index = null;
        String propertyName = null;
        if (Strings.contains(key, "_")) {
            String[] keyPairs = Strings.split(key, "_");
            propertyName = keyPairs[0];
            if (Strings.isNumber(keyPairs[1])) {
                index = Integer.valueOf(keyPairs[1]);
            }
        }
        return Pair.of(propertyName, index);
    }

    protected <T> T doBindEntity(T entity, Map<String, String> map) {
        try {
            UserGroup pd = new UserGroup();
            DataBinder binder = new DataBinder(entity, "command");
            binder.setConversionService(conversionService);
            MutablePropertyValues pvs = new MutablePropertyValues(map);
            binder.bind(pvs);
            binder.close();
            return entity;
        } catch (Exception e) {
            throw new BusinessException("BIND_ENTITY_DATA_ERROR", "绑定对象值失败", e.getMessage());
        }

    }


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        super.referenceData(request, model);
    }
}
