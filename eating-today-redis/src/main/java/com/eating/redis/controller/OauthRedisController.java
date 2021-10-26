package com.eating.redis.controller;

import com.eating.base.model.oauth.RedisAuthUser;
import com.eating.base.util.FastJsonRedisSerializer;
import com.eating.redis.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author han bin
 **/
@RestController
@RequestMapping("/redis")
public class OauthRedisController {

    private RedisTemplate redisTemplate;

    private RedisConfig redisConfig;

    @Autowired
    public OauthRedisController(RedisConfig redisConfig){
        this.redisConfig = redisConfig;
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(RedisAuthUser.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate =  redisConfig.getRedisTemplate(0);
        if(Objects.isNull(redisTemplate)){
            return;
        }
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
    }

    @PostMapping("/cache-object")
    public void setCacheObject(@RequestParam String key, @RequestBody RedisAuthUser value){
        redisTemplate.opsForValue().set(key, value);
    }

    @PostMapping(value = "/cache-object", params = {"timeUnit"})
    public void setCacheObject(@RequestParam String key, @RequestBody RedisAuthUser value,
                                   @RequestParam Integer timeout, @RequestParam TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @GetMapping(value = "/expire", params = {"key"})
    public RedisAuthUser getCacheObject(@RequestParam String key){
        ValueOperations<String, RedisAuthUser> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    @PostMapping(value = "/remove-object", params = "key")
    public boolean deleteObject(@RequestParam String key){
        return redisTemplate.delete(key);
    }
}
