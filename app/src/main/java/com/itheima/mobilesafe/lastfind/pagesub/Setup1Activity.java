package com.itheima.mobilesafe.lastfind.pagesub;

import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ActivityChange;


/**
 * 设置向导1
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午10:09:28
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setup1);
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void next(View view) {
        ActivityChange.activityChange(Setup1Activity.this, Setup2Activity.class, view, ActivityChange.NEXT);
    }

    @Override
    public void prev(View view) {

    }
}
