/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-02-15
 */
package com.acooly.showcase.stemp.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.stemp.service.EmSumdataService;
import com.acooly.showcase.stemp.dao.EmSumdataDao;
import com.acooly.showcase.stemp.entity.EmSumdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * em_sumdata Service实现
 *
 * @author acooly
 * @date 2025-02-15 14:50:19
 */
@Service("emSumdataService")
public class EmSumdataServiceImpl extends EntityServiceImpl<EmSumdata, EmSumdataDao> implements EmSumdataService {

}
