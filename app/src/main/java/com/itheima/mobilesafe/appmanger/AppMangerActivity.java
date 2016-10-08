package com.itheima.mobilesafe.appmanger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.adapter.AppMangerAdapter;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import java.util.List;

/**
 * Created by billow on 2016/10/8.
 */

public class AppMangerActivity extends Activity {

    public final static String TAG = "AppMangerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;
    private ListView lv_app_manger;
    private LinearLayout ll_loading;
    private List<AppInfo> appInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_app_manager);
        tv_avail_rom = (TextView) this.findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) this.findViewById(R.id.tv_avail_sd);
        lv_app_manger = (ListView) this.findViewById(R.id.lv_app_manger);
        ll_loading = (LinearLayout) this.findViewById(R.id.ll_loading);

        long sdSize = this.getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        long romSize = this.getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        tv_avail_rom.setText("手机内存:" + Formatter.formatFileSize(this, romSize));
        tv_avail_sd.setText("SD卡内存:" + Formatter.formatFileSize(this, sdSize));


        ll_loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        lv_app_manger.setAdapter(new AppMangerAdapter(AppMangerActivity.this, appInfos));
                    }
                });
            }
        }.start();
    }

    /**
     * 获取某个目录下的可用空间
     *
     * @param path
     * @return
     */
    public long getAvailSpace(String path) {
        StatFs statF = new StatFs(path);
        return statF.getAvailableBytes();
    }
}
