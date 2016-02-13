package com.huangyuanlove.liaoba.entity;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseTextBean {


    private int code;
    private String text;

    public ResponseTextBean() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
