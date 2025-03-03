/*
 * acooly.cn Inc.
 * Copyright (c) 2025 All Rights Reserved.
 * create by acooly
 * date:2025-02-19
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.BoardService;
import com.acooly.showcase.link.dao.BoardDao;
import com.acooly.showcase.link.entity.Board;

/**
 * dm_board Service实现
 *
 * @author acooly
 * @date 2025-02-19 18:45:30
 */
@Service("boardService")
public class BoardServiceImpl extends EntityServiceImpl<Board, BoardDao> implements BoardService {

}
