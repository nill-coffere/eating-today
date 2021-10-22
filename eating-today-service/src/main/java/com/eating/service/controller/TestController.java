package com.eating.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author han bin
 **/
@RestController
public class TestController {

    @GetMapping("/hello")
    public String sayHelloEatingService(@RequestParam String username){
        return "hello eating service" + username;
    }

}
