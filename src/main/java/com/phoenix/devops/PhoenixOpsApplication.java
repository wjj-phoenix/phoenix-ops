package com.phoenix.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableScheduling
@EnableWebSecurity
@SpringBootApplication
@EnableConfigurationProperties
public class PhoenixOpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoenixOpsApplication.class, args);
    }
}
