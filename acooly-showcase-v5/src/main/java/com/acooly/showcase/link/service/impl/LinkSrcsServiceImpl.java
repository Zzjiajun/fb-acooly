/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-28
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.LinkSrcsService;
import com.acooly.showcase.link.dao.LinkSrcsDao;
import com.acooly.showcase.link.entity.LinkSrcs;

/**
 * link_srcs Service实现
 *
 * @author acooly
 * @date 2024-03-28 14:56:12
 */
@Service("linkSrcsService")
public class LinkSrcsServiceImpl extends EntityServiceImpl<LinkSrcs, LinkSrcsDao> implements LinkSrcsService {

}
