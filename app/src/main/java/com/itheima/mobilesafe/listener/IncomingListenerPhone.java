package com.itheima.mobilesafe.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.itheima.mobilesafe.ui.ToastUtils;

/**
 * 来电接听状态监听器
 * Created by billow on 2016/9/16.
 */
public class IncomingListenerPhone extends PhoneStateListener {

    //窗体管理者
    private WindowManager wm;
    private View view;

    public IncomingListenerPhone(WindowManager wm, View view) {
        this.wm = wm;
        this.view = view;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        // state：状态，incomingNumber：来电号码
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
                // 查询数据库的操作
                String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
                ToastUtils.myToast(wm, view, address);
                break;
            case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电拒绝
                //把这个View移除
                if (view != null) {
                    wm.removeView(view);
                }
                break;
            default:
                break;
        }
    }
}
