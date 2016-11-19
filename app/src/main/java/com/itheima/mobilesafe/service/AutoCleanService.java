package com.itheima.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.itheima.mobilesafe.receiver.ScreenReceiver;

/**
 * 自动清理后台进程服务
 * Created by billow on 2016/11/19.
 */

public class AutoCleanService extends Service {

    private ActivityManager am;
    private ScreenReceiver screenReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        screenReceiver = new ScreenReceiver(am);
        //服务开启时，注册广播接收器
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //服务关闭时，移除广播接收器
        unregisterReceiver(screenReceiver);
        screenReceiver = null;
        super.onDestroy();
    }
}
