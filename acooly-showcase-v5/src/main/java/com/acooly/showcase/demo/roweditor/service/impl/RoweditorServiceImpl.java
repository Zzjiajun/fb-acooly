/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-11-14
 */
package com.acooly.showcase.demo.roweditor.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.demo.roweditor.service.RoweditorService;
import com.acooly.showcase.demo.roweditor.dao.RoweditorDao;
import com.acooly.showcase.demo.roweditor.entity.Roweditor;

/**
 * 行编辑实例 Service实现
 *
 * @author acooly
 * @date 2021-11-14 16:15:13
 */
@Service("roweditorService")
public class RoweditorServiceImpl extends EntityServiceImpl<Roweditor, RoweditorDao> implements RoweditorService {

}
