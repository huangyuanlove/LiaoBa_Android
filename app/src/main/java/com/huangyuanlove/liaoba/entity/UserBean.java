package com.huangyuanlove.liaoba.entity;

/**
 * Created by huangyuan on 16-1-28.
 */
public class UserBean {


    private String userid;
    private String password;
    private String uuid;
    private float record;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getRecord() {
        return record;
    }

    public void setRecord(float record) {
        this.record = record;
    }
}
