package com.itheima.mobilesafe.taskmanger;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.adapter.TaskMangerAdapter;
import com.itheima.mobilesafe.base.BaseActivity;
import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.ui.ToastUtils;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 进程管理器
 */
public class TaskMangerActivity extends BaseActivity {

    public static String TAG = "TaskMangerActivity";

    private TextView tv_process_count;//运行中的进程
    private TextView tv_mem_info;//剩余/总内存
    private LinearLayout ll_loading;
    private ListView lv_task_manger;
    private List<TaskInfo> taskInfos;
    private TextView tv_status;
    private List<TaskInfo> userTaskInfo = new ArrayList<>();
    private List<TaskInfo> systemTaskInfo = new ArrayList<>();
    private TaskMangerAdapter adapter;
    private int processCount = 0;
    private long avaiMem = 0;
    private long totalMem = 0;

    @Override
    public void initView() {
        setContentView(R.layout.activity_task_manger);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
        ll_loading = (LinearLayout) this.findViewById(R.id.ll_loading);
        lv_task_manger = (ListView) findViewById(R.id.lv_task_manger);
        tv_status = (TextView) findViewById(R.id.tv_status);
    }

    @Override
    public void initData() {
        //填充数据
        fillData();
        lv_task_manger.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > userTaskInfo.size()) {
                    tv_status.setText("系统进程（" + systemTaskInfo.size() + "）");
                } else {
                    tv_status.setText("用户进程（" + userTaskInfo.size() + "）");
                }
            }
        });

        lv_task_manger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo taskInfo;
                if (position == 0) {//用户应用标签
                    return;
                } else if (position == userTaskInfo.size() + 1) {//系统应用标签
                    return;
                } else if (position <= userTaskInfo.size()) {//显示用户app
                    taskInfo = userTaskInfo.get(position - 1);
                } else {//显示系统app
                    taskInfo = systemTaskInfo.get(position - 1 - userTaskInfo.size() - 1);
                }
                TaskMangerAdapter.ViewHolder vh = (TaskMangerAdapter.ViewHolder) view.getTag();
                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                    vh.checkBox.setChecked(false);
                } else {
                    taskInfo.setChecked(true);
                    vh.checkBox.setChecked(true);
                }
            }
        });
    }

    /**
     * 设置进程管理器页面中的显示信息
     */
    private void setTitle() {
        processCount = SystemInfoUtils.getRunningProcessCount(this);
        avaiMem = SystemInfoUtils.getAvaiMem(this);
        totalMem = SystemInfoUtils.getTotalMem(this);
        tv_process_count.setText("运行中的进程：" + processCount + " 个");
        tv_mem_info.setText("剩余/总内存：" + Formatter.formatFileSize(this, avaiMem) + "/" + Formatter.formatFileSize(this, totalMem));
    }

    /**
     * 填充数据
     */
    private void fillData() {
        ll_loading.setVisibility(ProgressBar.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                taskInfos = TaskInfoProvider.getTaskInfos();
                //统计用户进程和系统进程的数量
                for (TaskInfo info : taskInfos) {
                    if (info.isUserTask()) {
                        userTaskInfo.add(info);
                    } else {
                        systemTaskInfo.add(info);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(ProgressBar.INVISIBLE);
                        adapter = new TaskMangerAdapter(TaskMangerActivity.this);
                        adapter.setTaskInfos(taskInfos);
                        adapter.setUserTaskInfo(userTaskInfo);
                        adapter.setSystemTaskInfo(systemTaskInfo);
                        lv_task_manger.setAdapter(adapter);
                        //设置进程管理器页面中的显示信息
                        setTitle();
                    }
                });
            }
        }.start();
    }

    /**
     * 全选
     *
     * @param view
     */
    public void selectAll(View view) {
        for (TaskInfo taskInfo : userTaskInfo) {
            taskInfo.setChecked(true);
        }
        for (TaskInfo taskInfo : systemTaskInfo) {
            taskInfo.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     *
     * @param view
     */
    public void selectOppo(View view) {
        for (TaskInfo taskInfo : userTaskInfo) {
            taskInfo.setChecked(!taskInfo.isChecked());
        }
        for (TaskInfo taskInfo : systemTaskInfo) {
            taskInfo.setChecked(!taskInfo.isChecked());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理
     *
     * @param view
     */
    public void killTask(View view) {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        int count = 0;
        long savedMem = 0;
        Iterator<TaskInfo> iterator = taskInfos.iterator();
        while (iterator.hasNext()) {
            TaskInfo taskInfo = iterator.next();
            if (taskInfo.isChecked()) {
                am.killBackgroundProcesses(taskInfo.getPackname());
                iterator.remove();
                count++;
                savedMem += taskInfo.getMemsize();
                if (taskInfo.isUserTask()) {
                    userTaskInfo.remove(taskInfo);
                } else {
                    systemTaskInfo.remove(taskInfo);
                }
            }
        }
        avaiMem += savedMem;
        processCount -= count;
        ll_loading.setVisibility(ProgressBar.INVISIBLE);
        adapter = new TaskMangerAdapter(TaskMangerActivity.this);
        adapter.setTaskInfos(taskInfos);
        adapter.setUserTaskInfo(userTaskInfo);
        adapter.setSystemTaskInfo(systemTaskInfo);
        lv_task_manger.setAdapter(adapter);
        //设置进程管理器页面中的显示信息
        tv_process_count.setText("运行中的进程：" + processCount + " 个");
        tv_mem_info.setText("剩余/总内存：" + Formatter.formatFileSize(TaskMangerActivity.this, avaiMem) + "/" + Formatter.formatFileSize(TaskMangerActivity.this, totalMem));
        ToastUtils.toastLong("杀死" + count + "进程，释放" + Formatter.formatFileSize(this, savedMem) + "内存！");
    }

    /**
     * 设置页面
     *
     * @param view
     */
    public void enterSetting(View view) {

    }
}
