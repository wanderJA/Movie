package com.wander.baseframe.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.wander.baseframe.BaseApp;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {
    public static Resources getResources() {
        return BaseApp.getInstance().getResources();
    }

    public static String getString(int id) {
        return BaseApp.getInstance().getResources().getString(id);
    }

    public static String getString(int resId, Object... formatArgs) {
        return BaseApp.getInstance().getResources().getString(resId, formatArgs);
    }

    public static int getDimen(int id) {
        return BaseApp.getInstance().getResources().getDimensionPixelOffset(id);
    }

    public static Drawable getDrawable(int id) {
        return BaseApp.getInstance().getResources().getDrawable(id);
    }

    public static int getColor(int id) {
        return BaseApp.getInstance().getResources().getColor(id);
    }

    public static InputStream getAssets(String fileName) throws IOException {
        return BaseApp.getInstance().getResources().getAssets().open(fileName);
    }

    public static ColorStateList getColorStateList(int id) {
        return BaseApp.getInstance().getResources().getColorStateList(id);
    }

    public static String[] getStringArray(int id) {
        return BaseApp.getInstance().getResources().getStringArray(id);
    }

    public static int[] getIntArray(int id) {
        return BaseApp.getInstance().getResources().getIntArray(id);
    }

    public static TypedArray getTypedArray(int id) {
        return BaseApp.getInstance().getResources().obtainTypedArray(id);
    }

    public static int getDimensionPixelOffset(int id) {
        return BaseApp.getInstance().getResources().getDimensionPixelOffset(id);
    }

    public static int getInteger(int id) {
        return BaseApp.getInstance().getResources().getInteger(id);
    }
}
