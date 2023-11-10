package com.acooly.showcase;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;

/** @author qiubo */
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
