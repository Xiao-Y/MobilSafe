package com.itheima.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程管理配置器
 * Created by Administrator on 2016/11/2.
 */
public class TaskMangerAdapter extends BaseAdapter {

    private Context context;

    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userTaskInfo;
    private List<TaskInfo> systemTaskInfo;

    public TaskMangerAdapter(List<TaskInfo> taskInfos) {
        this.taskInfos = taskInfos;
        userTaskInfo = new ArrayList<>();
        systemTaskInfo = new ArrayList<>();
        for (TaskInfo info : taskInfos) {
            if (info.isUserTask()) {
                userTaskInfo.add(info);
            } else {
                systemTaskInfo.add(info);
            }
        }
    }

    public TaskMangerAdapter(Context context, List<TaskInfo> taskInfos) {
        this.context = context;
        this.taskInfos = taskInfos;
        userTaskInfo = new ArrayList<>();
        systemTaskInfo = new ArrayList<>();
        for (TaskInfo info : taskInfos) {
            if (info.isUserTask()) {
                userTaskInfo.add(info);
            } else {
                systemTaskInfo.add(info);
            }
        }
    }

    @Override
    public int getCount() {
        return taskInfos.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder vh;
        TaskInfo taskInfo;
        if (convertView == null || !(convertView instanceof RelativeLayout)) {
            view = LayoutInflater.from(context).inflate(R.layout.tesk_manager_item_view, null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) view.findViewById(R.id.color_image);
            vh.title = (TextView) view.findViewById(R.id.color_title);
            vh.text = (TextView) view.findViewById(R.id.color_text);
            vh.checkBox = (CheckBox) view.findViewById(R.id.cb_status);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }
        if (position == 0) {//用户应用标签
            TextView tv = new TextView(context);
            tv.setText("用户应用（" + userTaskInfo.size() + "）");
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position == userTaskInfo.size() + 1) {//系统应用标签
            TextView tv = new TextView(context);
            tv.setText("系统应用（" + systemTaskInfo.size() + "）");
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position <= userTaskInfo.size()) {//显示用户app
            taskInfo = userTaskInfo.get(position - 1);
        } else {//显示系统app
            taskInfo = systemTaskInfo.get(position - 1 - userTaskInfo.size() - 1);
        }
        vh.imageView.setImageDrawable(taskInfo.getIcon());
        vh.title.setText(taskInfo.getName());
        vh.text.setText("占用内存：" + Formatter.formatFileSize(context, taskInfo.getMemsize()));
        vh.checkBox.setChecked(taskInfo.isChecked());
        return view;
    }

    public class ViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView text;
        public CheckBox checkBox;
    }
}
