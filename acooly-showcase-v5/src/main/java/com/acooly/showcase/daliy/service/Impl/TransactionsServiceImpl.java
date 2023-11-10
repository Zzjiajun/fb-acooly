package com.acooly.showcase.daliy.service.Impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.TransactionsDao;
import com.acooly.showcase.daliy.entity.Transactionss;
import com.acooly.showcase.daliy.service.TransactionsService;
import org.springframework.stereotype.Service;

@Service("TransactionsService")
public class TransactionsServiceImpl extends EntityServiceImpl<Transactionss, TransactionsDao> implements TransactionsService {
    @Override
    public Float getOutFolat(String outName) {
        return this.getEntityDao().getOutFolat(outName);
    }

    @Override
    public Float getInFolat(String outName) {
        return this.getEntityDao().getInFolat(outName);
    }
}
