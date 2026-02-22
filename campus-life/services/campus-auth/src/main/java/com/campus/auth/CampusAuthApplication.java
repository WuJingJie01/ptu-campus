package com.campus.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CampusAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAuthApplication.class, args);
        System.out.println("server started!");
    }

}
