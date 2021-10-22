package com.eating.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.eating.service.*"})
public class EatingTodayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatingTodayServiceApplication.class, args);
    }

}
