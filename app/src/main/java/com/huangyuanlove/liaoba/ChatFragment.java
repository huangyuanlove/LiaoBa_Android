package com.huangyuanlove.liaoba;


import android.animation.Animator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.adapter.MessageAdapter;
import com.huangyuanlove.liaoba.entity.Message;
import com.huangyuanlove.liaoba.entity.ResponseLink;
import com.huangyuanlove.liaoba.entity.ResponseTrain;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.DisplayUnitTranslatUtils;
import com.huangyuanlove.liaoba.utils.GsonTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ChatFragment extends Fragment {

    private ListView messageListView;
    private EditText inputText;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private RequestQueue requestQueue;
    private int currentResponseCode;
    private Button sendMsg;

    private ActionBar actionBar;
    private float mFirst;
    private float mCurrentY;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public interface ChatFragmentCallBack {
        void transe(EditText editText);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, null);
        inputText = (EditText) view.findViewById(R.id.input_msg);
        TextView emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        //获取系统认为最低的滑动距离，也就是说超过这个距离的移动，系统就认为是滑动状态了
        final int mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        MyApplication myApplication = (MyApplication) getActivity().getApplication();

        requestQueue = myApplication.getRequestQueue();
        sendMsg = (Button) view.findViewById(R.id.send_msg_button);
        ((ChatFragmentCallBack) getActivity()).transe(inputText);


        messageListView = (ListView) view.findViewById(R.id.msg_listView);
        messageListView.setEmptyView(emptyTextView);
        messageAdapter = new MessageAdapter(getActivity(), R.layout.item_msg, messageList);
        messageListView.setAdapter(messageAdapter);


        //设置事件监听
        messageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirst = DisplayUnitTranslatUtils.px2dip(getActivity(), event.getY());

                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = DisplayUnitTranslatUtils.px2dip(getActivity(), event.getY());

                        if (mCurrentY - mFirst > mTouchSlop) {

                            actionBar.show();
                        } else if (mFirst - mCurrentY > mTouchSlop) {

                            actionBar.hide();
                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });


        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = inputText.getText().toString().trim();
                if (!"".equals(content))
                {
                    sharedPreferences = ((MyApplication) getActivity().getApplication()).getSharedPreferences();
                    editor = sharedPreferences.edit();
                    if (content.contains("游戏") || content.contains("挑战")) {
                        editor.putBoolean("game",true).apply();
                        Toast.makeText(getActivity(),"恭喜解锁隐藏功能，请到隐藏属性菜单查看",Toast.LENGTH_SHORT).show();
                    }

                    if (content.contains("地图")) {
                        editor.putBoolean("map",true).commit();
                        Toast.makeText(getActivity(),"恭喜解锁隐藏功能，请到隐藏属性菜单查看",Toast.LENGTH_SHORT).show();
                    }
                    if (content.contains("音乐")) {
                        editor.putBoolean("music",true).commit();
                        Toast.makeText(getActivity(),"恭喜解锁隐藏功能，请到隐藏属性菜单查看",Toast.LENGTH_SHORT).show();
                    }

                    String info = content;
                    try {
                        info = URLEncoder.encode(content, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            String.format(Config.TURING_ROBOT_BASH_URL, info),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    currentResponseCode = 0;

                                    try {
                                        currentResponseCode = new JSONObject(response).getInt("code");
                                        if (currentResponseCode == 0)
                                            response = "不能理解你说的话";
                                        else if (currentResponseCode == 200000) {
                                            ResponseLink responseLink = GsonTool.getObj(response, ResponseLink.class);
                                            response = responseLink.getText() + "\n已经帮您自动打开，网址如下：\n" + responseLink.getUrl();
                                            ResponseURLView.actionStart(getActivity(), responseLink.getUrl());
                                        } else if (currentResponseCode == 305000) {
                                            ResponseTrain responseTrain = GsonTool.getObj(response, ResponseTrain.class);
                                            List<ResponseTrain.ListEntity> trainList = responseTrain.getList();
                                            for (ResponseTrain.ListEntity train : trainList) {
                                                response += train.getTrainnum() + "\n"
                                                        + train.getStart() + "----->" + train.getTerminal() + "\n"
                                                        + train.getStarttime() + "----->" + train.getEndtime() + "\n\n";
                                            }
                                            response = response.substring(response.lastIndexOf("}") + 1);
                                        } else
                                            response = new JSONObject(response).getString("text");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Message message = new Message(response, Message.MSG_TYPE_RECEIVED);
                                    messageList.add(message);
                                    messageAdapter.notifyDataSetChanged();
                                    messageListView.setSelection(messageList.size());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT).show();
                                }
                            });
                    requestQueue.add(stringRequest);
                    Message message = new Message(content, Message.MSG_TYPE_SENT);
                    messageList.add(message);
                    messageAdapter.notifyDataSetChanged();
                    messageListView.setSelection(messageList.size());
                    inputText.setText("");
                }
            }
        });

        return view;
    }


}
