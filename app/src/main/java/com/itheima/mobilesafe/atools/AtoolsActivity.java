package com.itheima.mobilesafe.atools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.SmsUtil;

/**
 * 高级工具
 * Created by billow on 2016/8/28.
 */
public class AtoolsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_atools);
    }

    /**
     * 查询电话号码
     *
     * @param view
     */
    public void numberQuery(View view) {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 短信备份
     *
     * @param view
     */
    public void smsBackup(View view) {
        try {
            SmsUtil.smsBackup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短信还原
     *
     * @param view
     */
    public void smsRestore(View view) {

    }
}
