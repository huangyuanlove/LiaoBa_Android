package com.huangyuanlove.liaoba.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.entity.UpdataBean;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.UpdateManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout updatePassword;
    private RelativeLayout suggest;
    private RelativeLayout aboutMe;
    private RelativeLayout update;
    private RequestQueue requestQueue;
    private boolean hasNewVersion = false;
    private UpdataBean updataBean;
    private UpdateManager mUpdateManager;
    private String version ;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        requestQueue = ((MyApplication)getApplication()).getRequestQueue();
        mUpdateManager = new UpdateManager(this);
        version = mUpdateManager.getVersion();

        initData();
        initView();
    }

    private void initData()
    {
        String url = Config.CHEKC_UPDATA_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null && !"".equals(response))
                {
                    try {
                        response = new String(response.getBytes("iso-8859-1"),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    updataBean = gson.fromJson(response,UpdataBean.class);
                    hasNewVersion = true;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updataBean = null;
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("version", version);
                return map;
            }
        };



        requestQueue.add(stringRequest);
    }

    private void initView() {
        updatePassword = (RelativeLayout) findViewById(R.id.set_updata_password);
        updatePassword.setOnClickListener(this);
        suggest = (RelativeLayout) findViewById(R.id.set_suggest);
        suggest.setOnClickListener(this);
        aboutMe = (RelativeLayout) findViewById(R.id.set_about_me);
        aboutMe.setOnClickListener(this);
        update = (RelativeLayout) findViewById(R.id.set_update);
        update.setOnClickListener(this);
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_about_me:
                showPopupWindow();
                break;
            case R.id.set_updata_password:
                startActivity(new Intent(this,ModifyPasswordActivity.class));
                break;
            case R.id.set_suggest:
                startActivity(new Intent(this,SuggestActivity.class));
                break;
            case R.id.set_update:

                if(hasNewVersion) {
                    mUpdateManager.checkUpdateInfo(updataBean.getVersion(), updataBean.getDownLoadUrl(), updataBean.getContent());
                }
                else
                {
                    Toast.makeText(this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    private void showPopupWindow()
    {
        final Intent intent = new Intent();

        intent.setAction("android.intent.action.VIEW");

        dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("关于作者")
                .setMessage("计科1203--黄沅\n指导教师：李盘靖")
                .setPositiveButton("博客", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("http://blog.csdn.net/huangyuan_xuan?viewmode=contents");
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("网站", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("http://www.huangyuanlove.com");
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();

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
