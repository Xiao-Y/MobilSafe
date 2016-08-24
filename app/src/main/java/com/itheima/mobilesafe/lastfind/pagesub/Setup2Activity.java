package com.itheima.mobilesafe.lastfind.pagesub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.ActivityChange;


/**
 * 设置向导2
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午10:09:28
 */
public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_setup2_sim;
    private TelephonyManager tm;
    private String sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setup2);
        //获取手机服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        siv_setup2_sim = (SettingItemView) findViewById(R.id.siv_setup2_sim);
        //初始化复选框
        sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            siv_setup2_sim.setChecked(false);
        } else {
            siv_setup2_sim.setChecked(true);
        }

        siv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                boolean checked = !siv_setup2_sim.isChecked();
                siv_setup2_sim.setChecked(checked);
                if (checked) {
                    //获取sim卡序号
                    sim = tm.getSimSerialNumber();
                } else {
                    sim = null;
                }
                //保存sim卡序号
                edit.putString("sim", sim);
                edit.commit();
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void next(View view) {
        if (TextUtils.isEmpty(sim)) {
            Toast.makeText(this, "SIM没有绑定...", Toast.LENGTH_SHORT).show();
            //return;
        }
        ActivityChange.activityChange(Setup2Activity.this, Setup3Activity.class, view, ActivityChange.NEXT);
    }

    /**
     * 上一步
     *
     * @param view
     */
    public void prev(View view) {
        ActivityChange.activityChange(Setup2Activity.this, Setup1Activity.class, view, ActivityChange.PREV);
    }
}
