package com.huangyuanlove.liaoba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.huangyuanlove.liaoba.customui.SecretTextView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/21
 */
public class WelcomeActivity extends Activity {
    SecretTextView secretTextView;
    static final int START_ACTIVITY = 1;
    static final int START_ANIMOTION = 2;
    private boolean isSaveStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_activity);

        secretTextView = (SecretTextView) findViewById(R.id.secretTextView);
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.titanic_textView);

        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPreferencesName), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        isSaveStatus = sharedPreferences.getBoolean("isSaveStatus", false);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = START_ANIMOTION;
                mHandler.sendMessage(msg);
            }
        }, 1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = START_ACTIVITY;
                mHandler.sendMessage(msg);
            }
        }, 4000);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_ANIMOTION) {

                secretTextView.setmDuration(3000);
                secretTextView.setIsVisible(false);
                secretTextView.toggle();
            }
            if (msg.what == START_ACTIVITY) {

                Intent intent = null;
                if (isSaveStatus) {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);

                } else {
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }
    };

}
