package com.huangyuanlove.liaoba.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.entity.UserBean;
import com.huangyuanlove.liaoba.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowRecordActivity extends Activity {

    private RequestQueue requestQueue;

    private ListView listView;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);
        listView = (ListView) findViewById(R.id.show_record_list);
        requestQueue = ((MyApplication) getApplication()).getRequestQueue();
        initData();
    }

    private void initData() {
        String url = Config.SHOW_RECORD_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        Gson gson = new Gson();
                        List<UserBean> users = gson.fromJson(response, new TypeToken<List<UserBean>>() {
                        }.getType());
                        for (UserBean user : users) {
                            Map<String, String> map = new HashMap<>();
                            map.put("userid", user.getUserid());
                            map.put("record", user.getRecord() + "");
                            datas.add(map);
                        }

                        adapter = new SimpleAdapter(ShowRecordActivity.this, datas, R.layout.item_record_list, new String[]{"userid", "record"}, new int[]{R.id.item_record_user_id, R.id.item_record_user_record});
                        listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        requestQueue.add(stringRequest);
    }
}
