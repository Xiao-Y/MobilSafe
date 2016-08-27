package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.GPSService;

/**
 * 短信广播接收器
 * Created by billow on 2016/8/24.
 */
public class SMSReceiver extends BroadcastReceiver {

    private final static String TAG = "SMSReceiver";
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //安全号码
        String phone = sp.getString("phone", "");

        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object b : objects) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
            String sender = sms.getOriginatingAddress();
            if (sender.contains(phone)) {
                String body = sms.getMessageBody();
                switch (body) {
                    case "#*location*#"://GPS追踪
                        //当接收到GPS追踪消息时，启动位置追踪服务
                        Intent i = new Intent(context, GPSService.class);
                        context.startService(i);
                        //将位置信息用短信发给安全号码
                        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                        String lastlocation = sp.getString("lastlocation", null);
                        if (TextUtils.isEmpty(lastlocation)) {
                            SmsManager.getDefault().sendTextMessage(sender, null, "还没有获取位置，正在努力中...", null, null);
                        } else {
                            SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                        }

                        Log.i(TAG, "#*location*#");
                        //终止广播，防止其它软件接收
                        abortBroadcast();
                        break;
                    case "#*alarm*#"://播放报警
                        Log.i(TAG, "#*alarm*#");
                        MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                        //循环播放
                        player.setLooping(true);
                        //声音开到最大
                        player.setVolume(1.0f, 1.0f);
                        //开始播放
                        player.start();
                        //终止接收短信广播，防止其它软件接收
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
}
