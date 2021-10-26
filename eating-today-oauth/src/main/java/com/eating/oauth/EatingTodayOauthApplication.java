package com.eating.oauth;

import com.eating.feign.redis.OauthRedisApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses  = {OauthRedisApi.class})
@MapperScan(value = "com.eating.oauth.mapper")
public class EatingTodayOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatingTodayOauthApplication.class, args);
    }

}
