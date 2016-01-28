package com.huangyuanlove.liaoba.entity;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/16
 */
public class MessageBean {

    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SENT = 1;

    private String content;
    private int type;
    public MessageBean(String content, int type)
    {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }
}
