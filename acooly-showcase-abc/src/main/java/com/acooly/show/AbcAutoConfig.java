package com.acooly.show;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-25 16:57
 */
@Configuration
@EnableConfigurationProperties({AbcProperties.class})
@ComponentScan
public class AbcAutoConfig {
    @Autowired private AbcProperties abcProperties;

}
