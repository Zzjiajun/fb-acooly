package com.acooly.showcase.daliy.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.daliy.entity.Transactionss;
import org.springframework.data.repository.query.Param;

import java.util.Map;

public interface TransactionsService extends EntityService<Transactionss> {
    Float getOutFolat( String outName);
    Float getInFolat( String outName);
}
