package com.huangyuanlove.liaoba.entity;

/**
 * Created by huangyuan on 16-1-28.
 */
public class UserBean {


    private String userid;
    private String password;
    private String uuid;
    private String record;


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

    public void setUuid(String UUID) {
        this.uuid = UUID;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
