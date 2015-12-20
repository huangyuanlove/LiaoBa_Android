package com.huangyuanlove.liaoba.utils;

/**
 * Created by huangyuan on 15-12-13.
 */
public class Config {

    public static final int MENU_CHECK_UPDATE = 0;
    public static final int MENU_HIDE_FUNCTION = 1;
    public static final int MENU_SETTING = 2;
    public static final int MENU_ABOUT_ME = 3;

    //图灵机器人接口
    public static final String TURING_ROBOT_BASH_URL = "http://www.tuling123.com/openapi/api?key=28ae93a45250dbd6bfc4e574d945f8b5&info=%s";


    //图灵机器人返回码
    //文本类
    public static final int RESPONSE_TYPE_TEXT = 100000;
    //连接类
    public static final int RESPONSE_TYPE_LINK = 200000;
    //新闻类
    public static final int RESPONSE_TYPE_NEWS = 302000;
    //列车
    public static final int RESPONSE_TYPE_TRAIN = 305000;
    //菜谱
    public static final int RESPONSE_TYPE_COOKBOOK = 308000;
}
