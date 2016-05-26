package com.huangyuanlove.liaoba.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.adapter.Five_Five_Adapter;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.SharePrefrenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Five_Five_Activity extends AppCompatActivity {


    private final String TAG = "Five_Five_Activity";
    private int[][] datas = new int[5][5];
    private GridView gridView;
    private Five_Five_Adapter adapter;
    private int flag = 1;
    private TextView timeTextView;
    private Timer timer;
    private int times;
    private Context mContext;
    private RequestQueue requestQueue;
    private SharePrefrenceUtils sharePrefrenceUtils;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                times += 1;
                timeTextView.setText(String.valueOf(times));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five__five);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        timeTextView = (TextView) findViewById(R.id.time);
        timer = new Timer(true);
        mContext = Five_Five_Activity.this;
        sharePrefrenceUtils = SharePrefrenceUtils.getInstance(mContext);
        requestQueue = ((MyApplication) getApplication()).getRequestQueue();
        getSupportActionBar().setTitle("游戏");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("点击小方块会改变它和它上下左右的颜色，将所有小方块的颜色变成其他颜色，you win")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(0);
                            }
                        }, 1000,1000);
                    }
                })
                .create();
        alertDialog.show();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                datas[i][j] = 1;
            }
        }
        gridView = (GridView) findViewById(R.id.five_five_gridView);
        adapter = new Five_Five_Adapter(this, datas);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                int i = position / 5;
                int j = position % 5;
                negative(i, j);
                if (i > 0)
                    negative(i - 1, j);
                if (i < 4)
                    negative(i + 1, j);
                if (j > 0)
                    negative(i, j - 1);
                if (j < 4)
                    negative(i, j + 1);
                adapter.notifyDataSetChanged();
                for (int[] data : datas) {
                    for (int d : data) {
                        if (d == flag) {
                            return;
                        }
                    }
                }
                flag = -flag;
                Toast.makeText(getApplicationContext(), "you win", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setCancelable(true)
                        .setTitle("完成")
                        .setCancelable(true)
                        .setMessage("YOU WIN!!\n是否上传通关时间？")
                        .setNegativeButton("否",null)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.cancel();
                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.POST, Config.UPDATE_RECORD_URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("uuid",sharePrefrenceUtils.getString("uuid") );
                                        map.put("record",String.valueOf(times));
                                        return map;
                                    }
                                };
                            requestQueue.add(stringRequest);
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    public void negative(int i, int j) {
        datas[i][j] = -datas[i][j];
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.see_record:
                mContext.startActivity(new Intent(mContext,ShowRecordActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
