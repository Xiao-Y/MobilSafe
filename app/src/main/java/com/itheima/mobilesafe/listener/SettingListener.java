package com.itheima.mobilesafe.listener;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.setting.SettingActivity;
import com.itheima.mobilesafe.ui.SettingClickView;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.MyApplication;

/**
 * Created by billow on 2016/9/6.
 */
public class SettingListener implements View.OnClickListener {

    private SharedPreferences sp;
    private Context context = MyApplication.getContext();
    private SharedPreferences.Editor editor;
    private Intent intent;

    public SettingListener() {
    }

    public SettingListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        switch (view.getId()) {
            case R.id.siv_update:
                this.autoUpdate(view);
                break;
            case R.id.siv_show_address:
                this.showAddress(view);
                break;
            case R.id.scv_change_bg:
                this.changeBg(view);
                break;
            case R.id.siv_balck:
                this.enableBlackNumber(view);
                break;
            default:
                break;
        }
    }

    /**
     * 设置拦截短信和来电
     *
     * @param view
     */
    private void enableBlackNumber(View view) {
        SettingItemView siView = (SettingItemView) view;
        intent = new Intent(context, CallSmsSafeService.class);
        // 判断是否有选中
        if (siView.isChecked()) {
            context.stopService(intent);
            siView.setChecked(false);
        } else {
            context.startService(intent);
            siView.setChecked(true);
        }
    }

    /**
     * 更换来电归属地的背景样式
     *
     * @param view
     */
    private void changeBg(View view) {
        final String[] items = SettingActivity.items;
        String title = SettingActivity.title;
        final SettingClickView scv_change_bg = (SettingClickView) view.findViewById(R.id.scv_change_bg);
        int dd = sp.getInt("which", 0);
        //dialog依赖于窗体
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, dd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //保存选择参数
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("which", which);
                editor.commit();
                scv_change_bg.setDesc(items[which]);
                //取消对话框
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 设置显示来电归属地
     *
     * @param view
     */
    private void showAddress(View view) {
        SettingItemView siView = (SettingItemView) view;
        intent = new Intent(context, AddressService.class);
        // 判断是否有选中
        if (siView.isChecked()) {
            context.stopService(intent);
            siView.setChecked(false);
        } else {
            context.startService(intent);
            siView.setChecked(true);
        }
    }

    /**
     * 设置自动更新
     */
    private void autoUpdate(View view) {
        SettingItemView siView = (SettingItemView) view;
        editor = sp.edit();
        // 判断是否有选中
        // 已经打开自动升级了
        if (siView.isChecked()) {
            siView.setChecked(false);
            editor.putBoolean("update", false);
        } else {
            // 没有打开自动升级
            siView.setChecked(true);
            editor.putBoolean("update", true);
        }
        editor.commit();
    }
}
