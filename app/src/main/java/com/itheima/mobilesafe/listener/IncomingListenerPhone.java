package com.itheima.mobilesafe.listener;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.ui.ToastUtils;
import com.itheima.mobilesafe.utils.LogUtil;
import com.itheima.mobilesafe.utils.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 来电接听状态监听器
 * Created by billow on 2016/9/16.
 */
public class IncomingListenerPhone extends PhoneStateListener {
    public final static String TAG = "IncomingListenerPhone";

    /**
     * 监听标志：来电、去电归属地显示
     */
    public final static int LISTENER_FLAG_ADDRESS_SERVICE = 1;
    /**
     * 监听标志：来电黑名单拦截
     */
    public final static int LISTENER_FLAG_BALCK_NUMBER = 2;

    //窗体管理者
    private WindowManager wm;
    private View view;
    //监听标志
    private int listenerFlag;

    public IncomingListenerPhone(int listenerFlag) {
        this.listenerFlag = listenerFlag;
    }

    public IncomingListenerPhone(WindowManager wm, View view, int listenerFlag) {
        this.wm = wm;
        this.view = view;
        this.listenerFlag = listenerFlag;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        // state：状态，incomingNumber：来电号码
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
                if (listenerFlag == LISTENER_FLAG_ADDRESS_SERVICE) {
                    // 查询数据库的操作
                    String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
                    ToastUtils.myToast(wm, view, address);
                } else if (listenerFlag == LISTENER_FLAG_BALCK_NUMBER) {
                    BlackNumberDao dao = new BlackNumberDao(MyApplication.getContext());
                    try {
                        String mode = dao.findMode(incomingNumber);
                        if ("2".equals(mode) || "3".equals(mode)) {
                            this.endCall();
                            LogUtil.i(TAG, "电话被挂断...");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电拒绝
                if (listenerFlag == LISTENER_FLAG_ADDRESS_SERVICE) {
                    //把这个View移除
                    if (view != null) {
                        wm.removeView(view);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 挂断电话(需要引入两个aidl文件)
     */
    private void endCall() {
        try {
            Class<?> clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, Context.TELECOM_SERVICE);
            ITelephony.Stub.asInterface(iBinder).endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
