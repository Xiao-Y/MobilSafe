package com.itheima.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Objects;

/**
 * 服务工具类
 * Created by billow on 2016/9/6.
 */
public class ServiceUtils {

    private static Context context = MyApplication.getContext();

    /**
     * 校验某个服务是否还运行
     *
     * @param serviceName 服务名称
     * @return
     */
    public static boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //最多获取100个服务
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            String className = serviceInfo.service.getClassName();
            if (Objects.equals(className, serviceName)) {
                return true;
            }
        }
        return false;
    }
}
