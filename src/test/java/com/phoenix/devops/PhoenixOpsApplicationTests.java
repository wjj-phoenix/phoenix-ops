package com.phoenix.devops;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PhoenixOpsApplicationTests {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
