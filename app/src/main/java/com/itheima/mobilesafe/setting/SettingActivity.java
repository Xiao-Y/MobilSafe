package com.itheima.mobilesafe.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.listener.SettingListener;
import com.itheima.mobilesafe.service.AddressService;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);

        // 初始化自动升级是否开启
        boolean update = sp.getBoolean("update", false);
        siv_update.setChecked(update);
        siv_update.setOnClickListener(new SettingListener());

        //判断服务是否运行
        String name = AddressService.class.getName();
        boolean running = ServiceUtils.isServiceRunning(name);
        siv_show_address.setChecked(running);
        siv_show_address.setOnClickListener(new SettingListener());
    }
}
