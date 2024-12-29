package com.scholar.securitytest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.scholar.securitytest.mapper")//扫描mapper
public class SecurityTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SecurityTestApplication.class, args);
        System.out.println("args:"+ args);
    }

}
