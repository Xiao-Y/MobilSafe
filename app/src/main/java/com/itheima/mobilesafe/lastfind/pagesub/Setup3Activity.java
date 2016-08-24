package com.itheima.mobilesafe.lastfind.pagesub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.lastfind.SelectContactActivity;
import com.itheima.mobilesafe.utils.ActivityChange;


/**
 * 设置向导3
 *
 * @author XiaoY
 * @date: 2016年8月17日 下午10:09:28
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText tx_setup3_phone;
    SharedPreferences.Editor edit = null;
    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setup3);
        tx_setup3_phone = (EditText) findViewById(R.id.tx_setup3_phone);
        phone = sp.getString("phone", "");
        tx_setup3_phone.setText(phone);

    }

    /**
     * 点击“选择联系人”
     *
     * @param view
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        phone = data.getStringExtra("phone").replace("-", "").replace(" ", "").replace("+86", "");
        tx_setup3_phone.setText(phone);
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void next(View view) {
        edit = sp.edit();
        edit.putString("phone", phone);
        edit.commit();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "安全号码不能为空...", Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityChange.activityChange(Setup3Activity.this, Setup4Activity.class, view, ActivityChange.NEXT);
    }

    /**
     * 上一步
     *
     * @param view
     */
    public void prev(View view) {
        ActivityChange.activityChange(Setup3Activity.this, Setup2Activity.class, view, ActivityChange.PREV);
    }
}
