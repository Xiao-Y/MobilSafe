package com.itheima.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供进程信息
 * Created by Administrator on 2016/10/29.
 */
public class TaskInfoProvider {

    private static Context context = MyApplication.getContext();

    /**
     * 获取当前手机的进程信息
     *
     * @return
     */
    public static List<TaskInfo> getTaskInfos() {
        List<TaskInfo> infos = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo rap : processInfos) {
            TaskInfo info = new TaskInfo();
            String packname = rap.processName;
            info.setPackname(packname);
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[] { rap.pid });
            long memsize = memoryInfo[0].getTotalPrivateDirty();
            info.setMemsize(memsize);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                info.setIcon(icon);
                String name = applicationInfo.loadLabel(pm).toString();
                info.setName(name);
                if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) == 0) {//用户进程
                    info.setUserTask(true);
                } else {//系统进程
                    info.setUserTask(false);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            infos.add(info);
        }
        return infos;
    }
}
