package com.itheima.mobilesafe.taskmanger;

import android.content.Context;
import android.content.SharedPreferences;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.base.BaseActivity;
import com.itheima.mobilesafe.listener.TaskSettingListener;
import com.itheima.mobilesafe.service.AutoCleanService;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.MyApplication;
import com.itheima.mobilesafe.utils.ServiceUtils;

public class TaskSettingActivity extends BaseActivity {
    private Context context = MyApplication.getContext();
    private SharedPreferences sp;

    private SettingItemView siv_show_sys_task;
    private SettingItemView siv_lock_task_clear;


    @Override
    public void initView() {
        setContentView(R.layout.activity_task_setting);
        siv_show_sys_task = (SettingItemView) findViewById(R.id.siv_show_sys_task);
        siv_lock_task_clear = (SettingItemView) findViewById(R.id.siv_lock_task_clear);
    }

    @Override
    public void initData() {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        siv_show_sys_task.setChecked(sp.getBoolean("showsys", false));
        siv_lock_task_clear.setChecked(sp.getBoolean("lockTaskClear", false));
    }

    @Override
    public void initOther() {
        siv_show_sys_task.setOnClickListener(new TaskSettingListener());
        siv_lock_task_clear.setOnClickListener(new TaskSettingListener());
    }

    @Override
    protected void onStart() {
        //设置页面的勾选框
        boolean running = ServiceUtils.isServiceRunning(AutoCleanService.class.getName());
        siv_lock_task_clear.setChecked(running);
        super.onStart();
    }
}
