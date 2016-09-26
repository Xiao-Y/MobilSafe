package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.itheima.mobilesafe.listener.IncomingListenerPhone;
import com.itheima.mobilesafe.receiver.InnerSmsReceiver;

/**
 * 黑名单拦截服务<br/>
 * 当服务启动时，监听短信广播
 * Created by billow on 2016/9/26.
 */
public class CallSmsSafeService extends Service {
    private InnerSmsReceiver smsReceiver;
    private IncomingListenerPhone listenerPhone;
    //电话服务
    private TelephonyManager tm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listenerPhone = new IncomingListenerPhone(IncomingListenerPhone.LISTENER_FLAG_BALCK_NUMBER);
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
        smsReceiver = new InnerSmsReceiver();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 取消监听来电
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(smsReceiver);
        smsReceiver = null;
        super.onDestroy();
    }
}
