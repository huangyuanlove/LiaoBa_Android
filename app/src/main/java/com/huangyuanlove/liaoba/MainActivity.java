package com.huangyuanlove.liaoba;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener,ChatFragment.ChatFragmentCallBack {

    private ResideMenu resideMenu;
    private double mExitTime;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenuFragment();
        initResideMenu();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.chat_fragment, new ChatFragment())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
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
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {

        switch ((int)v.getTag())
        {
            case Config.MENU_CHECK_UPDATE:

                Toast.makeText(this,"注销",Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
                Toast.makeText(this,"还没有开启隐藏功能",Toast.LENGTH_SHORT).show();
                break;
            case Config.MENU_ABOUT_ME:

                Toast.makeText(this,"huangyuan_xuan",Toast.LENGTH_SHORT).show();

                break;
            case Config.MENU_SETTING:
                Toast.makeText(this,"暂无权限进行设置",Toast.LENGTH_SHORT).show();
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

    private void initResideMenu()
    {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = { "注销", "隐藏属性", "软件设置", "关于作者" };
        int icon[] = { R.drawable.logout,
                R.drawable.hide_function,
                R.drawable.setting,
                R.drawable.about_me };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(this);
            item.setTag(i);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position)
        {
            case 1:
                editText.setText("随便说点什么");
                break;
            case 2:
                editText.setText("北京天气");
                break;
            case 3:
                editText.setText("XX快递 快递单号");
                break;
            case 4:
                editText.setText("讲个笑话");
                break;
            case 5:
                editText.setText("明天北京到拉萨的飞机");
                break;
            case 6:
                editText.setText("3的27次方");
                break;
            case 7:
                editText.setText("北京到青岛的火车");
                break;
            case 8:
                editText.setText("美女图片");
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }

    @Override
    public void transe(EditText editText) {
        this.editText = editText;
    }
}
