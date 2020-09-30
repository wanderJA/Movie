/***
 This is free and unencumbered software released into the public domain.

 Anyone is free to copy, modify, publish, use, compile, sell, or
 distribute this software, either in source code form or as a compiled
 binary, for any purpose, commercial or non-commercial, and by any
 means.

 For more information, please refer to <http://unlicense.org/>
 */

package com.wander.baseframe.utils;

import android.text.TextUtils;
import android.util.Log;

import com.wander.baseframe.BuildConfig;
import com.wander.baseframe.context.AppContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * @author Mustafa Ferhan Akman
 * <p/>
 * Create a simple and more understandable Android logs.
 * @date 21.06.2012
 */

public class DebugLog {
    private static String TAG = "DebugLog";
    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable()) return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void e(String tag, String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        if (!TextUtils.isEmpty(tag)) {
            Log.e(tag, createLog(message));
        } else {
            Log.e(className, createLog(message));
        }
    }

    public static void i(String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void i(String tag, String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        if (!TextUtils.isEmpty(tag)) {
            Log.i(tag, createLog(message));
        } else {
            Log.i(className, createLog(message));
        }
    }

    public static void d(String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void d(String tag, String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        if (!TextUtils.isEmpty(tag)) {
            Log.d(tag, createLog(message));
        } else {
            Log.d(className, createLog(message));
        }
    }

    public static void v(String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void v(String tag, String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        if (!TextUtils.isEmpty(tag)) {
            Log.v(tag, createLog(message));
        } else {
            Log.v(className, createLog(message));
        }
    }

    public static void w(String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable()) return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }


    /**
     * 格式化输出log，主要避免大量使用+连接String的情况
     */
    public static void formatLog(String tag, String format, Object... args) {
        if (!isDebuggable()) return;
        try {
            String msg = (args == null) ? format : String.format(Locale.US, format, args);
            d(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String splice(Object... msg) {
        if (!AppContext.INSTANCE.isLog()) {
            return "";
        }
        if (msg == null) {
            return "-------------------------  log is null ---------------------------";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object m : msg) {
            stringBuilder.append(m).append("\t");
        }
        return stringBuilder.toString();
    }


    public static String getPrintStackStr(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    public static void printStackTrace(Throwable var5) {
        e(TAG, getPrintStackStr(var5));
    }
}
