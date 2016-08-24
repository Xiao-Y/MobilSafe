package com.itheima.mobilesafe.lastfind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.lastfind.pagesub.Setup1Activity;

/**
 * 手机防盗主页
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午9:57:54
 */
public class LastFindActivity extends Activity {

    private SharedPreferences ps;
    private TextView tv_safenumber;
    private ImageView iv_protecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ps = this.getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = ps.getBoolean("configed", false);
        // 是否做过设置向导
        if (configed) {
            this.setContentView(R.layout.activity_lost_find);

            tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
            String phone = ps.getString("phone", "");
            tv_safenumber.setText(phone);

            iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
            Boolean protecting = ps.getBoolean("protecting", false);
            if (protecting) {
                iv_protecting.setImageResource(R.mipmap.lock);
            } else {
                iv_protecting.setImageResource(R.mipmap.unlock);
            }
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
