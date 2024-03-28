/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-28
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.LinkIntService;
import com.acooly.showcase.link.dao.LinkIntDao;
import com.acooly.showcase.link.entity.LinkInt;

/**
 * link_int Service实现
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Service("linkIntService")
public class LinkIntServiceImpl extends EntityServiceImpl<LinkInt, LinkIntDao> implements LinkIntService {

}
