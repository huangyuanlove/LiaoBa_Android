package com.huangyuanlove.liaoba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/21
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        initRippleViewButton();
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.login_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

    }

    private void initRippleViewButton() {

        final EditText username_editText = (EditText) findViewById(R.id.username);
        final EditText password_editText = (EditText) findViewById(R.id.password);

        final CheckBox saveStatus = (CheckBox) findViewById(R.id.saveStatus);

        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPreferencesName), Context.MODE_APPEND);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        username_editText.setText(sharedPreferences.getString("username",""));

        RippleView loginButton = (RippleView) findViewById(R.id.login_button);
        RippleView registerButton = (RippleView) findViewById(R.id.register_button);
        RippleView otherButton = (RippleView) findViewById(R.id.other_button);

        loginButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        registerButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        otherButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(username_editText.getText().toString().trim())) {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    username_editText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "用户名不可为空！", Toast.LENGTH_SHORT).show();

                    return;
                }
                if ("".equals(password_editText.getText().toString().trim())) {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    password_editText.startAnimation(shake);
                    Toast.makeText(LoginActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();

                    return;
                }
                Toast.makeText(LoginActivity.this, "登录中······", Toast.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (saveStatus.isChecked()) {
                            editor.putBoolean("isSaveStatus", true).apply();
                            editor.putString("username", username_editText.getText().toString()).apply();
                        } else {
                            editor.putBoolean("isSaveStatus", false).apply();

                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }, 2000);
                password_editText.setText("");
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && getFragmentManager().getBackStackEntryCount()==1)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
