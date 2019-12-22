package com.wander.baseframe;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;

public class AndroidUtilities {
    public static Context applicationContext;
    public static volatile Handler applicationHandler;

    public AndroidUtilities() {
    }

    public static void onCreate(Context context) {
        applicationContext = context;
        applicationHandler = new Handler(applicationContext.getMainLooper());
    }


    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0L);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0L) {
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }

    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        applicationHandler.removeCallbacks(runnable);
    }

    public static File getCacheDir() {
        String state = null;

        try {
            state = Environment.getExternalStorageState();
        } catch (Exception var4) {
            Log.e("AndroidUtilities", "" + var4);
        }

        File e;
        if (state == null || state.startsWith("mounted")) {
            try {
                e = applicationContext.getExternalCacheDir();
                if (e != null) {
                    return e;
                }
            } catch (Exception var3) {
                Log.e("AndroidUtilities", "" + var3);
            }
        }

        try {
            e = applicationContext.getCacheDir();
            if (e != null) {
                return e;
            }
        } catch (Exception var2) {
            Log.e("AndroidUtilities", "" + var2);
        }

        return new File("");
    }
}
