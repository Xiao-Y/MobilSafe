package com.itheima.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.AppInfo;

import java.util.List;

/**
 * 显示应用列表的数据适配器
 * Created by billow on 2016/10/8.
 */

public class AppMangerAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> appInfos;

    public AppMangerAdapter(Context context, List<AppInfo> appInfos) {
        this.context = context;
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        AppManagerHolder ch;
        if (view == null) {
            //为每一条数据加载布局
            view = LayoutInflater.from(context).inflate(R.layout.app_manager_item_view, null);
            ch = new AppManagerHolder();
            ch.imageView = (ImageView) view.findViewById(R.id.color_image);
            ch.title = (TextView) view.findViewById(R.id.color_title);
            ch.text = (TextView) view.findViewById(R.id.color_text);
            view.setTag(ch);
        }
        ch = (AppManagerHolder) view.getTag();
        AppInfo appInfo = appInfos.get(i);
        ch.imageView.setImageDrawable(appInfo.getIcon());
        ch.title.setText(appInfo.getName());
        ch.text.setText(appInfo.getPackname());
        return view;
    }
}

class AppManagerHolder {
    ImageView imageView;
    TextView title;
    TextView text;
}