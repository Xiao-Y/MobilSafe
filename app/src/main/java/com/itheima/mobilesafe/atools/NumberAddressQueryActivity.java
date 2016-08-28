package com.itheima.mobilesafe.atools;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;

/**
 * 号码归属地查询
 * Created by billow on 2016/8/28.
 */
public class NumberAddressQueryActivity extends Activity {

    private EditText edPhone;
    private Button btQuery;
    private TextView numbernateAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_number_address_query);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        btQuery = (Button) findViewById(R.id.bt_query);
        numbernateAddress = (TextView) findViewById(R.id.tv_bum_add);
    }

    /**
     * 查询号码归属地
     *
     * @param view
     */
    public void numberQuery(View view) {
        String phone = edPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "查询号码不能为空..", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "查询号码：" + phone, Toast.LENGTH_SHORT).show();
        }
    }
}
