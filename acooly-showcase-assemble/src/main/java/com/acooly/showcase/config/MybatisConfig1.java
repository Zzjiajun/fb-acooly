package com.acooly.showcase.config;

import com.acooly.module.ds.DruidProperties;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MybatisConfig1 {


//    @Bean
//    //DruidDataSource的bean name=xxDataSource，读取环境配置前缀yiji.ds1
//    public DruidDataSource linkDataSource() {
//        return DruidProperties.buildFromEnv("table.ds");
//    }
//    @Bean
//    //DruidDataSource的bean name=yyDataSource，读取环境配置前缀yiji.ds2
//    public DruidDataSource tableDataSource() {
//        return DruidProperties.buildFromEnv("table.ds");
//    }
}
