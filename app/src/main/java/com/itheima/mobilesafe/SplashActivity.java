package com.itheima.mobilesafe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.itheima.mobilesafe.appmanger.AppMangerActivity;
import com.itheima.mobilesafe.db.DBUtils;
import com.itheima.mobilesafe.ui.ToastUtils;
import com.itheima.mobilesafe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressLint ({ "HandlerLeak", "ShowToast" })
public class SplashActivity extends Activity {

    protected static final String TAG = "SplashActivity";
    protected static final int SHOW_UPDATE_DIALOG = 0;
    protected static final int ENTER_HOME = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    private TextView tv_splash_version;
    private String description;
    private TextView tv_update_info;

    /**
     * 新版本的下载地址
     */
    private String apkurl;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号" + getVersionName());
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        boolean update = sp.getBoolean("update", false);
        //复制号码归属地离线查询的数据库到指定的位置
        DBUtils.copyDB(this, "address.db");
        if (update) {
            // 检查升级
            checkUpdate();
        } else {
            // 自动升级已经关闭
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 进入主页面
                    enterHome();
                }
            }, 2000);
        }
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(aa);

        //创建快捷方式
        //installShortCut();
    }

    /**
     * 创建快捷方式
     */
    private void installShortCut() {
        //通知创建快捷图标的意图
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //设置是否重复创建
        intent.putExtra("duplicate", false);
        //快捷方式：1.名称、2.图标、3.意图
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "软件管理");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.app));
        //启动应用的意图
        Intent shortcutIntent = new Intent();
        //AndroidManifest.xml中添加过滤和分类
        shortcutIntent.setAction("com.itheima.mobilesafe.appmanger.action.APPMANGER_ACTION");
        shortcutIntent.addCategory("android.intent.categore.DEFAULT");
        //点击图标想要启动的activity
        shortcutIntent.setClass(this, AppMangerActivity.class);

        //通知广播要去创建图标和响应程序
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        sendBroadcast(intent);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:// 显示升级的对话框
                    Log.i(TAG, "显示升级的对话框");
                    showUpdateDialog();
                    break;
                case ENTER_HOME:// 进入主页面
                    enterHome();
                    break;
                case URL_ERROR:// URL错误
                    enterHome();
                    ToastUtils.toastShort("URL错误");
                    break;
                case NETWORK_ERROR:// 网络异常
                    enterHome();
                    ToastUtils.toastShort("网络异常");
                    break;
                case JSON_ERROR:// JSON解析出错
                    enterHome();
                    ToastUtils.toastShort("JSON解析出错");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkUpdate() {

        new Thread() {
            public void run() {
                // URLhttp://192.168.1.254:8080/updateinfo.html
                Message mes = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {

                    URL url = new URL(getString(R.string.serverurl));
                    // 联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        // 联网成功
                        InputStream is = conn.getInputStream();
                        // 把流转成String
                        String result = StreamTools.readFromStream(is);
                        Log.i(TAG, "联网成功了" + result);
                        // json解析
                        JSONObject obj = new JSONObject(result);
                        // 得到服务器的版本信息
                        String version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");
                        // 校验是否有新版本
                        if (getVersionName().equals(version)) {
                            // 版本一致，没有新版本，进入主页面
                            mes.what = ENTER_HOME;
                        } else {
                            // 有新版本，弹出一升级对话框
                            mes.what = SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    mes.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    mes.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mes.what = JSON_ERROR;
                } finally {
                    long endTime = System.currentTimeMillis();
                    // 我们花了多少时间
                    long dTime = endTime - startTime;
                    // 2000
                    if (dTime < 2000) {
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(mes);
                }
            }

            ;
        }.start();
    }

    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
        // this = Activity.this
        AlertDialog.Builder builder = new Builder(SplashActivity.this);
        builder.setTitle("提示升级");
        // builder.setCancelable(false);//强制升级
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 进入主页面
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(description);
        builder.setPositiveButton("立刻升级", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载APK，并且替换安装
                // 判断sdcard是否存在
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // afnal，开源框架
                    FinalHttp finalhttp = new FinalHttp();
                    finalhttp.download(apkurl, Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            t.printStackTrace();
                            ToastUtils.toastLong("下载失败");
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            tv_update_info.setVisibility(View.VISIBLE);
                            // 当前下载百分比
                            int progress = (int) (current * 100 / count);
                            tv_update_info.setText("下载进度：" + progress + "%");
                        }

                        @Override
                        public void onSuccess(File t) {
                            super.onSuccess(t);
                            installAPK(t);
                        }

                        /**
                         * 安装APK
                         *
                         * @param t
                         */
                        private void installAPK(File t) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(t),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    });
                } else {
                    ToastUtils.toastShort("没有sdcard，请安装上在试");
                    return;
                }
            }
        });
        builder.setNegativeButton("下次再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();// 进入主页面
            }
        });
        builder.show();
    }

    protected void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        // 关闭当前页面
        finish();
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName() {
        // 用来管理手机的APK
        PackageManager pm = getPackageManager();
        try {
            // 得到知道APK的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
