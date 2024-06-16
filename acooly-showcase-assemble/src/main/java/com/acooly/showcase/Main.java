package com.acooly.showcase;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import com.acooly.module.ds.DruidProperties;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

/** @author qiubo  */
// 设置应用名和tomcat端口，日志路径为/var/log/webapps/${sysName}
@BootApp(sysName = "acooly-showcase", httpPort = 8083)
public class Main {
  public static final String PROFILE = "dev";

  public static void main(String[] args) {
    // 设置默认环境标识
    Apps.setProfileIfNotExists(PROFILE);
    new SpringApplication(Main.class).run(args);
  }
}
