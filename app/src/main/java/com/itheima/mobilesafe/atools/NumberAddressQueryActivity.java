package com.itheima.mobilesafe.atools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.NumberAddressQueryUtils;

/**
 * 号码归属地查询
 * Created by billow on 2016/8/28.
 */
public class NumberAddressQueryActivity extends Activity {

    private EditText edPhone;
    private Button btQuery;
    private TextView numbernateAddress;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_number_address_query);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        btQuery = (Button) findViewById(R.id.bt_query);
        numbernateAddress = (TextView) findViewById(R.id.tv_bum_add);

        //当输入域改变时，触发
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() >= 3) {
                    String address = NumberAddressQueryUtils.queryNumber(charSequence.toString());
                    numbernateAddress.setText(address);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            edPhone.startAnimation(animation);
            //添加振动效果
            long[] pattern = { 200, 200, 300, 300, 1000, 2000 };
            //-1-不重复，0-重复
            vibrator.vibrate(pattern, -1);
        } else {
            String address = NumberAddressQueryUtils.queryNumber(phone);
            numbernateAddress.setText(address);
            //Toast.makeText(this, "查询号码：" + phone, Toast.LENGTH_SHORT).show();
        }
    }
}
