/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-06-08
 */
package com.acooly.showcase.link.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.link.service.DmAccessService;
import com.acooly.showcase.link.dao.DmAccessDao;
import com.acooly.showcase.link.entity.DmAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dm_access Service实现
 *
 * @author acooly
 * @date 2024-06-08 09:34:47
 */
@Service("dmAccessService")
public class DmAccessServiceImpl extends EntityServiceImpl<DmAccess, DmAccessDao> implements DmAccessService {

    @Override
    public Integer countDistinctIp() {
        return this.getEntityDao().countDistinctIp();
    }

    @Override
    public void deleteAll() {
        this.getEntityDao().truncateYourTable();
    }

    @Override
    public List<Map<String, Object>> getWorldIpDistribution() {
        List<Map<String, Object>> raw = this.getEntityDao().selectWorldIpDistribution();
        for (Map<String, Object> row : raw) {
            String cn = (String) row.get("name");
            if (COUNTRY_CN2EN.containsKey(cn)) {
                row.put("name", COUNTRY_CN2EN.get(cn));
            }
        }
        return raw;
    }

    @Override
    public List<Map<String, Object>> getSupplierStats() {
        return this.getEntityDao().selectSupplierStats();
    }

    @Override
    public List<Map<String, Object>> getTrend() {
        return this.getEntityDao().selectTrend();
    }

    @Override
    public List<Map<String, Object>> getUserStats() {
        return this.getEntityDao().selectUserStats();
    }


    private static final Map<String, String> COUNTRY_CN2EN = new HashMap<String, String>() {{
        put("中国", "China");
        put("美国", "United States");
        put("日本", "Japan");
        put("德国", "Germany");
        put("法国", "France");
        put("英国", "United Kingdom");
        put("俄罗斯", "Russia");
        put("加拿大", "Canada");
        put("澳大利亚", "Australia");
        put("韩国", "South Korea");
        put("意大利", "Italy");
        put("西班牙", "Spain");
        put("印度", "India");
        put("巴西", "Brazil");
        put("墨西哥", "Mexico");
        put("新加坡", "Singapore");
        put("马来西亚", "Malaysia");
        put("泰国", "Thailand");
        put("越南", "Vietnam");
        put("菲律宾", "Philippines");
        put("印度尼西亚", "Indonesia");
        put("土耳其", "Turkey");
        put("南非", "South Africa");
        put("埃及", "Egypt");
        put("阿根廷", "Argentina");
        put("沙特阿拉伯", "Saudi Arabia");
        put("阿联酋", "United Arab Emirates");
        put("瑞士", "Switzerland");
        put("瑞典", "Sweden");
        put("荷兰", "Netherlands");
        put("比利时", "Belgium");
        put("奥地利", "Austria");
        put("丹麦", "Denmark");
        put("挪威", "Norway");
        put("芬兰", "Finland");
        put("波兰", "Poland");
        put("葡萄牙", "Portugal");
        put("希腊", "Greece");
        put("捷克", "Czechia");
        put("匈牙利", "Hungary");
        put("以色列", "Israel");
        put("新西兰", "New Zealand");
        put("爱尔兰", "Ireland");
        put("乌克兰", "Ukraine");
        put("哈萨克斯坦", "Kazakhstan");
        put("巴基斯坦", "Pakistan");
        put("孟加拉国", "Bangladesh");
        put("伊朗", "Iran");
        put("伊拉克", "Iraq");
        put("科威特", "Kuwait");
        put("卡塔尔", "Qatar");
        put("智利", "Chile");
        put("哥伦比亚", "Colombia");
        put("秘鲁", "Peru");
        put("委内瑞拉", "Venezuela");
    }};
}
