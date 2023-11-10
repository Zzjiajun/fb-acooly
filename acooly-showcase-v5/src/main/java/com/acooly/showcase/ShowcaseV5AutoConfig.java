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
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-02-25 00:38
 */
@Configuration
public class ShowcaseV5AutoConfig {

    @Bean
    public StandardDatabaseScriptIniter showcaseV5ScriptIniter() {
        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "ac_showcase_member";
            }

            @Override
            public String getComponentName() {
                return "showcaseV5";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("ddl", "resource");
            }
        };
    }
}
