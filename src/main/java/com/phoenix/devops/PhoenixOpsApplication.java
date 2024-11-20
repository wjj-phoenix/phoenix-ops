package com.phoenix.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@EnableScheduling
// @EnableWebSecurity
@SpringBootApplication
public class PhoenixOpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoenixOpsApplication.class, args);
    }
}
