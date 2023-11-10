package com.acooly.showcase.test;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.ds.DruidProperties;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class DsTest {

//    @Test
//    public void testCreateDatabase() {
//        try {
//            DruidDataSource xdataSource = new DruidDataSource();
//            String url = DruidProperties.normalizeUrl("jdbc:mysql://127.0.0.1:3306/table_test");
//            xdataSource.setUrl(url);
//            String dataBase = "table_test";
//            log.warn("配置文件中指定的数据库[{}]不存在，尝试创建数据库", dataBase);
//            String createDataBaseSql = "CREATE SCHEMA `" + dataBase + "` DEFAULT CHARACTER SET utf8mb4 collate utf8mb4_unicode_ci;";
//            xdataSource.setUsername("root");
//            xdataSource.setPassword("824519Jj");
//            xdataSource.init();
//            try (Connection connection = xdataSource.getConnection()) {
//                try {
//                    ScriptUtils.executeSqlScript(
//                            connection, new ByteArrayResource(createDataBaseSql.getBytes(Charsets.UTF_8)));
//                    log.warn("创建数据库[{}]成功", dataBase);
//                } catch (ScriptException e1) {
//                    throw new AppConfigException("druid连接池自动创建数据库失败", e1);
//                }
//            }
//            xdataSource.close();
//        } catch (SQLException e1) {
//            throw new AppConfigException("druid连接池初始化失败", e1);
//        }
//
//    }


    @Test
    public void testDirSize() throws Exception {

        String path = "D:\\var\\log\\webapps\\acooly-showcase\\nfs";
        long dirSize = getDirSize(new File(path));
        System.out.println(dirSize);

        log.info("dir size:{}b, {}M",dirSize,dirSize/1024/1024);

        File pathFile = new File(path);
        long dirSize1 = Files.size(pathFile.toPath());
        System.out.println(dirSize1);
    }

    public long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        for (File subFile : files) {
            if (subFile.isFile()) {
                size += subFile.length();
            } else {
                size += getDirSize(subFile);
            }
        }
        return size;
    }

}
