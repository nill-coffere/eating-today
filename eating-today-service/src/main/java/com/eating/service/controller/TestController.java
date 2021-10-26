package com.eating.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author han bin
 **/
@RestController
public class TestController {

    @PostMapping("/hello")
    public String sayHelloEatingService(@RequestParam String username){
        return "hello eating service" + username;
    }

}
