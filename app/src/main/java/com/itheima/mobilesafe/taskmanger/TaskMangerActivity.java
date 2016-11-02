package com.itheima.mobilesafe.taskmanger;

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
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import java.util.ArrayList;
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
        int processCount = SystemInfoUtils.getRunningProcessCount(this);
        tv_process_count.setText("运行中的进程：" + processCount + " 个");
        long avaiMem = SystemInfoUtils.getAvaiMem(this);
        long totalMem = SystemInfoUtils.getTotalMem(this);
        tv_mem_info.setText("剩余/总内存：" + Formatter.formatFileSize(this, avaiMem) + "/" + Formatter.formatFileSize(this, totalMem));
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
                        lv_task_manger.setAdapter(new TaskMangerAdapter(TaskMangerActivity.this, taskInfos));
                    }
                });
            }
        }.start();
    }
}
