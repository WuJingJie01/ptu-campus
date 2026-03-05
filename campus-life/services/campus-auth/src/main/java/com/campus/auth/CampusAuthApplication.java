package com.campus.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.security.mapper")
public class CampusAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAuthApplication.class, args);
        System.out.println("campus-auth server started!");
    }

}
