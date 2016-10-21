package com.itheima.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;

import java.util.List;
import java.util.Map;

/**
 * 显示应用列表的数据适配器
 * Created by billow on 2016/10/8.
 */

public class AppMangerAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> userApp;//用户app
    private List<AppInfo> systemApp;//系统app
    private Map<String, List<AppInfo>> appInfos;

    public AppMangerAdapter(Context context, Map<String, List<AppInfo>> appInfos) {
        this.context = context;
        this.appInfos = appInfos;
    }

    /**
     * 控制listView的条目
     *
     * @return
     */
    @Override
    public int getCount() {
        userApp = appInfos.get("userApp");
        systemApp = appInfos.get("systemApp");
        return userApp.size() + systemApp.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        AppManagerHolder ch;
        if (view == null || !(view instanceof RelativeLayout)) {
            //为每一条数据加载布局
            view = LayoutInflater.from(context).inflate(R.layout.app_manager_item_view, null);
            ch = new AppManagerHolder();
            ch.imageView = (ImageView) view.findViewById(R.id.color_image);
            ch.title = (TextView) view.findViewById(R.id.color_title);
            ch.text = (TextView) view.findViewById(R.id.color_text);
            view.setTag(ch);
        }
        ch = (AppManagerHolder) view.getTag();
        AppInfo appInfo;
        if (i == 0) {
            TextView tv = new TextView(context);
            tv.setText("用户应用（" + userApp.size() + "）");
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (i == userApp.size() + 1) {
            TextView tv = new TextView(context);
            tv.setText("系统应用（" + systemApp.size() + "）");
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (i <= userApp.size()) {//显示用户app
            int newPosition = i - 1;
            appInfo = userApp.get(newPosition);
        } else {//显示系统app
            int newPosition = i - 1 - userApp.size() - 1;
            appInfo = systemApp.get(newPosition);
        }
        ch.imageView.setImageDrawable(appInfo.getIcon());
        ch.title.setText(appInfo.getName());
        if (appInfo.isInRom()) {
            ch.text.setText("手机内存");
        } else {
            ch.text.setText("外部内存");
        }
        return view;
    }
}

class AppManagerHolder {
    ImageView imageView;
    TextView title;
    TextView text;
}