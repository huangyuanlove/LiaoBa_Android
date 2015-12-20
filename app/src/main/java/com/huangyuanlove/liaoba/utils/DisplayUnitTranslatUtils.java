package com.huangyuanlove.liaoba.utils;

import android.content.Context;

/**
 * Created by huangyuan on 15-11-9.
 */
public class DisplayUnitTranslatUtils {

    /**
     * 将px值转化为dip，保证尺寸大小不变
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/fontScale + 0.5f);
    }

    public static int sp2px(Context context ,float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int)(spValue * fontScale + 0.5f);
    }
}
