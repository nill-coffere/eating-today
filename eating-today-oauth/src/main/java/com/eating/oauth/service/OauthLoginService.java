package com.eating.oauth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eating.base.model.oauth.AuthUser;
import com.eating.base.model.oauth.User;
import com.eating.oauth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author han bin
 **/
@Primary
@Service
public class OauthLoginService implements UserDetailsService {

    private UserMapper userMapper;

    @Autowired
    public OauthLoginService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wq = new LambdaQueryWrapper<>();
        wq.eq(User::getUserName,username);

        User user = userMapper.selectOne(wq);

        Integer status = 0;
        String info = "Login Success";
        if(Objects.isNull(user)){
            status = 1;
            info = "User does not exist.";
        }else if(!user.getIsActive()){
            status = 2;
            info = "Users have been disabled.";
        }

        return UserDetailConverter.convert(user, status, info);
    }

    private static class UserDetailConverter {
        static AuthUser convert(User user, int status, String info) {
            AuthUser authUser = new AuthUser(user);
            authUser.setLoginStatus(status);
            authUser.setLoginInfo(info);
            return authUser;
        }
    }
}
