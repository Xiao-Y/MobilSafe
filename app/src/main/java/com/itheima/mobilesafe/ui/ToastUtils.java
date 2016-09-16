package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.MyApplication;

/**
 * 自定义Toast
 * Created by billow on 2016/9/16.
 */
public class ToastUtils {

    /**
     * @param wm      窗体管理者
     * @param message 要显示的信息
     */
    public static void myToast(WindowManager wm, View view, String message) {
        Context context = MyApplication.getContext();
        view = View.inflate(context, R.layout.address_show, null);
        TextView textview = (TextView) view.findViewById(R.id.tv_address);
        //"半透明","活力橙","卫士蓝","金属灰","苹果绿"
        int[] ids = { R.drawable.call_locate_white9, R.drawable.call_locate_orange9, R.drawable.call_locate_blue9
                , R.drawable.call_locate_gray9, R.drawable.call_locate_green9 };
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        view.setBackgroundResource(ids[sp.getInt("which", 0)]);
        textview.setText(message);
        //窗体的参数就设置好了
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        wm.addView(view, params);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void toastLong(String message) {
        Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void toastShort(String message) {
        Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
