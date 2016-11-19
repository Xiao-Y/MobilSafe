package com.itheima.mobilesafe.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AutoCleanService;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.MyApplication;

/**
 * Created by billow on 2016/11/19.
 */

public class TaskSettingListener implements View.OnClickListener {

    private Context context = MyApplication.getContext();

    private SharedPreferences sp;

    @Override
    public void onClick(View v) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        switch (v.getId()) {
            case R.id.siv_show_sys_task://显示系统进程
                showSysTask(v);
                break;
            case R.id.siv_lock_task_clear://锁屏清理进程
                lockTaskClear(v);
                break;
        }
    }

    /**
     * 锁屏清理进程
     *
     * @param v
     */
    private void lockTaskClear(View v) {
        SettingItemView siView = (SettingItemView) v;
        boolean checked = siView.isChecked();
        Intent intent = new Intent(context, AutoCleanService.class);
        if (checked) {
            context.stopService(intent);
        } else {
            context.startService(intent);
        }
        siView.setChecked(!checked);
    }

    /**
     * 显示系统进程
     *
     * @param v
     */
    private void showSysTask(View v) {
        SettingItemView siView = (SettingItemView) v;
        SharedPreferences.Editor edit = sp.edit();
        boolean checked = siView.isChecked();
        siView.setChecked(!checked);
        edit.putBoolean("showsys", !checked);
        edit.commit();
    }
}
