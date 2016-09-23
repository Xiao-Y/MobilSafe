package com.itheima.mobilesafe.callsafe;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.List;
import java.util.zip.Inflater;

/**
 * 通讯卫士
 * Created by billow on 2016/9/21.
 */
public class CallSmsSafeActivity extends Activity {

    private ListView lvCallSmsSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_call_sms_safe);
        lvCallSmsSafe = (ListView) findViewById(R.id.lv_call_sms_safe);
        try {
            BlackNumberDao dao = new BlackNumberDao(this);
            List<BlackNumberInfo> list = dao.findAll();
            lvCallSmsSafe.setAdapter(new MyAdapter(this, list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<BlackNumberInfo> list;

        public MyAdapter(Context context, List<BlackNumberInfo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public BlackNumberInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_black_number, null);
            }
            TextView blackNumber = (TextView) convertView.findViewById(R.id.tv_number);
            TextView mode = (TextView) convertView.findViewById(R.id.tv_mode);
            BlackNumberInfo numberInfo = getItem(position);
            blackNumber.setText(numberInfo.getNumber());
            mode.setText(numberInfo.getMode());
            return convertView;
        }
    }
}
