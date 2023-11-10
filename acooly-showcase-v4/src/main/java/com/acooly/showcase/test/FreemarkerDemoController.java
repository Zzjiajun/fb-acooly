/**
 * acooly-showcase
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-04-11 15:38
 */
package com.acooly.showcase.test;

import com.acooly.core.utils.Dates;
import com.acooly.showcase.common.enums.CustomerType;
import com.acooly.showcase.common.enums.IdcardType;
import com.acooly.showcase.jpa.customer.entity.Customer;
import com.acooly.showcase.jpa.customer.web.CustomerManagerController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Freemarker 演示控制器
 *
 * @author zhangpu
 * @date 2019-04-11 15:38
 */
@Slf4j
@Controller
@RequestMapping("/freemarker/demo")
public class FreemarkerDemoController {

    static final int LIST_SIZE = 10;
    Map<String, String> USER = JSON.parseObject("{'zhangpu':'张浦','kule':'苦乐','cfuqing':'富强'," +
            "'jiangdao':'剪刀','zhike':'志客','xiyang':'夕阳','fazheng':'法正'}", Map.class);

    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("arrays",new String[]{"字符串1","字符串2","字符串3"});
        model.addAttribute("entity", getEntity(1L));
        model.addAttribute("entities", getListEntity());
        model.addAttribute("obj",null);
        model.addAllAttributes(referenceData(request));
        request.setAttribute("requestAttr", "请求对象属性");
        request.getSession(true).setAttribute("sessionAttr", "这是会话Session变量值");
        request.getServletContext().setAttribute("applcationAttr", "Application变量值");

        return "/freemarker/demo/index";
    }

    @RequestMapping("header")
    public String header(HttpServletRequest request, Model model) {
        model.addAttribute("descn", "通过@includePage自定义标签实现服务器端include");
        return "/freemarker/demo/header";
    }


    protected List<Customer> getListEntity() {
        List<Customer> customers = Lists.newArrayList();
        for (long i = 1; i <= LIST_SIZE; i++) {
            customers.add(getEntity(i));
        }
        return customers;
    }


    protected Customer getEntity(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setUsername(Lists.newArrayList(USER.keySet()).get(getRandom(USER.size() - 1)));
        customer.setRealName(USER.get(customer.getUsername()));
        int age = getRandom(25, 55);
        customer.setAge(age);
        customer.setBirthday(Dates.addYear(new Date(), -age));
        customer.setGender(getRandom(0, 1));
        customer.setIdcardType(IdcardType.cert);
        customer.setIdcardNo("1234123412341234");
        customer.setMobileNo("13989873641");
        customer.setMail(customer.getUsername() + "@acooly.cn");
        customer.setCustomerType(CustomerType.normal);
        customer.setFee(null);
        customer.setSalary(getRandom(1000, 5000) * 100L);
        customer.setStatus(Customer.STATUS_ENABLE);
        customer.setCreateTime(new Date());
        customer.setUpdateTime(null);
        return customer;
    }


    protected int getRandom(int max) {
        return getRandom(0, max);
    }

    protected int getRandom(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }

    protected Map<String, Object> referenceData(HttpServletRequest request) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("allGenders", CustomerManagerController.OptionMapping.allGenders);
        model.put("allIdcardTypes", IdcardType.mapping());
        model.put("allCustomerTypes", CustomerType.mapping());
        model.put("allStatuss", CustomerManagerController.OptionMapping.allStatuss);
        return model;
    }
}
