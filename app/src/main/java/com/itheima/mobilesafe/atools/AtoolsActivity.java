package com.itheima.mobilesafe.atools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.ToastUtils;
import com.itheima.mobilesafe.utils.SmsUtil;

/**
 * 高级工具
 * Created by billow on 2016/8/28.
 */
public class AtoolsActivity extends Activity {

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_atools);
    }

    /**
     * 查询电话号码
     *
     * @param view
     */
    public void numberQuery(View view) {
        Intent intent = new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 短信备份
     *
     * @param view
     */
    public void smsBackup(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("短信备份中...");
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    SmsUtil.smsBackup(new SmsUtil.BackUpCallBack() {
                        @Override
                        public void beforeBackUp(int max) {
                            progressDialog.setMax(max);
                        }

                        @Override
                        public void onSmsBackUp(int progress) {
                            progressDialog.setProgress(progress);
                        }
                    });
                    //子线程中更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.toastLong("短信备份成功！");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    //子线程中更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.toastLong("短信备份失败！");
                        }
                    });
                } finally {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    /**
     * 短信还原
     *
     * @param view
     */
    public void smsRestore(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("短信还原中...");
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    SmsUtil.smsRestore(true, new SmsUtil.BackUpCallBack() {
                        @Override
                        public void beforeBackUp(int max) {
                            progressDialog.setMax(max);
                        }

                        @Override
                        public void onSmsBackUp(int progress) {
                            progressDialog.setProgress(progress);
                        }
                    });
                    //子线程中更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.toastLong("短信还原成功...");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    //子线程中更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.toastLong("短信还原出错了...");
                        }
                    });
                } finally {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }
}
