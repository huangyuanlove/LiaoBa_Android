package com.huangyuanlove.liaoba.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.gson.Gson;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;
import com.huangyuanlove.liaoba.entity.UserBean;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.SharePrefrenceUtils;

import java.util.HashMap;
import java.util.Map;

public class ModifyPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private RippleView submitButton;
    private RippleView resetButton;
    private EditText oldPassword;
    private EditText newPaddword;
    private EditText confirmPassword;
    private Animation shake;
    private RequestQueue requestQueue;
    private SharePrefrenceUtils sharePrefrenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        requestQueue = ((MyApplication)getApplication()).getRequestQueue();
        sharePrefrenceUtils = SharePrefrenceUtils.getInstance(this);
        initView();
    }

    private void initView() {
        shake = AnimationUtils.loadAnimation(ModifyPasswordActivity.this, R.anim.shake);
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.modify_password_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        submitButton = (RippleView) findViewById(R.id.modify_password_confirmbutton);
        submitButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        submitButton.setOnClickListener(this);
        resetButton = (RippleView) findViewById(R.id.modify_password_resetbutton);
        resetButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        resetButton.setOnClickListener(this);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPaddword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

        getSupportActionBar().setTitle("修改密码");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.modify_password_confirmbutton:
                if("".equals(oldPassword.getText().toString().trim()))
                {
                    oldPassword.startAnimation(shake);
                    Toast.makeText(this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if("".equals(oldPassword.getText().toString().trim()))
                {
                    oldPassword.startAnimation(shake);
                    Toast.makeText(this, "新密码不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if("".equals(confirmPassword.getText().toString().trim()))
                {
                    confirmPassword.startAnimation(shake);
                    Toast.makeText(this,"确认密码不可为空",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(!newPaddword.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
                {
                    confirmPassword.startAnimation(shake);
                    Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return ;
                }

        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , Config.MODIFYPASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        if(response != null && !"".equals(response.trim()))
                        {
                            UserBean user = gson.fromJson(response, UserBean.class);
                            if (user != null) {
                                sharePrefrenceUtils.setString("userid", user.getUserid());
                                sharePrefrenceUtils.setFloat("record", user.getRecord());
                                sharePrefrenceUtils.setString("uuid", user.getUuid());
                                if (sharePrefrenceUtils.getBoolean("isSaveStatus", false)) {
                                    sharePrefrenceUtils.setString("password", user.getPassword());
                                }
                                Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                ModifyPasswordActivity.this.finish();
                            }
                        }
                        else
                        {
                            oldPassword.startAnimation(shake);
                            Toast.makeText(ModifyPasswordActivity.this, "密码不正确！", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ModifyPasswordActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String userid = sharePrefrenceUtils.getString("userid");
                String uuid=sharePrefrenceUtils.getString("uuid");
                float record=sharePrefrenceUtils.getFloat("record");
                Map<String,String> map = new HashMap<>();
                map.put("userid",userid);
                map.put("oldPassword",oldPassword.getText().toString().trim());
                map.put("newPassword",newPaddword.getText().toString().trim());
                map.put("uuid",uuid);
                map.put("record",record+"");
                return  map;
            }
        };

                requestQueue.add(stringRequest);
                break;
            case R.id.modify_password_resetbutton:
                oldPassword.setText("");
                newPaddword.setText("");
                confirmPassword.setText("");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
