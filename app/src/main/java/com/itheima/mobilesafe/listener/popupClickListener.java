package com.itheima.mobilesafe.listener;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.ui.ToastUtils;
import com.itheima.mobilesafe.utils.MyApplication;

/**
 * 弹出窗体的点击事件
 * Created by billow on 2016/10/21.
 */

public class popupClickListener implements View.OnClickListener {
    private Context context;
    private AppInfo appInfo;
    private CallBack callBack;

    public popupClickListener(Context context, AppInfo appInfo, CallBack callBack) {
        this.context = context;
        this.appInfo = appInfo;
        this.callBack = callBack;
    }

    @Override
    public void onClick(View v) {
        callBack.callBackAfterOnClick();
        switch (v.getId()) {
            case R.id.ll_share:
                break;
            case R.id.ll_start:
                this.startApplication();
                break;
            case R.id.ll_uninstall:
                if (appInfo.isUserApp()) {
                    this.uninstallApplication();
                    callBack.callBackBeforeOnClick();
                } else {
                    ToastUtils.toastShort("系统应用必须获得ROOT权限才能卸载...");
                }
                break;
        }
    }

    /**
     * 卸载应用
     */
    private void uninstallApplication() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + appInfo.getPackname()));
        context.startActivity(intent);
    }

    /**
     * 启动应用程序
     */
    private void startApplication() {
        PackageManager pm = MyApplication.getContext().getPackageManager();
        //        Intent intent = new Intent();
        //        intent.setAction("android.intent.action.MAIN");
        //        intent.addCategory("android.intent.category.LAUNCHER");
        //        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackname());
        if (intent != null) {
            context.startActivity(intent);
        } else {
            ToastUtils.toastShort("对不起没有找到该应用...");
        }
    }

    public interface CallBack {
        void callBackBeforeOnClick();

        void callBackAfterOnClick();
    }
}
