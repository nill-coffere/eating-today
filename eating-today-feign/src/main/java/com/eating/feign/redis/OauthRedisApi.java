package com.eating.feign.redis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author han bin
 **/
@FeignClient(value = "EATING-REDIS", url = "http://192.168.3.13:7005")
@RequestMapping("/redis")
public interface OauthRedisApi {

    @PostMapping("/getRedisTemplate")
    public RedisTemplate getRedisTemplateByDataBase(Integer database);

//    @PostMapping("/keys")
//    public Collection<String> keys(@RequestParam String pattern);

    @PostMapping("/cache-object")
    public <T> void setCacheObject(@RequestParam String key, @RequestParam T value);

    @PostMapping(value = "/cache-object", params = {"timeUnit"})
    public <T> void setCacheObject(@RequestParam String key, @RequestParam T value,
                                   @RequestParam Integer timeout, @RequestParam TimeUnit timeUnit);

//    @PostMapping("/expire")
//    public boolean expire(@RequestParam String key, @RequestParam long timeout);
//
//    @PostMapping(value = "/expire", params = {"unit"})
//    public boolean expire(@RequestParam String key, @RequestParam long timeout, @RequestParam TimeUnit unit);

    @GetMapping("/expire")
    public <T> T getCacheObject(@RequestParam String key);

    @PostMapping(value = "/remove-object", params = "key")
    public boolean deleteObject(@RequestParam String key);

//    @PostMapping(value = "/remove-object", params = "collection")
//    public long deleteObject(@RequestParam Collection collection);
//
//    @PostMapping(value = "/cache-list", params = {"dataList"})
//    public <T> long setCacheList(@RequestParam String key, @RequestParam List<T> dataList);
//
//    @PostMapping("/cache-list")
//    public <T> List<T> getCacheList(@RequestParam String key);
//
//    @PostMapping(value = "/cache-set", params = {"dataSet"})
//    public <T> long setCacheSet(@RequestParam String key, @RequestParam Set<T> dataSet);
//
//    @PostMapping("/cache-set")
//    public <T> Set<T> getCacheSet(@RequestParam String key);
//
//    @PostMapping(value = "/cache-map", params = {"dataMap"})
//    public <T> void setCacheMap(@RequestParam String key, @RequestParam Map<String, T> dataMap);
//
//    @GetMapping("/cache-map")
//    public <T> Map<String, T> getCacheMap(@RequestParam String key);
//
//    @PostMapping(value = "/cache-map-value", params = {"value"})
//    public <T> void setCacheMapValue(@RequestParam String key, @RequestParam String hKey, @RequestParam T value);
//
//    @PostMapping("/cache-map-value")
//    public <T> T getCacheMapValue(@RequestParam String key, @RequestParam String hKey);
//
//    @GetMapping("/multi-cache-map-value")
//    public <T> List<T> getMultiCacheMapValue(@RequestParam String key, @RequestParam Collection<Object> hKeys);

}
