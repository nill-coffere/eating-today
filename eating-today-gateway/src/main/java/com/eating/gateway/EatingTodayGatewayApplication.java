package com.eating.gateway;

import com.eating.feign.oauth.LoginApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses  = {LoginApi.class})
public class EatingTodayGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatingTodayGatewayApplication.class, args);
    }

}
