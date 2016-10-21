package com.itheima.mobilesafe.appmanger;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.adapter.AppMangerAdapter;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.listener.popupClickListener;
import com.itheima.mobilesafe.utils.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billow on 2016/10/8.
 */

public class AppMangerActivity extends Activity {

    public final static String TAG = "AppMangerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;
    private ListView lv_app_manger;
    private LinearLayout ll_loading;
    //启动
    private RelativeLayout ll_start;
    //分享
    private RelativeLayout ll_share;
    //卸载
    private RelativeLayout ll_uninstall;
    //应用程序的状态
    private TextView tv_status;
    private List<AppInfo> appInfos;
    private List<AppInfo> userApp;//用户app
    private List<AppInfo> systemApp;//系统app
    private Map<String, List<AppInfo>> mapAppInfo;
    //弹出窗体
    private PopupWindow popupWindow;
    private AppMangerAdapter ama;
    private AppInfo appInfo;

    public AppMangerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_app_manager);
        tv_avail_rom = (TextView) this.findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) this.findViewById(R.id.tv_avail_sd);
        lv_app_manger = (ListView) this.findViewById(R.id.lv_app_manger);
        ll_loading = (LinearLayout) this.findViewById(R.id.ll_loading);
        tv_status = (TextView) this.findViewById(R.id.tv_status);

        long sdSize = this.getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        long romSize = this.getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        tv_avail_rom.setText("手机内存:" + Formatter.formatFileSize(this, romSize));
        tv_avail_sd.setText("SD卡内存:" + Formatter.formatFileSize(this, sdSize));

        this.fillData();

        //对ListView滚动添加监听事件
        lv_app_manger.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if (userApp != null && systemApp != null) {
                    if (firstVisibleItem > userApp.size()) {
                        tv_status.setText("系统应用（" + systemApp.size() + "）");
                    } else {
                        tv_status.setText("用户应用（" + userApp.size() + "）");
                    }
                }
            }
        });

        //对ListViwe点击添加监听事件
        lv_app_manger.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//用户应用标签
                    return;
                } else if (position == (userApp.size() + 1)) {//系统应用标签
                    return;
                } else if (position <= userApp.size()) {//用户应用
                    int newposition = position - 1;
                    appInfo = userApp.get(newposition);
                } else {//系统应用
                    int newposition = position - 1 - userApp.size() - 1;
                    appInfo = systemApp.get(newposition);
                }
                dismissPopupWindow();
                //弹出窗体
                //TextView textView = new TextView(getApplicationContext());
                //textView.setTextColor(Color.BLACK);
                //textView.setText(appInfo.getName());
                View contentView = View.inflate(getApplication(), R.layout.popup_app_item, null);
                //设置动画效果
                ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(300);
                //设置透明效果
                AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
                aa.setDuration(300);
                //动画集（false：独立播放）
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(sa);
                set.addAnimation(aa);
                contentView.setAnimation(set);
                popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                //动画效果必须要有背景色
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景色为透明
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int x = DensityUtil.dip2px(getApplicationContext(), 10);
                popupWindow.showAtLocation(parent, Gravity.RIGHT | Gravity.TOP, x, location[1]);
                //初始化窗体中的配件
                ll_share = (RelativeLayout) contentView.findViewById(R.id.ll_share);
                ll_start = (RelativeLayout) contentView.findViewById(R.id.ll_start);
                ll_uninstall = (RelativeLayout) contentView.findViewById(R.id.ll_uninstall);

                //分享
                ll_share.setOnClickListener(new popupClickListener(AppMangerActivity.this, appInfo, new popupClickListener.CallBack() {
                    @Override
                    public void callBackBeforeOnClick() {
                        dismissPopupWindow();
                    }

                    @Override
                    public void callBackAfterOnClick() {

                    }
                }));

                //启动
                ll_start.setOnClickListener(new popupClickListener(AppMangerActivity.this, appInfo, new popupClickListener.CallBack() {
                    @Override
                    public void callBackBeforeOnClick() {
                        dismissPopupWindow();
                    }

                    @Override
                    public void callBackAfterOnClick() {

                    }
                }));

                //卸载
                ll_uninstall.setOnClickListener(new popupClickListener(AppMangerActivity.this, appInfo, new popupClickListener.CallBack() {
                    @Override
                    public void callBackBeforeOnClick() {
                        dismissPopupWindow();
                    }

                    @Override
                    public void callBackAfterOnClick() {
                        fillData();
                    }
                }));
            }
        });
    }

    /**
     * 获取数据
     */
    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos();
                userApp = new ArrayList<>();
                systemApp = new ArrayList<>();
                for (AppInfo info : appInfos) {
                    if (info.isUserApp()) {
                        userApp.add(info);
                    } else {
                        systemApp.add(info);
                    }
                }
                mapAppInfo = new HashMap<>();
                mapAppInfo.put("userApp", userApp);
                mapAppInfo.put("systemApp", systemApp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_app_manger.setAdapter(new AppMangerAdapter(AppMangerActivity.this, mapAppInfo));
                        ll_loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }.start();
    }

    /**
     * 关闭弹出窗体
     */
    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 获取某个目录下的可用空间
     *
     * @param path
     * @return
     */
    public long getAvailSpace(String path) {
        StatFs statF = new StatFs(path);
        return statF.getAvailableBytes();
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }
}
