package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import com.itheima.mobilesafe.listener.IncomingListenerPhone;
import com.itheima.mobilesafe.receiver.OutCallReceiver;

/**
 * 来电号码归属地显示
 * Created by billow on 2016/9/6.
 */
public class AddressService extends Service {


    //电话服务
    private TelephonyManager tm;
    private IncomingListenerPhone incominglistenerPhone;
    //窗体管理者
    private WindowManager wm;
    private View view;
    //拨打电话广播接收者
    private BroadcastReceiver outCallReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //实例化窗体
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 监听来电
        incominglistenerPhone = new IncomingListenerPhone(wm, view);
        tm.listen(incominglistenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
        //用代码去注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        outCallReceiver = new OutCallReceiver(wm, view);
        registerReceiver(outCallReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消监听来电
        tm.listen(incominglistenerPhone, PhoneStateListener.LISTEN_NONE);
        incominglistenerPhone = null;
        //用代码取消注册广播接收者
        unregisterReceiver(outCallReceiver);
        outCallReceiver = null;
    }
}
