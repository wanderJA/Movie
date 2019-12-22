package com.wander.baseframe.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;

public class SystemBarUtil {


    public static final boolean IS_HUAWEI_USE_NOTCH = true;
    public static final boolean IS_VIVO_USE_NOTCH = true;
    public static final boolean IS_XIAOMI_USE_NOTCH = true;
    public static final boolean IS_OPPO_USE_NOTCH = true;


    public static void openSystemBar(Activity activity) {
        openSystemBar(activity.getWindow(), activity);
    }

    public static void openSystemBar(Window window, Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            int flag;
            if (hasNotchAtVoio(context)) {
                flag = View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag = flag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            window.getDecorView().setSystemUiVisibility(flag);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public static void hideSystemBar(Activity activity) {
        hideSystemBar(activity.getWindow(), activity);
    }

    public static void hideSystemBar(Window window, Context context) {
        DebugLog.e("UiVisibility: " + window.getDecorView().getSystemUiVisibility());
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(layoutParams);
                return;
            }
            if (hasNotchAtVoio(context)) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            } else if (hasNotchAtHuawei(context)) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                );
            } else {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }


    public static boolean isUseNotch(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                DisplayCutout cutout = ((Activity) context).getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                if (cutout != null) {
                    List<Rect> boundingRects = cutout.getBoundingRects();
                    if (boundingRects != null && boundingRects.size() != 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (hasNotchAtVoio(context) && IS_VIVO_USE_NOTCH) {
            return true;
        } else if (hasNotchAtHuawei(context) && IS_HUAWEI_USE_NOTCH) {
            return true;
        } else if (hasMIUINotchInScreen() && IS_XIAOMI_USE_NOTCH) {
            return true;
        } else if (hasOppoNotchInScreen(context) && IS_OPPO_USE_NOTCH) {
            return true;
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isNavigationBarShowing(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (defaultDisplay == null) {
            return false;
        }
        int height = defaultDisplay.getHeight();
        int width = defaultDisplay.getWidth();
        try {
            Point point = new Point();
            defaultDisplay.getRealSize(point);
            if (height > width) {
                return point.y != height;
            }
            if (point.x == width && point.y == height) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getScreenRealSize(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        return point;
    }


    /**
     * 不包括虚拟按键和状态栏，但可能包括刘海区域
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getFullScreenHeight(Activity context) {
        int height = getScreenRealSize(context).y;
        int notchHeight = 0;
        if (isUseNotch(context)) {
            notchHeight = 0;
        } else {
            notchHeight = SystemBarUtil.getNotchHeight(context);
        }

        height -= notchHeight;
        return height;
    }

    public static int getNotchHeight(Activity context) {
        int height = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return 0;
        }
        try {
            if (hasNotchAtVoio(context)) {
                height = getVivoNotchHeight(context);
            } else if (hasNotchAtHuawei(context)) {
                height = getNotchSizeAtHuawei(context)[1];
            } else if (hasMIUINotchInScreen()) {
                height = getMIUINotchSize(context);
            } else if (hasOppoNotchInScreen(context)) {
                height = getOppoNotchHeight();
            } else {
                height = getOtherAndroidPNotchHeight(context.getWindow());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return height;
    }

    public static boolean hasNotch(Activity context) {
        return hasNotch(context, context.getWindow());
    }

    public static boolean hasNotch(Context context, Window window) {
        return hasOppoNotchInScreen(context) || hasNotchAtHuawei(context)
                || hasMIUINotchInScreen() || hasNotchAtVoio(context)
                || hasNotchAtOtherAndroidP(window);
    }


    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchAtVoio(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            DebugLog.e("Notch", "hasNotchAtVoio ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            DebugLog.e("Notch", "hasNotchAtVoio NoSuchMethodException");
        } catch (Exception e) {
            DebugLog.e("Notch", "hasNotchAtVoio Exception");
        } finally {
            return ret;
        }
    }


    private static boolean hasRoundCornerAtVoio(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_FILLET);
        } catch (ClassNotFoundException e) {
            DebugLog.e("Notch", "hasNotchAtVoio ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            DebugLog.e("Notch", "hasNotchAtVoio NoSuchMethodException");
        } catch (Exception e) {
            DebugLog.e("Notch", "hasNotchAtVoio Exception");
        } finally {
            return ret;
        }
    }

    private static int getVivoNotchHeight(Context context) {
        if (hasRoundCornerAtVoio(context)) {
            return Tools.dip2px(27 + 5);
        } else {
            return Tools.dip2px(27);
        }
    }

    private static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            DebugLog.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            DebugLog.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            DebugLog.e("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    //获取刘海尺寸：width、height
    //int[0]值为刘海宽度 int[1]值为刘海高度
    private static int[] getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            DebugLog.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            DebugLog.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            DebugLog.e("Notch", "getNotchSizeAtHuawei Exception");
        } finally {
            return ret;
        }
    }


    /**
     * 只适用于判断MIUI 是否有刘海屏
     * SystemProperties.getInt("ro.miui.notch", 0) == 1;
     *
     * @return
     */
    private static boolean hasMIUINotchInScreen() {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("getInt", String.class, int.class);
            return (int) get.invoke(clz, "ro.miui.notch", 0) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取MIUI 的刘海屏高度
     *
     * @param context
     */
    private static int getMIUINotchSize(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * 是否是Oppo的刘海屏
     *
     * @param context
     * @return
     */
    private static boolean hasOppoNotchInScreen(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    private static int getOppoNotchHeight() {
        return 80; //px
    }

    private static boolean hasNotchAtOtherAndroidP(Window window) {
        //Android P版本及以上的统一适配
        return getOtherAndroidPNotchHeight(window) > 0;
    }

    private static int getOtherAndroidPNotchHeight(Window window) {
        //Android P版本及以上的统一适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout = window.getDecorView().getRootWindowInsets().getDisplayCutout();
            if (displayCutout != null) {
                return displayCutout.getSafeInsetTop();
            }
        }
        return 0;
    }
}