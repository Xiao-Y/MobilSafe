package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
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

    private static WindowManager.LayoutParams params;
    private static SharedPreferences sp;
    private static View view;

    /**
     * @param wm      窗体管理者
     * @param message 要显示的信息
     */
    public static void myToast(final WindowManager wm, View w, String message) {
        view = w;
        Context context = MyApplication.getContext();
        view = View.inflate(context, R.layout.address_show, null);

        final long[] mHits = new long[2];
        //双击居中
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits, 1, mHits, 0, 1);
                //获取CPU运行的时间
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    DisplayMetrics metric = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metric);
                    int widthPixels = metric.widthPixels;
                    int width = view.getWidth();
                    params.x = (widthPixels - width) / 2;
                    //更新Toast的位置
                    wm.updateViewLayout(view, params);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("lastx", params.x);
                    edit.putInt("lasty", params.y);
                    edit.commit();
                }
            }
        });

        //触摸移动
        view.setOnTouchListener(new View.OnTouchListener() {
            //Toast的初始位置
            float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指按下屏幕时
                        startX = motionEvent.getRawX();
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://手指按下屏幕移动时
                        //获取新的位置
                        float newX = motionEvent.getRawX();
                        float newY = motionEvent.getRawY();
                        //计算偏移量
                        float dy = newY - startY;
                        float dx = newX - startX;
                        params.x += dx;
                        params.y += dy;
                        //考虑边界值问题
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        DisplayMetrics metric = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(metric);
                        int widthPixels = metric.widthPixels;
                        int heightPixels = metric.heightPixels;
                        int width = view.getWidth();
                        int height = view.getHeight();
                        int dWidth = widthPixels - width;
                        int dHeight = heightPixels - height;
                        if (params.x > dWidth) {
                            params.x = dWidth;
                        }
                        if (params.y > dHeight) {
                            params.y = dHeight;
                        }

                        //更新Toast的位置
                        wm.updateViewLayout(view, params);
                        //重新获取现在的位置
                        startX = motionEvent.getRawX();
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP://手指离开屏幕时
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("lastx", params.x);
                        edit.putInt("lasty", params.y);
                        edit.commit();
                        break;
                }
                return false;//返回true时，事件处理完毕，后面的事件不会再执行。根据需求返回值
            }
        });

        TextView textview = (TextView) view.findViewById(R.id.tv_address);
        //"半透明","活力橙","卫士蓝","金属灰","苹果绿"
        int[] ids = { R.drawable.call_locate_white, R.drawable.call_locate_orange, R.drawable.call_locate_blue
                , R.drawable.call_locate_gray, R.drawable.call_locate_green };
        sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        view.setBackgroundResource(ids[sp.getInt("which", 0)]);
        textview.setText(message);
        //窗体的参数就设置好了
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        //初始化位置（左上角，距上、左100像素）
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = sp.getInt("lastx", 100);
        params.y = sp.getInt("lasty", 100);

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        // params.type = WindowManager.LayoutParams.TYPE_TOAST;
        //android系统里面具有电话优先级的一种窗体类型，记得添加权限
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
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
