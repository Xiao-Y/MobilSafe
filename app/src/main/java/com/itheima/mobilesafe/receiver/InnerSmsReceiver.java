package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;

/**
 * 黑名单短信广播接收器
 * Created by billow on 2016/10/1.
 */
public class InnerSmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BlackNumberDao dao = new BlackNumberDao(context);

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object objs : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs);
            String send = smsMessage.getOriginatingAddress();
            try {
                String mode = dao.findMode(send);
                //拦截模式为短信和所有
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
