package com.eating.oauth.service;

import com.eating.base.model.oauth.AuthUser;
import com.eating.base.model.oauth.RedisAuthUser;
import com.eating.base.util.Constants;
import com.eating.base.util.Random;
import com.eating.feign.redis.OauthRedisApi;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author han bin
 **/
@Service
public class OauthTokenService {

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private int expireTime;

    private OauthRedisApi oauthRedisApi;

    @Autowired
    public OauthTokenService(OauthRedisApi oauthRedisApi){
        this.oauthRedisApi = oauthRedisApi;
    }

    private static final long MILLIS_SECOND = 1000;

    private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;


    public void verifyToken(AuthUser loginUser){
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN){
            refreshToken(loginUser);
        }
    }

    public AuthUser getLoginUser(String token){
        if (!Objects.isNull(token)){
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            RedisAuthUser user = oauthRedisApi.getCacheObject(userKey);
            return new AuthUser(user);
        }
        return null;
    }

    public AuthUser getLoginUser(HttpServletRequest request){
        String token = getToken(request);
        if (!Objects.isNull(token)){
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            RedisAuthUser user = oauthRedisApi.getCacheObject(userKey);
            return new AuthUser(user);
        }
        return null;
    }

    private String getToken(HttpServletRequest request){
        String token = request.getHeader(header);
        if (!Objects.isNull(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getTokenKey(String uuid){
        return String.format("%s%s",Constants.LOGIN_TOKEN_KEY, uuid);
    }

    public String createToken(AuthUser user){
        String token = Random.getRandomCharCap(32);
        user.setToken(token);
        refreshToken(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    public String createToken(Map<String, Object> claims){
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    public void refreshToken(AuthUser user) {
        user.setLoginTime(System.currentTimeMillis());
        user.setExpireTime(user.getLoginTime() + expireTime * MILLIS_MINUTE);
        String userKey = getTokenKey(user.getToken());
        oauthRedisApi.setCacheObject(userKey, new RedisAuthUser(user), expireTime, TimeUnit.MINUTES);
    }

    public void removeLoginUser(String token){
        if (!Objects.isNull(token)){
            String userKey = getTokenKey(token);
            oauthRedisApi.deleteObject(userKey);
        }
    }
}
