package com.itheima.mobilesafe.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.listener.SettingListener;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.CallSmsSafeService;
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
    //设置拦截短信和来电
    private SettingItemView siv_balck;
    //设置显示归属地背景
    public final static String title = "归属地提示风格";
    public final static String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
        scv_changebg = (SettingClickView) findViewById(R.id.scv_change_bg);
        siv_balck = (SettingItemView) findViewById(R.id.siv_balck);

        // 初始化自动升级是否开启
        boolean update = sp.getBoolean("update", false);
        siv_update.setChecked(update);
        siv_update.setOnClickListener(new SettingListener());

        // 设置显示来电归属地
        siv_show_address.setOnClickListener(new SettingListener());

        //更换来电归属地的背景样式
        scv_changebg.setTitle(title);
        int which = sp.getInt("which", 0);
        scv_changebg.setDesc(items[which]);
        scv_changebg.setOnClickListener(new SettingListener(SettingActivity.this));

        //设置拦截短信和来电
        siv_balck.setOnClickListener(new SettingListener());
    }

    /**
     * 用于看到界面的时候执行
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 设置显示来电归属地
        //判断服务是否运行
        String name = AddressService.class.getName();
        boolean running = ServiceUtils.isServiceRunning(name);
        siv_show_address.setChecked(running);

        //设置拦截短信和来电
        name = CallSmsSafeService.class.getName();
        running = ServiceUtils.isServiceRunning(name);
        siv_balck.setChecked(running);
    }
}
