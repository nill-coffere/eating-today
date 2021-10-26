package com.eating.base.response;

import lombok.Data;

import java.io.Serializable;

import java.util.List;

/**
 * @Author han bin
 **/
@Data
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Page implements Serializable {

    private static final long serialVersion = 2L;

    private Long total;

    private List<?> list;

    public Page(PageBuilder pageBuilder){
        this.total = pageBuilder.total;
        this.list = pageBuilder.list;
    }

    public static class PageBuilder{
        private Long total;
        private List<?> list;

        public PageBuilder total(Long total){
            this.total = total;
            return this;
        }

        public PageBuilder list(List<?> list){
            this.list = list;
            return this;
        }

        public Page build(){
            return new Page(this);
        }
    }

}
