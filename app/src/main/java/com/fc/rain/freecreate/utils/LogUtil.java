package com.fc.rain.freecreate.utils;

import android.util.Log;

import com.fc.rain.freecreate.MyApplication;

/**
 * Created by Rain on 2017/11/23.
 */

public class LogUtil {

    private static boolean isDebug = MyApplication.Companion.isDebug();
    private static final String TAG = "Rain--->";

    public static void d(String message) {
        if (isDebug) {
            Log.d(TAG, message);
        }
    }

    public static void i(String message) {
        if (isDebug) {
            Log.i(TAG, message);
        }
    }

    public static void e(String message) {
        if (isDebug) {
            Log.e(TAG, message);
        }
    }

    public static void w(String message) {
        if (isDebug) {
            Log.w(TAG, message);
        }
    }

    public static void v(String message) {
        if (isDebug) {
            Log.v(TAG, message);
        }
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }
}
