package com.phoenix.devops.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Optional;

/**
 * @author wjj-phoenix
 * @since 2024-11-14
 * 打印应用相关上下文信息
 */
@Slf4j
@Component
public class LoggerApplicationInfoPrinter implements ApplicationListener<AvailabilityChangeEvent<ReadinessState>> {
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent<ReadinessState> availabilityChangeEvent) {
        // ReadinessState.ACCEPTING_TRAFFIC 表示可以接受流量
        if (availabilityChangeEvent.getState() == ReadinessState.ACCEPTING_TRAFFIC) {
            try {
                ConfigurableApplicationContext context = (ConfigurableApplicationContext) availabilityChangeEvent.getSource();
                ConfigurableEnvironment environment = context.getEnvironment();
                String serverPort = Optional.ofNullable(environment.getProperty("local.server.port")).orElse(environment.getProperty("server.port"));

                // 获取Java虚拟机内存管理的MXBean
                MemoryUsage heapUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

                log.info("==============================");
                log.info("[{}] JVM堆内存使用情况: 初始值={}MB, 最大值={}MB, 当前值={}MB", applicationName, heapUsage.getInit() / 1024 / 1024, heapUsage.getMax() / 1024 / 1024, heapUsage.getUsed() / 1024 / 1024);
                log.info("[{}}] 项目启动端口: {}, 项目启动耗时: {} 秒", applicationName, serverPort, ManagementFactory.getRuntimeMXBean().getUptime() / 1000f);
                log.info("==============================");
            } catch (Exception ex) {
                // ignore
                log.error("项目启动出现异常: {}", ex.getMessage(), ex);
            }
        }
    }
}
