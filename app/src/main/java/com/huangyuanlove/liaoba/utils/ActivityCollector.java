package com.huangyuanlove.liaoba.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/24
 */
public class ActivityCollector {
    public static List<Activity> activityList = new ArrayList<>();
    public static void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity)
    {
        activityList.remove(activity);
    }


    public static void finishAll(){
        for(Activity activity : activityList)
        {
            if(!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }
}
