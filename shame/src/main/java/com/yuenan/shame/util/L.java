package com.yuenan.shame.util;

import android.util.Log;


/**
 * 日志Log管理类
 * Created by Liuhuacheng.
 * Created time 17/10/20
 * 使用 compile 'com.orhanobut:logger:1.8'
 */

public class L {
    private static final String TAG = "L";
    public static boolean isDebug = true;

    public static void i(String message) {
        if (isDebug) {
            Log.i(TAG, " wenbaMsg*****" + message);
        }
    }

    public static void d(String message) {
        if (isDebug) {
            Log.d(TAG, " wenbaMsg*****" + message);
        }
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(TAG, tag + " wenbaMsg*****" + message);
        }
    }

    public static void e(String message) {
        if (isDebug) {
            Log.e(TAG, " wenbaMsg*****" + message);
        }
    }

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(TAG, tag + " wenbaMsg*****" + message);
        }
    }

    public static void e(String tag, Exception e) {
        if (isDebug) {
            Log.e(TAG, tag + " wenbaMsg*****" + e.getMessage());
        }
    }

    public static void w(Throwable e) {
        if (isDebug) {
            Log.w(TAG, e.getMessage());
        }
    }

}
