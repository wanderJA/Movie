package com.wander.baseframe.context

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.FileProvider
import com.facebook.common.util.ByteConstants
import com.wander.baseframe.BaseApp
import com.wander.baseframe.BuildConfig
import com.wander.baseframe.library.fresco.ReadBitmapMemoryCacheParams
import com.wander.baseframe.utils.*
import io.reactivex.plugins.RxJavaPlugins
import java.io.File

/**
 * Created by wander on 2017/3/15.
 */

object AppContext {
    private val TAG = "AppContext"
    private var sInited = false
    var DEVICE_ID = "000000"
    var SCREEN_WIDTH = 1080
    var SCREEN_HEIGHT = 1920
    var STATUS_BAR_HEIGHT = 60
    var mBrand = ""
    var systemStorageLow = false
    var isOpenGLEnable = true
    var mChannel = "vipd"


    /**
     * 是否打开调试   测试时使用
     */
    val isLog = BuildConfig.DEBUG

    val CUSTOM_SERVER = "CUSTOM_SERVER"
    val SERVER_TYPE = "SERVER_TYPE"
    /**
     * 存不同网络类型，不直接存网络地址,减少局部变量的判断，，，
     * 且线上用户不会牵涉其他逻辑
     */
    var netType = if (isLog) PreferenceTool.loadPrefInt(SERVER_TYPE, 0) else 1
    /**
     * 未打开debug开关或主动切换为线上环境
     */
    var isRelease = !isLog || netType == 1
    var SCREEN_HEIGHT_PRE = "SCREEN_HEIGHT_PRE"
    var SCREEN_WIDTH_PRE = "SCREEN_WIDTH_PRE"
    var versionName = "1.0.0"


    @Synchronized
    fun init(context: Context): Boolean {
        if (sInited) {
            return true
        } else {
            try {
                val width: Int
                val height: Int
                val wm1 = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (wm1 != null) {
                    val display = wm1.defaultDisplay
                    val point = Point()
                    display.getSize(point)
                    width = point.x
                    height = point.y
                    val displayMetrics = DisplayMetrics()
                    display.getMetrics(displayMetrics)
                    DebugLog.i(
                        TAG,
                        String.format(
                            "Screen density: %s\t fond scaledDensity:%s",
                            displayMetrics.density,
                            displayMetrics.scaledDensity
                        )
                    )
                } else {
                    val displayMetrics = context.resources.displayMetrics
                    width = displayMetrics.widthPixels
                    height = displayMetrics.heightPixels
                }
                SCREEN_HEIGHT = PreferenceTool.get(SCREEN_HEIGHT_PRE, 0)
                SCREEN_WIDTH = PreferenceTool.get(SCREEN_WIDTH_PRE, 0)
                //用户调整分辨率，不包含虚拟按键的隐藏(为了保证小米全面屏获取高度不准确的问题，每次进入阅读器重新刷新)
                if (SCREEN_WIDTH != width && SCREEN_HEIGHT != height) {
                    SCREEN_WIDTH = if (width == 0) 1080 else width
                    SCREEN_HEIGHT = if (height == 0) 1920 else height
                    PreferenceTool.put(SCREEN_HEIGHT_PRE, SCREEN_HEIGHT)
                    PreferenceTool.put(SCREEN_WIDTH_PRE, SCREEN_WIDTH)
                }
                DebugLog.i(
                    "AppContext",
                    String.format("Screen width: %s  height: %s", SCREEN_WIDTH, SCREEN_HEIGHT)
                )
                val packageManager = context.packageManager
                val applicationInfo = packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                mChannel = applicationInfo.metaData.getString("UMENG_CHANNEL") ?: "vipd"
                RxJavaPlugins.setErrorHandler { throwable -> throwable.printStackTrace() }

                //                PackageManager packageManager = context.getPackageManager();
                //                PackageInfo pi1 = packageManager.getPackageInfo(context.getPackageName(), 0);
                //                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

                DEVICE_ID = DeviceTools.getDeviceID(BaseApp.getInstance())
                DebugLog.i("AppContext", "DEVICE_ID:$DEVICE_ID")
                val statusBarHeight = ImmersionBar.getStatusBarHeight(context.resources)
                STATUS_BAR_HEIGHT =
                    if (statusBarHeight > 0) statusBarHeight else Tools.dip2px(context, 20f)
            } catch (var5: Exception) {
                DebugLog.printStackTrace(var5)
                return false
            } finally {
                mBrand = Build.BRAND
                if (isLog) {
                    DebugLog.i("AppContext", "MODEL: " + Build.MODEL)
                    DebugLog.i("AppContext", "BOARD: " + Build.BOARD)
                    DebugLog.i("AppContext", "BRAND: $mBrand")
                    DebugLog.i("AppContext", "DEVICE: " + Build.DEVICE)
                    DebugLog.i("AppContext", "PRODUCT: " + Build.PRODUCT)
                    DebugLog.i("AppContext", "DISPLAY: " + Build.DISPLAY)
                    DebugLog.i("AppContext", "HOST: " + Build.HOST)
                    DebugLog.i("AppContext", "ID: " + Build.ID)
                    DebugLog.i("AppContext", "USER: " + Build.USER)
                    DebugLog.i(
                        TAG,
                        "fresco memory:" + ReadBitmapMemoryCacheParams(context).getMaxCacheSize() / ByteConstants.MB + " MB"
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val s = StringBuilder()
                        for (abi in Build.SUPPORTED_ABIS) {
                            s.append(abi).append("\t")
                        }
                        DebugLog.i(TAG, "CPU_API: $s")
                    } else {
                        DebugLog.i(TAG, "CPU_API: " + Build.CPU_ABI)
                    }
                }
                //                loadAppUid();
            }

            sInited = true
            return true
        }
    }

    fun updateHeightAndWidth(height: Int, width: Int) {
        if (SCREEN_HEIGHT != height) {
            SCREEN_HEIGHT = height
            PreferenceTool.put(SCREEN_HEIGHT_PRE, SCREEN_HEIGHT)
        }
        if (SCREEN_WIDTH != width) {
            SCREEN_WIDTH = width
            PreferenceTool.put(SCREEN_WIDTH_PRE, SCREEN_WIDTH)
        }
    }

    /**
     * 7.0以后  转换为content访问方式
     *
     * @return
     */
    fun getFileProviderUri(file: File): Uri {
        return FileProvider.getUriForFile(
            BaseApp.getInstance(),
            BuildConfig.APPLICATION_ID + ".fileprovider",
            file.absoluteFile
        )
    }


    fun checkPermission(context: Context?, permission: String): Boolean {
        try {

            if (context == null) {
                return false
            } else if (TextUtils.isEmpty(permission)) {
                return false
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission == Manifest.permission.SYSTEM_ALERT_WINDOW) {
                if (Settings.canDrawOverlays(context)) {
                    return true
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission == Manifest.permission.WRITE_SETTINGS) {
                if (Settings.System.canWrite(context)) {
                    return true
                }
            } else {
                val var2 = context.packageManager
                return var2.checkPermission(
                    permission,
                    context.packageName
                ) == PackageManager.PERMISSION_GRANTED
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}
