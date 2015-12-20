package com.huangyuanlove.liaoba.entity;

import java.util.List;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseCookbook {



    private int code;
    private String text;
    private List<ListEntity> list;

    public void setCode(int code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {

        private String name;
        private String icon;
        private String info;
        private String detailurl;

        public void setName(String name) {
            this.name = name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

        public String getInfo() {
            return info;
        }

        public String getDetailurl() {
            return detailurl;
        }
    }
}
