package com.huangyuanlove.liaoba.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.SharePrefrenceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SuggestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private EditText suggestEdit;
    private RequestQueue requestQueue;
    private SharePrefrenceUtils sharePrefrenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        MyApplication myApplication = (MyApplication) getApplication();
        requestQueue = myApplication.getRequestQueue();
        sharePrefrenceUtils = SharePrefrenceUtils.getInstance(this);
        initView();

    }

    private void initView()
    {
        suggestEdit = (EditText) findViewById(R.id.suggest_edit);
        suggestEdit.setOnClickListener(this);
        submit = (Button) findViewById(R.id.suggest_btn);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.suggest_btn:
                if(TextUtils.isEmpty(suggestEdit.getText().toString().trim()))
                {
                    Toast.makeText(SuggestActivity.this,"请填写你的意见",Toast.LENGTH_SHORT).show();
                    suggestEdit.setText("");
                }
                else
                {
                    sendSuggest();
                }
                break;
        }

    }

    private void sendSuggest()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SUGGEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null || "".equals(response)) {
                    Toast.makeText(SuggestActivity.this, "收到了你的意见，感谢你的反馈", Toast.LENGTH_SHORT).show();
                    SuggestActivity.this.finish();
                } else
                {
                    Toast.makeText(SuggestActivity.this, "网络似乎有点问题，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SuggestActivity.this, "网络似乎有点问题，请稍后再试", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日 hh:mm:ss");
                String time = sdf.format(new Date());
                Map<String,String> map = new HashMap<>();
                map.put("userid",sharePrefrenceUtils.getString("userid"));
                map.put("time",time);
                map.put("content",suggestEdit.getText().toString().trim());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
