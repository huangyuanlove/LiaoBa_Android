package com.huangyuanlove.liaoba;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.huangyuanlove.liaoba.customui.indris.material.RippleView;
import com.huangyuanlove.liaoba.customui.titanic.Titanic;
import com.huangyuanlove.liaoba.customui.titanic.TitanicTextView;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TitanicTextView titanicTextView = (TitanicTextView) findViewById(R.id.register_titanic_textView);
        Titanic titanic = new Titanic();
        titanic.start(titanicTextView);

        initRegisterView();

    }

    private void initRegisterView() {
        final Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        final EditText username_editText = (EditText) findViewById(R.id.register_username);
        final EditText password_editText = (EditText) findViewById(R.id.register_password);
        final EditText confirmPassword_editText = (EditText) findViewById(R.id.register_confirmpassword);

        RippleView confirm_button = (RippleView) findViewById(R.id.register_confirmbutton);
        RippleView reset_button = (RippleView) findViewById(R.id.register_resetbutton);

        confirm_button.setRippleColor(Color.rgb(121, 121, 121), 1.0f);
        reset_button.setRippleColor(Color.rgb(121, 121, 121), 1.0f);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_editText.setText("");
                password_editText.setText("");
                confirmPassword_editText.setText("");
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(username_editText.getText().toString().trim()))
                {
                    username_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this, "用户名不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if("".equals(password_editText.getText().toString().trim()))
                {
                    password_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this, "密码不可为空！", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if("".equals(confirmPassword_editText.getText().toString().trim()))
                {
                    confirmPassword_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this,"确认密码不可为空",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(!password_editText.getText().toString().trim().equals(confirmPassword_editText.getText().toString().trim()))
                {
                    confirmPassword_editText.startAnimation(shake);
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return ;
                }


            }
        });

    }
}
