package com.huangyuanlove.liaoba;

import android.app.Activity;
import android.os.Bundle;

import com.huangyuanlove.liaoba.utils.ActivityCollector;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/24
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }




}
