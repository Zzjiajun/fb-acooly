package com.acooly.showcase.daliy.service.Impl;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.AccountsDao;
import com.acooly.showcase.daliy.entity.Accounts;
import com.acooly.showcase.daliy.service.AccountsService;
import org.springframework.stereotype.Service;

@Service("AccountsService")
public class AccountsServiceImpl extends EntityServiceImpl<Accounts, AccountsDao> implements AccountsService {
}
