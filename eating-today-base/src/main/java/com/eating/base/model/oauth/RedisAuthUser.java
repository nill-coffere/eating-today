package com.eating.base.model.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

/**
 * @Author han bin
 **/
@Data
@RedisHash("AuthUser")
@NoArgsConstructor
public class RedisAuthUser {
    private User user;

    private String token;

    private Long loginTime;

    private Long expireTime;

    private String loginInfo;

    private Integer loginStatus;

    public RedisAuthUser(AuthUser user){
        this.user = user.getUser();
        this.token = user.getToken();
        this.expireTime = user.getExpireTime();
        this.loginTime = user.getLoginTime();
        this.loginInfo = user.getLoginInfo();
        this.loginStatus = user.getLoginStatus();
    }
}
