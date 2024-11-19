package com.phoenix.devops.configuration;

import com.phoenix.devops.monitor.LoggerApplicationInfoPrinter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wjj-phoenix
 * @since 2024-11-14
 */
@Configuration
public class ILoggerConfiguration {
    // 打印应用监控信息
    @Bean
    public LoggerApplicationInfoPrinter easyLogApplicationInfoPrinter() {
        return new LoggerApplicationInfoPrinter();
    }
}
