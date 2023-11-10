/**
 * acooly-showcase
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-02-25 00:38
 */
package com.acooly.showcase;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-02-25 00:38
 */
@Configuration
public class ShowcaseV4AutoConfig {

    @Bean
    public StandardDatabaseScriptIniter showcaseV4ScriptIniter() {
        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "dm_customer";
            }

            @Override
            public String getComponentName() {
                return "showcaseV4";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("ddl", "resource");
            }
        };
    }
}
