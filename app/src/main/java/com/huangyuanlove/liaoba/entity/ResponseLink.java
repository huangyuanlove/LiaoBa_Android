package com.huangyuanlove.liaoba.entity;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseLink {


    private int code;
    private String text;
    private String url;

    public ResponseLink() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
