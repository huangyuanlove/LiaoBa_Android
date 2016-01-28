package com.huangyuanlove.liaoba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;
import com.huangyuanlove.liaoba.entity.UserBean;
import com.huangyuanlove.liaoba.utils.ActivityCollector;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.SharePrefrenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/21
 */
public class LoginActivity extends BaseActivity {
    private double mExitTime;
    private RequestQueue requestQueue;
    private EditText username_editText;
    private EditText password_editText;
    private CheckBox saveStatus;
    private SharePrefrenceUtils sharePrefrenceUtils;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        sharePrefrenceUtils = SharePrefrenceUtils.getInstance(LoginActivity.this);
        requestQueue = ((MyApplication) getApplication()).getRequestQueue();
        initRippleViewButton();
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.login_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

    }

    private void initRippleViewButton() {

        username_editText = (EditText) findViewById(R.id.username);
        password_editText = (EditText) findViewById(R.id.password);

        saveStatus = (CheckBox) findViewById(R.id.saveStatus);


        username_editText.setText(sharePrefrenceUtils.getString("username"));

        RippleView loginButton = (RippleView) findViewById(R.id.login_button);
        RippleView registerButton = (RippleView) findViewById(R.id.register_button);

        loginButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        registerButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(username_editText.getText().toString().trim())) {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    username_editText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "用户名不可为空！", Toast.LENGTH_SHORT).show();

                    return;
                }
                if ("".equals(password_editText.getText().toString().trim())) {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    password_editText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (saveStatus.isChecked()) {
                    sharePrefrenceUtils.setString("username", username_editText.getText().toString());
                    sharePrefrenceUtils.setBoolean("isSaveStatus", true);
                } else {
                    sharePrefrenceUtils.setBoolean("isSaveStatus", false);
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !"".equals(response.trim())) {

                            UserBean user = gson.fromJson(response, UserBean.class);
                            sharePrefrenceUtils.setString("userid", user.getUserid());
                            sharePrefrenceUtils.setString("record", user.getRecord());
                            sharePrefrenceUtils.setString("uuid", user.getUUID());
                            if(saveStatus.isChecked())
                            {
                                sharePrefrenceUtils.setString("password",user.getPassword());
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LoginActivity", error.toString() + "-----------");
                        Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
//                            username=huangyuan&userPassword=amw
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put("userid", username_editText.getText().toString().trim());
                        map.put("password", password_editText.getText().toString().trim());
                        return map;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
