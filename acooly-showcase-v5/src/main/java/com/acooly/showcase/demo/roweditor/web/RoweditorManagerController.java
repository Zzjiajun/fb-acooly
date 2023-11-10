/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-11-14
 */
package com.acooly.showcase.demo.roweditor.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.member.common.enums.CustomerTypeEnum;
import com.acooly.showcase.member.common.enums.IdcardTypeEnum;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.demo.roweditor.entity.Roweditor;
import com.acooly.showcase.demo.roweditor.service.RoweditorService;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.utils.enums.SimpleStatus;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 行编辑实例 管理控制器
 *
 * @author acooly
 * @date 2021-11-14 16:15:12
 */
@Controller
@RequestMapping(value = "/manage/demo/roweditor/roweditor")
public class RoweditorManagerController extends AbstractShowcaseController<Roweditor, RoweditorService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private RoweditorService roweditorService;


    @RequestMapping(value = {"data"})
    @ResponseBody
    public Object data(HttpServletRequest request, HttpServletResponse response) {
        List list = new ArrayList<>();
        String name = Servlets.getParameter("name");
        if (Strings.equalsIgnoreCase(name, Gender.class.getSimpleName())) {
            for (Gender status : Gender.values()) {
                list.add(new OptionData(status.code(), status.message()));
            }
        } else if (Strings.equalsIgnoreCase(name, AnimalSign.class.getSimpleName())) {
            for (AnimalSign status : AnimalSign.values()) {
                list.add(new OptionData(status.code(), status.message()));
            }
        } else if (Strings.equalsIgnoreCase(name, ChannelEnum.class.getSimpleName())) {
            for (ChannelEnum status : ChannelEnum.values()) {
                list.add(new OptionData(status.code(), status.message()));
            }
            return list;
        } else if (Strings.equalsIgnoreCase(name, SimpleStatus.class.getSimpleName())) {
            for (SimpleStatus status : SimpleStatus.values()) {
                list.add(new OptionData(status.code(), status.message()));
            }
        }
        return list;
    }

    @RequestMapping(value = {"mapping"})
    @ResponseBody
    public Object mapping(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapping = referenceData(request);
        return mapping;
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allGenders", Gender.mapping());
        model.put("allAnimals", AnimalSign.mapping());
        model.put("allRegistryChannels", ChannelEnum.mapping());
        model.put("allStatuss", SimpleStatus.mapping());
    }

}
