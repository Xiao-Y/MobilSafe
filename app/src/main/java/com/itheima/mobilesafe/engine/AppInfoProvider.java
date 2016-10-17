package com.itheima.mobilesafe.engine;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务方法：提供手机中所有的应用程序信息
 * Created by billow on 2016/10/8.
 */

public class AppInfoProvider {

    public static List<AppInfo> getAppInfos() {

        List<AppInfo> list = new ArrayList<>();

        PackageManager pm = MyApplication.getContext().getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        for (PackageInfo packInfo : packInfos) {
            AppInfo info = new AppInfo();
            ApplicationInfo applicationInfo = packInfo.applicationInfo;
            info.setPackname(packInfo.packageName);
            info.setName(String.valueOf(applicationInfo.loadLabel(pm)));
            info.setIcon(applicationInfo.loadIcon(pm));
            int flags = applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//用户程序
                info.setUserApp(true);
            } else {//系统程序
                info.setUserApp(false);
            }

            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {//内存储
                info.setInRom(true);
            } else {//外存储
                info.setInRom(false);
            }

            list.add(info);
        }
        return list;
    }
}
