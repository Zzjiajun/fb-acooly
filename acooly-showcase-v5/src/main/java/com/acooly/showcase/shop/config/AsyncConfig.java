package com.acooly.showcase.shop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理和定时任务配置
 * 用于图片压缩和OSS上传的异步处理
 * 用于统计聚合的定时任务
 *
 * @author acooly
 * @date 2025-12-18
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling  // 启用定时任务支持（如果使用Spring的@Scheduled注解）
public class AsyncConfig {

    /**
     * 图片处理线程池
     * 用于图片压缩和OSS上传的异步处理
     */
    @Bean(name = "imageProcessExecutor")
    public Executor imageProcessExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 核心线程数
        executor.setMaxPoolSize(10);  // 最大线程数
        executor.setQueueCapacity(100);  // 队列容量
        executor.setThreadNamePrefix("image-process-");  // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  // 拒绝策略：调用者运行
        executor.setWaitForTasksToCompleteOnShutdown(true);  // 关闭时等待任务完成
        executor.setAwaitTerminationSeconds(60);  // 等待时间（秒）
        executor.initialize();
        log.info("【异步配置】图片处理线程池初始化完成 - 核心线程数: {}, 最大线程数: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize());
        return executor;
    }
}


