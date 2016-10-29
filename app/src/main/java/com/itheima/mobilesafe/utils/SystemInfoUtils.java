package com.itheima.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 系统工具类
 * Created by Administrator on 2016/10/29.
 */
public class SystemInfoUtils {

    /**
     * 获取当前正在运行的进程数量
     *
     * @return 进程数量
     */
    public static int getRunningProcessCount(Context context) {
        //PackageManager //包管理器   相当于程序管理器。静态的内容
        //ActivityManager //进程管理器  管理手机的活动信息
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }

    /**
     * 获取手机剩余内存
     *
     * @return
     */
    public static long getAvaiMem(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取手机总内存
     *
     * @return
     */
    public static long getTotalMem(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;
    }
}
