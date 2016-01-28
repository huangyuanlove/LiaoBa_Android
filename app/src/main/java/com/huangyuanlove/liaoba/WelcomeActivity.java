package com.huangyuanlove.liaoba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.customui.SecretTextView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.SharePrefrenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/21
 */
public class WelcomeActivity extends Activity {
    SecretTextView secretTextView;
    static final int START_ACTIVITY = 1;
    static final int START_ANIMOTION = 2;
    private RequestQueue requestQueue;
    private boolean isSaveStatus;
    private SharePrefrenceUtils sharePrefrenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_activity);
        sharePrefrenceUtils = SharePrefrenceUtils.getInstance(WelcomeActivity.this);
        secretTextView = (SecretTextView) findViewById(R.id.secretTextView);
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.titanic_textView);
        requestQueue = ((MyApplication) getApplication()).getRequestQueue();
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        isSaveStatus = sharePrefrenceUtils.getBoolean("isSaveStatus", false);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = START_ANIMOTION;
                mHandler.sendMessage(msg);
            }
        }, 1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = START_ACTIVITY;
                mHandler.sendMessage(msg);
            }
        }, 4000);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_ANIMOTION) {

                secretTextView.setmDuration(3000);
                secretTextView.setIsVisible(false);
                secretTextView.toggle();
            }
            if (msg.what == START_ACTIVITY) {

                if (isSaveStatus) {

                    final String userid = sharePrefrenceUtils.getString("");
                    final String password = sharePrefrenceUtils.getString("");

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !"".equals(response.trim())) {
                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            } else {
                                Toast.makeText(WelcomeActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
//                            username=huangyuan&userPassword=amw
                        protected Map<String, String> getParams() {
                            Map<String, String> map = new HashMap<>();
                            map.put("userid", userid);
                            map.put("password", password);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);


                } else {
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                WelcomeActivity.this.finish();
            }
        }
    };

}
