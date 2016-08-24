package com.itheima.mobilesafe.lastfind.pagesub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.lastfind.LastFindActivity;
import com.itheima.mobilesafe.utils.ActivityChange;


/**
 * 设置向导4
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午10:09:28
 */
public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ps = getSharedPreferences("config", MODE_PRIVATE);
        this.setContentView(R.layout.activity_setup4);
    }

    /**
     * 完成
     *
     * @param view
     */
    public void next(View view) {
        Editor edit = ps.edit();
        edit.putBoolean("configed", true);
        edit.commit();
        Intent intent = new Intent(Setup4Activity.this, LastFindActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * 上一步
     *
     * @param view
     */
    public void prev(View view) {
        ActivityChange.activityChange(Setup4Activity.this, Setup3Activity.class, view, ActivityChange.PREV);
    }
}
