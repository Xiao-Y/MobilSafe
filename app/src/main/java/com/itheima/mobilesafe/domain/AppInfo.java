package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息
 * Created by billow on 2016/10/8.
 */

public class AppInfo {

    //应用的名称
    private String name;
    //所在的包名
    private String packname;
    //应用图标
    private Drawable icon;
    //是否是用户应用
    private boolean userApp;
    //是否安装在手机内存
    private boolean inRom;

    /**
     * 应用的名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 应用的名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 所在的包名
     *
     * @return
     */
    public String getPackname() {
        return packname;
    }

    /**
     * 所在的包名
     *
     * @param packname
     */
    public void setPackname(String packname) {
        this.packname = packname;
    }

    /**
     * 应用图标
     *
     * @return
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * 应用图标
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * 是否是用户应用
     *
     * @return
     */
    public boolean isUserApp() {
        return userApp;
    }

    /**
     * 是否是用户应用
     *
     * @param userApp
     */
    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    /**
     * 是否安装在手机内存
     *
     * @return
     */
    public boolean isInRom() {
        return inRom;
    }

    /**
     * 是否安装在手机内存
     *
     * @param inRom
     */
    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", packname='" + packname + '\'' +
                ", icon=" + icon +
                ", userApp=" + userApp +
                ", inRom=" + inRom +
                '}';
    }
}
