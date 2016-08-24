package com.itheima.mobilesafe.lastfind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.lastfind.pagesub.Setup1Activity;
import com.itheima.mobilesafe.R;

/**
 * 手机防盗主页
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午9:57:54
 */
public class LastFindActivity extends Activity {

    private SharedPreferences ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ps = this.getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = ps.getBoolean("configed", false);
        // 是否做过设置向导
        if (configed) {
            this.setContentView(R.layout.activity_lost_find);
        } else {
            reEnterSetup(null);
        }
    }

    /**
     * 打开设置向导页面
     */
    public void reEnterSetup(View view) {
        Intent intent = new Intent(LastFindActivity.this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }

}
