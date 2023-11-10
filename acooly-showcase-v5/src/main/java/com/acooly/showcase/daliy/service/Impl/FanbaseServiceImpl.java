package com.acooly.showcase.daliy.service.Impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.FanbaseDao;
import com.acooly.showcase.daliy.entity.Fanbase;
import com.acooly.showcase.daliy.service.FanbaseService;
import org.springframework.stereotype.Service;

@Service("FanbaseService")
public class FanbaseServiceImpl extends EntityServiceImpl<Fanbase, FanbaseDao> implements FanbaseService {
}
