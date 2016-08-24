package com.itheima.mobilesafe.lastfind.pagesub;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 用于手势控制
 * Created by billow on 2016/8/21.
 */
public abstract class BaseSetupActivity extends Activity {

    //手势识别器
    public GestureDetector detector;

    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 手势滑动
             * @param e1 初始触摸位
             * @param e2 结束触摸位
             * @param velocityX X轴方向的速度
             * @param velocityY Y轴方向的速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if ((e2.getRawX() - e1.getRawX()) > 200) {
                    //上一个页面
                    prev(null);
                    return true;
                } else if ((e1.getRawX() - e2.getRawX()) > 200) {
                    //下一个页面
                    next(null);
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 使用手势
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 下一个页面
     *
     * @param view
     */
    public abstract void next(View view);

    /**
     * 上一个页面
     *
     * @param view
     */
    public abstract void prev(View view);
}
