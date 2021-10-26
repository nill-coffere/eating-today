package com.eating.oauth.service;

import com.eating.base.model.oauth.AuthUser;
import com.eating.base.model.oauth.User;
import com.eating.base.response.Response;
import com.eating.base.response.Result;
import com.eating.base.response.ResultCode;
import com.eating.oauth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author han bin
 **/
@Service
public class LoginService {

    private OauthTokenService tokenService;

    private AuthenticationManager authenticationManager;

    private UserMapper userMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public LoginService(OauthTokenService tokenService,
                        AuthenticationManager authenticationManager,
                        UserMapper userMapper){
        this.tokenService = tokenService;
        this.authenticationManager =authenticationManager;
        this.userMapper = userMapper;
    }

    public Response login(String userName, String passWord){
        Authentication authentication = null;
        try{
            // Check User
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userName, passWord));
        }catch (Exception e){
            e.printStackTrace();
            return new Response.ResponseBuilder().result(null,false).build();
        }
        if(Objects.isNull(authentication)){
            return new Response.ResponseBuilder().result(null,false).build();
        }
        AuthUser loginUser = (AuthUser) authentication.getPrincipal();
        if(Objects.isNull(loginUser)){
            return new Response.ResponseBuilder().result(null,false).build();
        }
        return new Response.ResponseBuilder().result(null,true)
                .module(loginUser.getUser())
                .token(tokenService.createToken(loginUser))
                .build();
    }

    public Response verifyToken(String token){
        AuthUser loginUser = tokenService.getLoginUser(token);
        if(Objects.isNull(loginUser)){
            return new Response.ResponseBuilder().result(null,false).build();
        }else{
            if(tokenService.verifyToken(loginUser)){
                return new Response.ResponseBuilder().module(loginUser).result(null,true).build();
            }else{
                return new Response.ResponseBuilder()
                        .result(new Result.ResultBuilder()
                                .code(ResultCode.HTTP_TOKEN_TIMEOUT)
                                .message("Token expired. Please login again.")
                                .build(),false)
                        .build();
            }
        }
    }

    public Response registrantion(User user){
        String passWord = user.getPassWord();
        try{
            user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
            int count = userMapper.insert(user);
            if(count <= 0){
                return new Response.ResponseBuilder().result(null,false).build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response.ResponseBuilder().result(new Result
                    .ResultBuilder()
                    .code(ResultCode.HTTP_ERROR)
                    .message("This user already exists.")
                    .build(),null)
                    .build();
        }
        return login(user.getUserName(), passWord);
    }

}
