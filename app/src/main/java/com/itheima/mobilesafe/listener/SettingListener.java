package com.itheima.mobilesafe.listener;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.MyApplication;

/**
 * Created by billow on 2016/9/6.
 */
public class SettingListener implements View.OnClickListener {

    private SharedPreferences sp;
    private Context context = MyApplication.getContext();
    private SharedPreferences.Editor editor;
    private Intent showAddress;

    @Override
    public void onClick(View view) {
        SettingItemView siView = (SettingItemView) view;
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        switch (view.getId()) {
            case R.id.siv_update:
                this.autoUpdate(siView);
                break;
            case R.id.siv_show_address:
                this.showAddress(siView);
            default:
                break;
        }
    }

    /**
     * 设置显示来电归属地
     *
     * @param view
     */
    private void showAddress(SettingItemView view) {
        showAddress = new Intent(context, AddressService.class);
        // 判断是否有选中
        if (view.isChecked()) {
            context.stopService(showAddress);
            view.setChecked(false);
        } else {
            context.startService(showAddress);
            view.setChecked(true);
        }
    }

    /**
     * 设置自动更新
     */
    private void autoUpdate(SettingItemView view) {
        editor = sp.edit();
        // 判断是否有选中
        // 已经打开自动升级了
        if (view.isChecked()) {
            view.setChecked(false);
            editor.putBoolean("update", false);
        } else {
            // 没有打开自动升级
            view.setChecked(true);
            editor.putBoolean("update", true);
        }
        editor.commit();
    }
}
