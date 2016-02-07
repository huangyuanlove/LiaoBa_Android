package com.huangyuanlove.liaoba;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huangyuanlove.liaoba.adapter.MessageAdapter;
import com.huangyuanlove.liaoba.entity.MessageBean;
import com.huangyuanlove.liaoba.entity.ResponseLinkBean;
import com.huangyuanlove.liaoba.entity.ResponseTrainBean;
import com.huangyuanlove.liaoba.utils.Config;
import com.huangyuanlove.liaoba.utils.GsonTool;
import com.mobeta.android.dslv.DragSortListView;

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

    private DragSortListView messageListView;
    private EditText inputText;
    private MessageAdapter messageAdapter;
    private List<MessageBean> messageList = new ArrayList<>();
    private RequestQueue requestQueue;
    private int currentResponseCode;
    private Button sendMsg;
    private ClipboardManager clipboardManager;
    private ActionBar actionBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PopupMenu pm;
    private Menu menu;
    public interface ChatFragmentCallBack {
        void transe(EditText editText);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, null);
        inputText = (EditText) view.findViewById(R.id.input_msg);
        TextView emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        MyApplication myApplication = (MyApplication) getActivity().getApplication();

        requestQueue = myApplication.getRequestQueue();
        sendMsg = (Button) view.findViewById(R.id.send_msg_button);
        ((ChatFragmentCallBack) getActivity()).transe(inputText);


        messageListView = (DragSortListView) view.findViewById(R.id.msg_listView);
        messageListView.setEmptyView(emptyTextView);
        messageAdapter = new MessageAdapter(getActivity(), R.layout.item_msg, messageList);
        messageListView.setAdapter(messageAdapter);

        messageListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int i, int i1) {
                MessageBean temp = messageList.get(i);
                messageList.remove(i);
                messageList.add(i1, temp);
                messageAdapter.notifyDataSetChanged();
            }
        });
        messageListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int i) {
                messageList.remove(i);
                messageAdapter.notifyDataSetChanged();
            }
        });


        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final MessageBean message = (MessageBean) parent.getItemAtPosition(position);
                pm = new PopupMenu(getActivity(),view);
                menu = pm.getMenu();
                getActivity().getMenuInflater().inflate(R.menu.msg_popup_menu,menu);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.options_menu_copy:
                                clipboardManager.setText(message.getContent());
                                Toast.makeText(getActivity(), message.getContent(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.options_menu_delete:
                                messageList.remove(position);
                                messageAdapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                pm.show();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = inputText.getText().toString().trim();
                if (!"".equals(content)) {
                    sharedPreferences = ((MyApplication) getActivity().getApplication()).getSharedPreferences();
                    editor = sharedPreferences.edit();
                    if (content.contains("游戏") || content.contains("挑战")) {
                        editor.putBoolean("game", true).apply();
                        Toast.makeText(getActivity(), "恭喜解锁隐藏功能，请到隐藏属性菜单查看", Toast.LENGTH_SHORT).show();
                    }

                    if (content.contains("地图")) {
                        editor.putBoolean("map", true).commit();
                        Toast.makeText(getActivity(), "恭喜解锁隐藏功能，请到隐藏属性菜单查看", Toast.LENGTH_SHORT).show();
                    }
                    if (content.contains("音乐")) {
                        editor.putBoolean("music", true).commit();
                        Toast.makeText(getActivity(), "恭喜解锁隐藏功能，请到隐藏属性菜单查看", Toast.LENGTH_SHORT).show();
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
                                            ResponseLinkBean responseLink = GsonTool.getObj(response, ResponseLinkBean.class);
                                            response = responseLink.getText() + "\n已经帮您自动打开，网址如下：\n" + responseLink.getUrl();
                                            ResponseURLViewActivity.actionStart(getActivity(), responseLink.getUrl());
                                        } else if (currentResponseCode == 305000) {
                                            ResponseTrainBean responseTrain = GsonTool.getObj(response, ResponseTrainBean.class);
                                            List<ResponseTrainBean.ListEntity> trainList = responseTrain.getList();
                                            for (ResponseTrainBean.ListEntity train : trainList) {
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
                                    MessageBean message = new MessageBean(response, MessageBean.MSG_TYPE_RECEIVED);
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
                    MessageBean message = new MessageBean(content, MessageBean.MSG_TYPE_SENT);
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
