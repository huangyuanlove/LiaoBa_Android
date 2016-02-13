package com.huangyuanlove.liaoba.utils;

/**
 * Created by huangyuan on 15-12-13.
 */
public class Config {

    //用于音乐播放的常量
    public static final String ACTION_PROGRESS = "pub.huangyuan.broadcast.progress";
    public static final String ACTION_SEEKTO = "pub.huangyuan.broadcast.seekto";
    public static final String EXSTRA_PROGRESS_MAX = "prg_max";
    public static final String EXSTRA_PROGRESS_CUR = "prg_current";
    //存放boolean，true ： 播放新文件，  false： 播放与暂停
    public static final String EXSTRA_CHANGE="change";
    //新文件路径
    public static final String EXSTRA_PATH="path";


    //用于隐藏菜单的常量
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

    

    //登录接口
    public static final String LOGIN_URL = "http://192.168.99.108:8080/LiaoBa/login.do";
    //注册接口
    public static final String REGISTER_URL = "http://192.168.99.108:8080/LiaoBa/regiser.do";
    //修改密码接口
    public static final String MODIFYPASSWORD_URL = "http://192.168.99.108:8080/LiaoBa/modifypassword.do";
    //聊天记录接口
    public static final String CHATLOG_URL = "http://192.168.99.108:8080/LiaoBa/chatlog.do";
}
