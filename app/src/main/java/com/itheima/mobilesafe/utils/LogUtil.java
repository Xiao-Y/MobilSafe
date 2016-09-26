package com.itheima.mobilesafe.utils;

import android.util.Log;

/**
 * 日志工具类
 * Created by billow on 2016/10/1.
 */

public class LogUtil {

    public final static int VERBOSE = 1;
    public final static int DEBUGGER = 2;
    public final static int INFO = 3;
    public final static int WARN = 4;
    public final static int REEOR = 5;
    //不打印信息（LEVEL = NOTHING）
    public final static int NOTHING = 6;
    public final static int LEVEL = VERBOSE;

    /**
     * 最低级别的信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * debugger级别的信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUGGER) {
            Log.d(tag, msg);
        }
    }

    /**
     * 常规级别的信息
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 警告级别的信息
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 错误级别的信息
     *
     * @param tag
     * @param msg
     */
    public static void r(String tag, String msg) {
        if (LEVEL <= REEOR) {
            Log.e(tag, msg);
        }
    }
}
