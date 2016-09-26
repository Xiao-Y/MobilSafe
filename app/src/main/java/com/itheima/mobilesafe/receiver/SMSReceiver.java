package com.itheima.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
 * 手机防盗短信广播接收器
 * Created by billow on 2016/8/24.
 */
public class SMSReceiver extends BroadcastReceiver {

    /**
     * 设备管理策略服务
     */
    private DevicePolicyManager dpm;
    private final static String TAG = "SMSReceiver";
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

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
                        getLocation(context, sender);
                        break;
                    case "#*alarm*#"://播放报警
                        playerAlarm(context);
                        break;
                    case "#*wipadata*#"://远程销毁数据
                        wipadtata(context);
                        break;
                    case "#*lockscreen*#"://远程锁定
                        lockscreen(context);
                        break;
                    default:
                }
            }
        }
    }

    /**
     * 远程销毁数据
     *
     * @param context
     */
    private void wipadtata(Context context) {
        ComponentName mDeviceAdminSample = new ComponentName(context, MyDeviceAdminReceiver.class);
        if (dpm.isAdminActive(mDeviceAdminSample)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //激活MyAddmin
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            //解说对此应用开启设备管理员的用处
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一个用于设备的屏幕的锁定");
            context.startActivity(intent);
        }
        //格式化SD卡
        //dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        //删除数据
        //dpm.wipeData(0);//0表示恢复原厂设置
        Log.i(TAG, "#*wipadata*#");
        //终止广播，防止其它软件接收
        abortBroadcast();
    }

    /**
     * 远程锁定
     *
     * @param context
     */
    private void lockscreen(Context context) {
        //判断是否开启设备管理员权限
        ComponentName mDeviceAdminSample = new ComponentName(context, MyDeviceAdminReceiver.class);
        if (dpm.isAdminActive(mDeviceAdminSample)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //激活MyAddmin
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            //解说对此应用开启设备管理员的用处
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一个用于设备的屏幕的锁定");
            context.startActivity(intent);
        }
        //设置锁屏
        dpm.lockNow();
        //重设密码
        //dpm.resetPassword("1234", 0);
        Log.i(TAG, "#*lockscreen*#");
        //终止广播，防止其它软件接收
        abortBroadcast();
    }

    /**
     * 播放报警
     *
     * @param context
     */
    private void playerAlarm(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
        //循环播放
        player.setLooping(true);
        //声音开到最大
        player.setVolume(1.0f, 1.0f);
        //开始播放
        player.start();
        Log.i(TAG, "#*alarm*#");
        //终止接收短信广播，防止其它软件接收
        abortBroadcast();
    }

    /**
     * 获取手机位置
     *
     * @param context
     * @param sender  信息发送者
     */
    private void getLocation(Context context, String sender) {
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
    }
}
