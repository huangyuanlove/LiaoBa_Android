package com.huangyuanlove.liaoba;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;

public class ModifyPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private RippleView submitButton;
    private RippleView resetButton;
    private EditText oldPassword;
    private EditText newPaddword;
    private EditText confirmPassword;
    private Animation shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        getSupportActionBar().hide();
        initView();
    }

    private void initView() {



        shake = AnimationUtils.loadAnimation(ModifyPasswordActivity.this, R.anim.shake);
        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.modify_password_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        submitButton = (RippleView) findViewById(R.id.modify_password_confirmbutton);
        submitButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        submitButton.setOnClickListener(this);
        resetButton = (RippleView) findViewById(R.id.modify_password_resetbutton);
        resetButton.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        resetButton.setOnClickListener(this);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPaddword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.modify_password_confirmbutton:
                if("".equals(oldPassword.getText().toString().trim()))
                {
                    oldPassword.startAnimation(shake);
                    Toast.makeText(this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if("".equals(oldPassword.getText().toString().trim()))
                {
                    oldPassword.startAnimation(shake);
                    Toast.makeText(this, "新密码不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if("".equals(confirmPassword.getText().toString().trim()))
                {
                    confirmPassword.startAnimation(shake);
                    Toast.makeText(this,"确认密码不可为空",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(!newPaddword.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
                {
                    confirmPassword.startAnimation(shake);
                    Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return ;
                }
                break;
            case R.id.modify_password_resetbutton:
                oldPassword.setText("");
                newPaddword.setText("");
                confirmPassword.setText("");
                break;
        }
    }
}
