package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import java.util.Objects;

/**
 * 引导广播接收器
 * Created by billow on 2016/8/21.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取当前的sim序列号
        String sim = sp.getString("sim", null);
        //获取保存的sim序列号
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmSimCallManager = tm.getSimSerialNumber();
        //比较两个序列号是否相同
        if (!Objects.equals(sim, tmSimCallManager)) {
            System.out.println("SIM卡已经变更....");
        }
    }
}
