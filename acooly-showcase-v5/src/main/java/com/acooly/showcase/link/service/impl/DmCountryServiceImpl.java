/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-06
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmCountryService;
import com.acooly.showcase.link.dao.DmCountryDao;
import com.acooly.showcase.link.entity.DmCountry;

/**
 * dm_country Service实现
 *
 * @author acooly
 * @date 2024-06-06 04:06:20
 */
@Service("dmCountryService")
public class DmCountryServiceImpl extends EntityServiceImpl<DmCountry, DmCountryDao> implements DmCountryService {

}
