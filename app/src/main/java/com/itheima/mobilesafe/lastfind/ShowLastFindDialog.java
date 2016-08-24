package com.itheima.mobilesafe.lastfind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.MD5Utils;

/**
 * 手机防盗页面的密码设置
 * <p/>
 * Created by billow on 2016/8/20.
 */
public class ShowLastFindDialog {

    private EditText tx_setup_pwd;
    private EditText tx_setup_confrim;
    private Button ok;
    private Button cancel;
    private AlertDialog alertDialog;

    public ShowLastFindDialog(Activity activity, SharedPreferences sp) {
        this.showLastFindDialog(activity, sp);
    }

    /**
     * 点击手机防盗
     */
    protected void showLastFindDialog(Activity activity, SharedPreferences sp) {
        if (isSetupPwd(sp)) {
            showInputPwdDialog(activity, sp);
        } else {
            showSetupPwdDialog(activity, sp);
        }
    }

    /**
     * 显示设置密码的dialog
     */
    private void showSetupPwdDialog(final Activity activity, final SharedPreferences sp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_setup_pwd, null);
        builder.setView(view);
        alertDialog = builder.show();
        tx_setup_pwd = (EditText) view.findViewById(R.id.tx_setup_pwd);
        tx_setup_confrim = (EditText) view.findViewById(R.id.tx_setup_confrim);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        // 点击取消关闭对话框
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        // 点击确定
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = tx_setup_pwd.getText().toString().trim();
                String confrimPwd = tx_setup_confrim.getText().toString().trim();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confrimPwd)) {
                    Toast.makeText(activity, "密码和确认密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confrimPwd)) {
                    Toast.makeText(activity, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 保存密码到SharedPreferences中
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("password", MD5Utils.md5Password(password));
                edit.commit();
                alertDialog.dismiss();
                Intent intent = new Intent(activity, LastFindActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 显示输入密码的dialog
     */
    private void showInputPwdDialog(final Activity activity, final SharedPreferences sp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity, R.layout.dialog_input_pwd, null);
        builder.setView(view);
        // alertDialog = builder.show();
        // 为了兼容低版本的--start
        alertDialog = builder.create();
        // 设置布局文件充满整个AlertDialog
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
        // 为了兼容低版本的--end
        tx_setup_pwd = (EditText) view.findViewById(R.id.tx_setup_pwd);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        // 点击取消关闭对话框
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        // 点击确定
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = MD5Utils.md5Password(tx_setup_pwd.getText().toString().trim());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(activity, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 取出SharedPreferences中的密码
                String psPassword = sp.getString("password", "");
                if (!password.equals(psPassword)) {
                    Toast.makeText(activity, "密码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Toast.makeText(activity, "密码正确，进入手机防盗！", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Intent intent = new Intent(activity, LastFindActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 检查是否设置过密码
     *
     * @return true 设置过;false 未设置
     */
    private boolean isSetupPwd(SharedPreferences sp) {
        String password = sp.getString("password", null);
        return !TextUtils.isEmpty(password);
    }

}
