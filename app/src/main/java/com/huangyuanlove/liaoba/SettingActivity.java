package com.huangyuanlove.liaoba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.huangyuanlove.liaoba.customui.indris.material.RippleView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout updatePassword;
    private RelativeLayout suggest;
    private RelativeLayout aboutMe;
    private PopupWindow popupWindow;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private RippleView submitButton;
    private RippleView resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        initView();
    }



    private void initView() {
        updatePassword = (RelativeLayout) findViewById(R.id.set_updata_password);
        updatePassword.setOnClickListener(this);
        suggest = (RelativeLayout) findViewById(R.id.set_suggest);
        suggest.setOnClickListener(this);
        aboutMe = (RelativeLayout) findViewById(R.id.set_about_me);
        aboutMe.setOnClickListener(this);

        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_about_me:
                break;
            case R.id.set_updata_password:
                startActivity(new Intent(this,ModifyPasswordActivity.class));
                break;
            case R.id.set_suggest:
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
