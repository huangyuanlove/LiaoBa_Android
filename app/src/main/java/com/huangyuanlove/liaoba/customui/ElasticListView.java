package com.huangyuanlove.liaoba.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import com.huangyuanlove.liaoba.utils.DisplayUnitTranslatUtils;

/**
 * Created by huangyuan on 15-12-14.
 */
public class ElasticListView extends ListView {


    public ElasticListView(Context context) {
        super(context);
    }

    public ElasticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElasticListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ElasticListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //maxOverScrollY 控制当listview滑动到底部时回弹的高度，默认为0,修改为自己设置的高度即可
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, DisplayUnitTranslatUtils.px2dip(getContext(),300), isTouchEvent);
    }
}
