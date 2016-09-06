package com.itheima.mobilesafe.utils;

import android.app.Application;
import android.content.Context;

/**
 * 全局Context
 * Created by billow on 2016/9/6.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
