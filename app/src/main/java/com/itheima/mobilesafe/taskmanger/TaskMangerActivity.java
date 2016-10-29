package com.itheima.mobilesafe.taskmanger;

import android.text.format.Formatter;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.base.BaseActivity;
import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.utils.LogUtil;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import java.util.List;

/**
 * 进程管理器
 */
public class TaskMangerActivity extends BaseActivity {

    public static String TAG = "TaskMangerActivity";

    private TextView tv_process_count;//运行中的进程
    private TextView tv_mem_info;//剩余/总内存

    @Override
    public void initView() {
        setContentView(R.layout.activity_task_manger);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
    }

    @Override
    public void initData() {
        int processCount = SystemInfoUtils.getRunningProcessCount(this);
        tv_process_count.setText("运行中的进程：" + processCount + " 个");
        long avaiMem = SystemInfoUtils.getAvaiMem(this);
        long totalMem = SystemInfoUtils.getTotalMem(this);
        tv_mem_info.setText("剩余/总内存：" + Formatter.formatFileSize(this, avaiMem)
                + "/" + Formatter.formatFileSize(this, totalMem));
        List<TaskInfo> taskInfos = TaskInfoProvider.getTaskInfos();
        for (TaskInfo info : taskInfos) {
            LogUtil.i(TAG, info.toString());
        }
    }
}
