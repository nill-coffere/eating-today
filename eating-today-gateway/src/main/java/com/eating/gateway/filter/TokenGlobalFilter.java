package com.eating.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.eating.base.response.Response;
import com.eating.base.response.ResultCode;
import com.eating.base.util.Constants;
import com.eating.feign.oauth.LoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author han bin
 **/
@Component
@EnableFeignClients
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    private LoginApi loginApi;

    private final static Map<String, String> GATEWAY_WHITE_LIST = new HashMap<>();

    static{
        GATEWAY_WHITE_LIST.put("/eating-oauth/oauth/login", "");
        GATEWAY_WHITE_LIST.put("/eating-oauth/oauth/registrantion", "");
    }

    @Autowired
    public TokenGlobalFilter(LoginApi loginApi){
        this.loginApi = loginApi;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<Void> result = getReturnMessage(exchange.getResponse(), ResultCode.HTTP_ERROR, "Please log in to visit.");
        try{
            ServerHttpRequest httpRequest = exchange.getRequest();
            String method = httpRequest.getMethodValue();
            // Filter white list
            if (GATEWAY_WHITE_LIST.containsKey(httpRequest.getPath().value())) {
                return chain.filter(exchange);
            }
            // Open interface must be POST
            // Internal call can be GET
            if("POST".equals(method)){
                List<String> authorization = httpRequest.getHeaders().get("Authorization");
                if(!CollectionUtils.isEmpty(authorization)){
                    String token = authorization.get(0);
                    if (!Objects.isNull(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
                        token = token.replace(Constants.TOKEN_PREFIX, "");
                    }
                    Response response = loginApi.verifyToken(token);
                    if(response.getResult().getCode() == ResultCode.HTTP_SUCCESS){
                        Object module = response.getModule();
                        LinkedHashMap loginUser = (LinkedHashMap) module;
                        if(!Objects.isNull(loginUser)){
                            return chain.filter(exchange);
                        }else{
                            result = getReturnMessage(exchange.getResponse(), ResultCode.HTTP_ERROR, "Log in to expire, please log in again.");
                        }
                    }else{
                        result = getReturnMessage(exchange.getResponse(), response.getResult().getCode(), response.getResult().getMessage());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result = getReturnMessage(exchange.getResponse(), ResultCode.HTTP_ERROR, "ERROR");
        }
        return result;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private Mono<Void> getReturnMessage(ServerHttpResponse httpResponse, int code, String msg) {
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject response = new JSONObject();
        JSONObject result = new JSONObject();
        result.put("resultCode", code);
        result.put("message", msg);
        response.put("serverResult", result);
        DataBuffer buffer = httpResponse.bufferFactory().wrap(response.toString().getBytes(StandardCharsets.UTF_8));
        return httpResponse.writeWith(Flux.just(buffer));
    }
}
