package com.itheima.mobilesafe.lastfind.pagesub;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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

    //private SharedPreferences sp;

    private CheckBox cbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sp = getSharedPreferences("config", MODE_PRIVATE);
        this.setContentView(R.layout.activity_setup4);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        //初始化复选框的状态
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            cbStatus.setChecked(true);
            cbStatus.setText("您已经开启防盗保护");
        } else {
            cbStatus.setChecked(false);
            cbStatus.setText("您还没有开启防盗保护");
        }

        cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean protecting) {
                if (protecting) {
                    cbStatus.setChecked(true);
                    cbStatus.setText("您已经开启防盗保护");
                } else {
                    cbStatus.setChecked(false);
                    cbStatus.setText("您还没有开启防盗保护");
                }

                //保存选择的状态
                Editor edit = sp.edit();
                edit.putBoolean("protecting", protecting);
                edit.commit();
            }
        });
    }

    /**
     * 完成
     *
     * @param view
     */
    public void next(View view) {
        Editor edit = sp.edit();
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
