package com.eating.feign.oauth;

import com.eating.base.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author han bin
 **/
@FeignClient(value = "EATING-OAUTH")
@RequestMapping("/oauth")
public interface LoginApi {

    @PostMapping("/login")
    public Response login(@RequestParam String userName, @RequestParam String passWord);

    @PostMapping("/verify/token")
    public Response verifyToken(@RequestParam String token);

}
