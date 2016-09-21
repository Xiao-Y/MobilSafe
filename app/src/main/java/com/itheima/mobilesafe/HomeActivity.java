package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.atools.AtoolsActivity;
import com.itheima.mobilesafe.callsafe.CallSmsSafeActivity;
import com.itheima.mobilesafe.lastfind.ShowLastFindDialog;
import com.itheima.mobilesafe.setting.SettingActivity;

public class HomeActivity extends Activity {

    private GridView list_home;
    private MyAdapter adapter;
    private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
            "高级工具", "设置中心" };

    private static int[] ids = { R.mipmap.safe, R.mipmap.callmsgsafe, R.mipmap.app,
            R.mipmap.taskmanager, R.mipmap.netmanager, R.mipmap.trojan,
            R.mipmap.sysoptimize, R.mipmap.atools, R.mipmap.settings };

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyAdapter();
        list_home.setAdapter(adapter);
        list_home.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 8:// 进入设置中心
                        intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 0:// 点击手机防盗
                        new ShowLastFindDialog(HomeActivity.this, sp);
                        break;
                    case 7:// 进入高级工具
                        intent = new Intent(HomeActivity.this, AtoolsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:// 进入通讯卫士
                        intent = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

            tv_item.setText(names[position]);
            iv_item.setImageResource(ids[position]);
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
