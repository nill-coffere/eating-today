package com.eating.feign.redis;

import com.eating.base.model.oauth.RedisAuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author han bin
 **/
@FeignClient(value = "EATING-REDIS", url = "http://192.168.3.13:7005")
@RequestMapping("/redis")
public interface OauthRedisApi {

    @PostMapping("/cache-object")
    public void setCacheObject(@RequestParam String key, @RequestBody RedisAuthUser value);

    @PostMapping(value = "/cache-object", params = {"timeUnit"})
    public void setCacheObject(@RequestParam String key, @RequestBody RedisAuthUser value,
                                   @RequestParam Integer timeout, @RequestParam TimeUnit timeUnit);

    @GetMapping(value = "/expire", params = {"key"})
    public RedisAuthUser getCacheObject(@RequestParam String key);

    @PostMapping(value = "/remove-object", params = "key")
    public boolean deleteObject(@RequestParam String key);

}
