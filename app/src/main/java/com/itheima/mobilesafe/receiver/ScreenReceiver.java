package com.itheima.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itheima.mobilesafe.utils.LogUtil;

import java.util.List;

/**
 * 当前接到锁屏通知时开始执行
 * Created by billow on 2016/11/19.
 */

public class ScreenReceiver extends BroadcastReceiver {

    private ActivityManager am;

    public ScreenReceiver(ActivityManager am) {
        this.am = am;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("ScreenReceiver", "杀死进程");
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            //杀死进程
            am.killBackgroundProcesses(info.processName);
        }
    }
}
