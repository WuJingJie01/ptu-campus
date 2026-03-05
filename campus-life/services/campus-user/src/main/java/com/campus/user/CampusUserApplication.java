package com.campus.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.security.mapper")
public class CampusUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusUserApplication.class, args);
        System.out.println("server started!");
    }

}
