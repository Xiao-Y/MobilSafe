package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;

/**
 * 黑名单拦截服务<br/>
 * 当服务启动时，监听短信广播
 * Created by billow on 2016/9/26.
 */
public class CallSmsSafeService extends Service {
    private InnerSmsReceiver smsReceiver;
    private BlackNumberDao dao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 短信广播接收器
     */
    private class InnerSmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object objs : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs);
                String send = smsMessage.getOriginatingAddress();
                try {
                    String mode = dao.findMode(send);
                    if ("2".equals(mode) || "3".equals(mode)) {
                        //阻止广播的传递
                        abortBroadcast();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        dao = new BlackNumberDao(this);
        smsReceiver = new InnerSmsReceiver();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(smsReceiver);
        smsReceiver = null;
        super.onDestroy();
    }
}
