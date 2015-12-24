package com.huangyuanlove.liaoba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.utils.Config;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener, ChatFragment.ChatFragmentCallBack {

    private ResideMenu mResideMenu;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager mFragmentManager;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenuFragment();
        initResideMenu();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.chat_fragment, new ChatFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mResideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_item:
                if (mFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(mFragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch ((int) v.getTag()) {
            case Config.MENU_CHECK_UPDATE:

                final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("确认注销？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                                MainActivity.this.finish();

                            }
                        })
                        .create();
                alertDialog.show();
                break;
            case Config.MENU_HIDE_FUNCTION:
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("hide_function", Context.MODE_PRIVATE);
                View hideFunctionView = LayoutInflater.from(this).inflate(R.layout.hide_function, null);
                RippleView gameButton = (RippleView) hideFunctionView.findViewById(R.id.hide_function_game);
                RippleView mapButton = (RippleView) hideFunctionView.findViewById(R.id.hide_function_map);
                RippleView musicButton = (RippleView) hideFunctionView.findViewById(R.id.hide_function_music);

                Map<String, ?> hideFunction = sharedPreferences.getAll();
                if (hideFunction.isEmpty()) {
                    Toast.makeText(this, "还没有开启隐藏功能", Toast.LENGTH_SHORT).show();
                    return ;
                } else {
                    //TODO 完成地图和音乐界面后修改此处
                    if (hideFunction.containsKey("game")) {
                        initHideFunctionView(gameButton, Five_Five_Activity.class);
                    }
                    if (hideFunction.containsKey("map")) {
                        initHideFunctionView(mapButton, BaiduMapActivity.class);
                    }
                    if (hideFunction.containsKey("music")) {
                        initHideFunctionView(musicButton, PlayMusicActivity.class);
                    }

                }
                new AlertDialog.Builder(this)
                        .setView(hideFunctionView)
                        .create()
                        .show();
                break;
            case Config.MENU_ABOUT_ME:

                Toast.makeText(this, "huangyuan_xuan", Toast.LENGTH_SHORT).show();

                break;
            case Config.MENU_SETTING:
                Toast.makeText(this, "暂无权限进行设置", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private List<MenuObject> getMenuObjects() {


        List<MenuObject> menuObjects = new ArrayList<>(9);

        MenuObject close = new MenuObject();
        close.setResource(android.R.drawable.ic_menu_close_clear_cancel);
        close.setBgColor(Color.GRAY);

        MenuObject chat = new MenuObject("聊天");
        chat.setResource(R.drawable.menu_chat);
        chat.setBgColor(Color.GRAY);

        MenuObject weather = new MenuObject("天气");
        weather.setResource(R.drawable.menu_weather);
        weather.setBgColor(Color.GRAY);

        MenuObject express = new MenuObject("快递");
        express.setResource(R.drawable.menu_express);
        express.setBgColor(Color.GRAY);

        MenuObject joke = new MenuObject("笑话");
        joke.setResource(R.drawable.menu_joke);
        joke.setBgColor(Color.GRAY);

        MenuObject plan = new MenuObject("航班");
        plan.setResource(R.drawable.menu_plane);
        plan.setBgColor(Color.GRAY);

        MenuObject calculate = new MenuObject("计算");
        calculate.setResource(R.drawable.menu_calculate);
        calculate.setBgColor(Color.GRAY);

        MenuObject train = new MenuObject("列车");
        train.setResource(R.drawable.menu_train);
        train.setBgColor(Color.GRAY);


        MenuObject search = new MenuObject("搜索");
        search.setResource(R.drawable.menu_search);
        search.setBgColor(Color.GRAY);

        menuObjects.add(close);
        menuObjects.add(chat);
        menuObjects.add(weather);
        menuObjects.add(express);
        menuObjects.add(joke);
        menuObjects.add(plan);
        menuObjects.add(calculate);
        menuObjects.add(train);
        menuObjects.add(search);

        return menuObjects;
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(200);
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private void initResideMenu() {
        // attach to current activity;
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.menu_background);
        mResideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = {"注销", "隐藏属性", "软件设置", "关于作者"};
        int icon[] = {R.drawable.logout,
                R.drawable.hide_function,
                R.drawable.setting,
                R.drawable.about_me};

        for (int i = 0; i < titles.length; i++) {
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(this);
            item.setTag(i);
            mResideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                mEditText.setText("随便说点什么");
                break;
            case 2:
                mEditText.setText("北京天气");
                break;
            case 3:
                mEditText.setText("XX快递 快递单号");
                break;
            case 4:
                mEditText.setText("讲个笑话");
                break;
            case 5:
                mEditText.setText("明天北京到拉萨的飞机");
                break;
            case 6:
                mEditText.setText("3的27次方");
                break;
            case 7:
                mEditText.setText("北京到青岛的火车");
                break;
            case 8:
                mEditText.setText("美女图片");
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }

    @Override
    public void transe(EditText editText) {
        this.mEditText = editText;
    }

    private void initHideFunctionView(RippleView rippleView, final Class clazz) {
        rippleView.setVisibility(View.VISIBLE);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, clazz));

            }
        });
    }
}
