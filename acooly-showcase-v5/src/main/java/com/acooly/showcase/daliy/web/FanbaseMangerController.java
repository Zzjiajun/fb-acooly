package com.acooly.showcase.daliy.web;

import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.daliy.entity.Fanbase;
import com.acooly.showcase.daliy.service.FanbaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage/showcase/daily/fanbase")
public class FanbaseMangerController extends AbstractShowcaseController<Fanbase, FanbaseService> {
    {
        allowMapping = "*";
    }
}
