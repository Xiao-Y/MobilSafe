package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息类
 * Created by Administrator on 2016/10/29.
 */
public class TaskInfo {

    //应用名称
    private String name;
    //应用图标
    private Drawable icon;
    //应用唯一标识
    private String packname;
    //应用大小
    private long memsize;
    //是否是用户进程
    private boolean userTask;
    private boolean checked;

    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packname='" + packname + '\'' +
                ", memsize=" + memsize +
                ", userTask=" + userTask +
                '}';
    }
}
