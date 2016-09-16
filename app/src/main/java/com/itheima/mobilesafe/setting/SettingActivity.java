package com.itheima.mobilesafe.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.listener.SettingListener;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.ui.SettingClickView;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.ServiceUtils;

/**
 * 设置中心
 *
 * @author XiaoY
 * @date: 2016年8月19日 下午10:53:11
 */
public class SettingActivity extends Activity {

    private SharedPreferences sp;
    //设置自动更新
    private SettingItemView siv_update;
    //设置显示来电归属地
    private SettingItemView siv_show_address;
    //设置显示归属地背景
    private SettingClickView scv_changebg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
        scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);

        // 初始化自动升级是否开启
        boolean update = sp.getBoolean("update", false);
        siv_update.setChecked(update);
        siv_update.setOnClickListener(new SettingListener());

        //判断服务是否运行
        String name = AddressService.class.getName();
        boolean running = ServiceUtils.isServiceRunning(name);
        siv_show_address.setChecked(running);
        siv_show_address.setOnClickListener(new SettingListener());

        //设置显示归属地背景
        final String title = "归属地提示风格";
        scv_changebg.setTitle(title);
        final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
        int which = sp.getInt("which", 0);
        scv_changebg.setDesc(items[which]);
        scv_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dd = sp.getInt("which", 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle(title);
                builder.setSingleChoiceItems(items, dd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存选择参数
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which", which);
                        editor.commit();
                        scv_changebg.setDesc(items[which]);
                        //取消对话框
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
}
