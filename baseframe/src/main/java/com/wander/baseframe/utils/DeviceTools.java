package com.wander.baseframe.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.wander.baseframe.BaseApp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 获取设备相关信息的工具类,从PPTools中提取出来
 *
 * @author Kevin
 * @version V1.0
 * @Date 3/8/16
 * @Description
 */
public class DeviceTools {

    private static String OPEN_UDID = "";
    private static String DEVICE_ID = "";
    private static String SERIAL_NUMBER = "";
    private static String IMEI = null;

    public static String getDeviceID(Context context) {
        return getIMEISameAsBaseLine(context);
    }


    private static String generateOPenUDID(Context context) {
        if (OPEN_UDID == null || OPEN_UDID.isEmpty()) {
            OPEN_UDID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (OPEN_UDID == null || OPEN_UDID.equals("9774d56d682e549c") || OPEN_UDID.length() < 15) {
                OPEN_UDID = generateSecureRandomString();
            }
        }
        return OPEN_UDID == null ? generateSecureRandomString() : OPEN_UDID;
    }

    private static String generateSecureRandomString() {
        final SecureRandom random = new SecureRandom();
        return new BigInteger(64, random).toString(16);
    }

    /**
     * 优先级 :IMEI > md5(macaddress) > openuuid
     */
    private static String getIMEISameAsBaseLine(Context _mContext) {
        //判断imei缓存是否为空，不为空直接返回。
        if (!isEmptySameAsBaseLine(IMEI)) {
            return IMEI;
        }
        String IMEI = "";
        if (_mContext != null) {
            IMEI = getDeviceIdSameAsBaseLine(_mContext);
            if (!isEmptySameAsBaseLine(IMEI) && !"0".equals(IMEI)) {
                DeviceTools.IMEI = IMEI;
                return IMEI;
            }
        }

        WifiManager wifiManager = (WifiManager) _mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (null != wifiInfo) {
            String macAddress = wifiInfo.getMacAddress();
            if (!isEmptySameAsBaseLine(macAddress)) {
                IMEI = md5SameAsBaseLine(macAddress);
            }
        }
        //判断通过iwifimanager取得的imei是否为空，不为空则进行缓存。
        if (!isEmptySameAsBaseLine(IMEI)) {
            DeviceTools.IMEI = IMEI;
        }
        if (!isEmptySameAsBaseLine(IMEI)) {
            IMEI = "";
            DeviceTools.IMEI = IMEI;
        }
        return IMEI;
    }

    //返回IMEI或者空串
    private static String getDeviceIdSameAsBaseLine(Context context) {
        String deviceId = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 29) {
                deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                // request old storage permission
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return deviceId;
                }
                deviceId = tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return deviceId;
        }
        return deviceId;
    }

    private static boolean isEmptySameAsBaseLine(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        } else {
            if (str.length() > 4) {
                return false;
            } else {
                return str.equalsIgnoreCase("null");
            }

        }
    }

    /**
     * md5
     *
     * @param str
     * @return
     */
    private static String md5SameAsBaseLine(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private static void getSimSerialNumber(Context context) {
        if (!SERIAL_NUMBER.isEmpty()) {
            return;
        }

        boolean hasPermission = context.checkPermission(Manifest.permission.READ_PHONE_STATE,
                android.os.Process.myPid(),
                android.os.Process.myUid())
                == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            SERIAL_NUMBER = tm.getSimSerialNumber();
            return;
        }
        SERIAL_NUMBER = generateSecureRandomString();
    }

    public static String encodeMD5(String str) {
        byte[] buffBytes = encodeMD5Byte(str);
        if (buffBytes == null) {
            return "";
        } else {
            StringBuilder encrypt = new StringBuilder();

            for (int i = 0; i < buffBytes.length; ++i) {
                if ((buffBytes[i] & 255) < 16) {
                    encrypt.append("0");
                }

                encrypt.append(Long.toString((long) (buffBytes[i] & 255), 16));
            }

            return encrypt.toString();
        }
    }

    public static byte[] encodeMD5Byte(String str) {
        if (str == null) {
            return null;
        }
        byte[] strByte = str.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strByte);
            byte[] newByte = md.digest();
            return newByte;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDisplayMessage() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }

    public static int getDisplayWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDisplayHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static String getIMEI(Context context) {
        //获取imei
        String result = IMEI;
        if (!isEmptySameAsBaseLine(IMEI)) {
            return IMEI;
        }
        if (context != null) {
            IMEI = getDeviceIdSameAsBaseLine(context);
            if (!isEmptySameAsBaseLine(IMEI) && !"0".equals(IMEI)) {
                return IMEI;
            }
        }
        return IMEI;
    }


    public static int translate(float one) {
        return (int) (getDisplayWidth() / 750f * one);
    }

    /**
     * 根据包名判断某个app是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = BaseApp.getInstance().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
