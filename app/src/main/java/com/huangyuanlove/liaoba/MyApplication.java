package com.huangyuanlove.liaoba;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by huangyuan on 15-12-18.
 */
public class MyApplication extends Application {
    private RequestQueue requestQueue;
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }
}
