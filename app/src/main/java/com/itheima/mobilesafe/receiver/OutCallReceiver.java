package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.itheima.mobilesafe.ui.ToastUtils;

/**
 * 拨打电话广播接收器
 * Created by billow on 2016/9/16.
 */
public class OutCallReceiver extends BroadcastReceiver {

    //窗体管理者
    private WindowManager wm;
    private View view;

    public OutCallReceiver(WindowManager wm, View view) {
        this.wm = wm;
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String phone = this.getResultData();
        String address = NumberAddressQueryUtils.queryNumber(phone);
        ToastUtils.myToast(wm, view, address);
    }
}
