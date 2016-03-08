package com.huangyuanlove.liaoba.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;
import com.huangyuanlove.liaoba.utils.Config;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private EditText username_editText;
    private EditText password_editText;
    private EditText confirmPassword_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.register_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);
        requestQueue = ((MyApplication)getApplication()).getRequestQueue();
        initRegisterView();

    }

    private void initRegisterView() {
        final Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        username_editText = (EditText) findViewById(R.id.register_username);
        password_editText = (EditText) findViewById(R.id.register_password);
        confirmPassword_editText = (EditText) findViewById(R.id.register_confirmpassword);

        RippleView confirm_button = (RippleView) findViewById(R.id.register_confirmbutton);
        RippleView reset_button = (RippleView) findViewById(R.id.register_resetbutton);

        confirm_button.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        reset_button.setRippleColor(Color.rgb(121, 121, 121), 1.0f);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_editText.setText("");
                password_editText.setText("");
                confirmPassword_editText.setText("");
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(username_editText.getText().toString().trim()))
                {
                    username_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this, "用户名不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if("".equals(password_editText.getText().toString().trim()))
                {
                    password_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if("".equals(confirmPassword_editText.getText().toString().trim()))
                {
                    confirmPassword_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this,"确认密码不可为空",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(!password_editText.getText().toString().trim().equals(confirmPassword_editText.getText().toString().trim()))
                {
                    confirmPassword_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return ;
                }


                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Config.REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response!= null && !"".equals(response.trim()))
                                {
                                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    RegisterActivity.this.finish();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"帐号已存在",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String userid = username_editText.getText().toString();
                        String password = password_editText.getText().toString();
                        Map<String,String> map = new HashMap<>(2);
                        map.put("userid",userid);
                        map.put("password",password);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });

    }
}
