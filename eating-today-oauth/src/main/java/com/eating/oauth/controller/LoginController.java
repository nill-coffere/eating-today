package com.eating.oauth.controller;

import com.eating.base.model.oauth.User;
import com.eating.base.response.Response;
import com.eating.oauth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author han bin
 **/
@RestController
@RequestMapping("/oauth")
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Response login(@RequestParam String userName, @RequestParam String passWord){
        return loginService.login(userName, passWord);
    }

    @PostMapping("/verify/token")
    public Response verifyToken(@RequestParam String token){
        return loginService.verifyToken(token);
    }

    @PostMapping("/registrantion")
    public Response registrantion(@RequestBody User user){
        return loginService.registrantion(user);
    }

}
