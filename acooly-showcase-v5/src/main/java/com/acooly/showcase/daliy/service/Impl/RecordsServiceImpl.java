package com.acooly.showcase.daliy.service.Impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.RecordsDao;
import com.acooly.showcase.daliy.entity.Records;
import com.acooly.showcase.daliy.service.RecordsService;
import org.springframework.stereotype.Service;

@Service("RecordsService")
public class RecordsServiceImpl extends EntityServiceImpl<Records, RecordsDao> implements RecordsService {
}
