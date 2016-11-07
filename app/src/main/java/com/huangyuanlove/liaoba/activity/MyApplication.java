package com.huangyuanlove.liaoba.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by huangyuan on 15-12-18.
 */
public class MyApplication extends Application {
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900025783", false);
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    public SharedPreferences getSharedPreferences()
    {
        if (sharedPreferences== null)
        {
            sharedPreferences = getSharedPreferences("hide_function", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


}
