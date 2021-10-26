package com.eating.base.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author han bin
 **/
@Data
@NoArgsConstructor
public class Result {
    private int code;
    private String message;

    public Result(ResultBuilder resultBuilder){
        this.code = resultBuilder.code;
        this.message = resultBuilder.message;
    }

    public static class ResultBuilder{
        private int code;
        private String message;

        public ResultBuilder code(int code){
            this.code = code;
            return this;
        }

        public ResultBuilder message(String message){
            this.message = message;
            return this;
        }

        public Result build(){
            return new Result(this);
        }
    }
}
