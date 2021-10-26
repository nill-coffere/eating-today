package com.eating.redis.controller;

import com.eating.base.model.oauth.AuthUser;
import com.eating.base.util.FastJsonRedisSerializer;
import com.eating.redis.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(AuthUser.class);
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
    public <T> void setCacheObject(@RequestParam String key, @RequestParam T value){
        redisTemplate.opsForValue().set(key, value);
    }

    @PostMapping(value = "/cache-object", params = {"timeUnit"})
    public <T> void setCacheObject(@RequestParam String key, @RequestParam T value,
                                   @RequestParam Integer timeout, @RequestParam TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @PostMapping("/expire")
    public boolean expire(@RequestParam String key, @RequestParam long timeout){
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    @PostMapping(value = "/expire", params = {"unit"})
    public boolean expire(@RequestParam String key, @RequestParam long timeout, @RequestParam TimeUnit unit){
        return redisTemplate.expire(key, timeout, unit);
    }

    @GetMapping("/expire")
    public <T> T getCacheObject(@RequestParam String key){
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    @PostMapping(value = "/remove-object", params = "key")
    public boolean deleteObject(@RequestParam String key){
        return redisTemplate.delete(key);
    }

    @PostMapping(value = "/remove-object", params = "collection")
    public long deleteObject(@RequestParam Collection collection){
        return redisTemplate.delete(collection);
    }

    @PostMapping(value = "/cache-list", params = {"dataList"})
    public <T> long setCacheList(@RequestParam String key, @RequestParam List<T> dataList){
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    @PostMapping("/cache-list")
    public <T> List<T> getCacheList(@RequestParam String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @PostMapping(value = "/cache-set", params = {"dataSet"})
    public <T> long setCacheSet(@RequestParam String key, @RequestParam Set<T> dataSet){
        Long count = redisTemplate.opsForSet().add(key, dataSet);
        return count == null ? 0 : count;
    }

    @PostMapping("/cache-set")
    public <T> Set<T> getCacheSet(@RequestParam String key){
        return redisTemplate.opsForSet().members(key);
    }

    @PostMapping(value = "/cache-map", params = {"dataMap"})
    public <T> void setCacheMap(@RequestParam String key, @RequestParam Map<String, T> dataMap){
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    @GetMapping("/cache-map")
    public <T> Map<String, T> getCacheMap(@RequestParam String key){
        return redisTemplate.opsForHash().entries(key);
    }

    @PostMapping(value = "/cache-map-value", params = {"value"})
    public <T> void setCacheMapValue(@RequestParam String key, @RequestParam String hKey, @RequestParam T value){
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    @PostMapping("/cache-map-value")
    public <T> T getCacheMapValue(@RequestParam String key, @RequestParam String hKey){
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    @GetMapping("/multi-cache-map-value")
    public <T> List<T> getMultiCacheMapValue(@RequestParam String key, @RequestParam Collection<Object> hKeys){
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    @PostMapping("/keys")
    public Collection<String> keys(@RequestParam String pattern){
        return redisTemplate.keys(pattern);
    }

}
