package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 短信广播接收器
 * Created by billow on 2016/8/24.
 */
public class SMSReceiver extends BroadcastReceiver {

    private final static String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object b : objects) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
            String sender = sms.getOriginatingAddress();
            String body = sms.getMessageBody();
            switch (body) {
                case "#*location*#"://GPS追踪
                    Log.i(TAG, "#*location*#");
                    //终止广播，防止其它软件接收
                    abortBroadcast();
                    break;
                case "#*alarm*#"://播放报警
                    Log.i(TAG, "#*alarm*#");
                    //终止广播，防止其它软件接收
                    abortBroadcast();
                    break;
                case "#*wipadata*#"://远程销毁数据
                    Log.i(TAG, "#*wipadata*#");
                    //终止广播，防止其它软件接收
                    abortBroadcast();
                    break;
                case "#*lockscreen*#"://行程锁定
                    Log.i(TAG, "#*lockscreen*#");
                    //终止广播，防止其它软件接收
                    abortBroadcast();
                    break;
                default:
            }
        }
    }
}
