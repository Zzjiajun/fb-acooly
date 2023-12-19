/*
 * acooly.cn Inc.
 * Copyright (c) 2023 All Rights Reserved.
 * create by acooly
 * date:2023-12-14
 */
package com.acooly.showcase.daliy.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.service.LinkService;
import com.acooly.showcase.daliy.dao.LinkDao;
import com.acooly.showcase.daliy.entity.Link;

/**
 * dm_link Service实现
 *
 * @author acooly
 * @date 2023-12-14 17:10:52
 */
@Service("linkService")
public class LinkServiceImpl extends EntityServiceImpl<Link, LinkDao> implements LinkService {

}
