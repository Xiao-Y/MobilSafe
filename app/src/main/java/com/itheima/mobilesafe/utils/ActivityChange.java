package com.itheima.mobilesafe.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.itheima.mobilesafe.R;

import java.util.Objects;

/**
 * Activity切换动画
 * Created by billow on 2016/8/21.
 */
public class ActivityChange {

    /**
     * 点击上一步的标志
     */
    public static String NEXT = "next";
    /**
     * 点击下一步的标志
     */
    public static String PREV = "prev";

    /**
     * activity切换的动画效果
     *
     * @param activity  当前的Activity
     * @param clazz     前往下一个Activity
     * @param view      点击事件的View
     * @param operation 操作next或prev
     */
    public static void activityChange(Activity activity, Class clazz, View view, String operation) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        activity.finish();
        if (Objects.equals(NEXT, operation)) {
            activityNextChange(activity);
        } else if (Objects.equals(PREV, operation)) {
            activityPrevChange(activity);
        }
    }

    /**
     * 点击上一个
     *
     * @param activity 当前的Activity
     */
    private static void activityPrevChange(Activity activity) {
        //方法要在startActivity或finish后面执行(动画效果)
        activity.overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    /**
     * 点击下一个
     *
     * @param activity 当前的Activity
     */
    private static void activityNextChange(Activity activity) {
        //方法要在startActivity或finish后面执行(动画效果)
        activity.overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }
}
