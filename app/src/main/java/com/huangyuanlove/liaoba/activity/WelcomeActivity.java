package com.huangyuanlove.liaoba.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.customui.SecretTextView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.PermissionHelper;
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
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_activity);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                            Toast.makeText(WelcomeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                WelcomeActivity.this.finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i("huangyuan", "All of requested permissions has been granted, so run app logic.");
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d("huangyuan", "The api level of system is lower than 23, so run app logic directly.");
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d("huangyuan", "All of requested permissions has been granted, so run app logic directly.");
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i("huangyuan", "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

}
