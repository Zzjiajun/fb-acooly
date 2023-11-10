package com.acooly.showcase.daliy.web;


import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.daliy.entity.Transactionss;

import com.acooly.showcase.daliy.service.AccountsService;
import com.acooly.showcase.daliy.service.TransactionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage/showcase/daily/transactionss")
public class TransactionsMangerController extends AbstractShowcaseController<Transactionss, TransactionsService> {
    {
        allowMapping = "*";
    }

    @Autowired
    private AccountsService accountsService;
}
