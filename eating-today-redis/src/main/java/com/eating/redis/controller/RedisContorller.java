package com.eating.redis.controller;

import com.eating.redis.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author han bin
 **/
@RestController
@RequestMapping("/redis")
public class RedisContorller {

   private RedisConfig redisConfig;

   @Autowired
   public RedisContorller(RedisConfig redisConfig){
      this.redisConfig = redisConfig;
   }

   @PostMapping("/getRedisTemplate")
   public RedisTemplate getRedisTemplateByDataBase(Integer database){
       return redisConfig.getRedisTemplate(database);
   }

}
