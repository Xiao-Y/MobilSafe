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
            list.add(info);
        }
        return list;
    }
}
