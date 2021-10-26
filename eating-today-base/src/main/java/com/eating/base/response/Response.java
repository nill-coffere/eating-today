package com.eating.base.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author han bin
 **/
@Data
@NoArgsConstructor
public class Response implements Serializable {

    private static final long serialVersion = 1L;

    private Object module;

    private Page page;

    private Result result;

    private String token;

    private String session;

    public Response(ResponseBuilder responseBuilder){
        this.module = responseBuilder.module;
        this.page = responseBuilder.page;
        this.result = responseBuilder.result;
        this.token = responseBuilder.token;
        this.session = responseBuilder.session;
    }

    public static class ResponseBuilder{
        private Object module;

        private Page page;

        private Result result;

        private String token;

        private String session;

        public ResponseBuilder module(Object module){
            this.module = module;
            return this;
        }

        public ResponseBuilder page(Page page){
            this.page = page;
            return this;
        }

        public ResponseBuilder result(Result result, Boolean status){
            if (!Objects.isNull(status) && status) {
                this.result = new Result.ResultBuilder().code(ResultCode.HTTP_SUCCESS).message("Success").build();
            }else if(!Objects.isNull(status) && !status){
                this.result = new Result.ResultBuilder().code(ResultCode.HTTP_ERROR).message("Fail").build();
            }else{
                this.result = result;
            }
            return this;
        }

        public ResponseBuilder token(String token){
            this.token = token;
            return this;
        }

        public ResponseBuilder session(String session){
            this.session = session;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

}
