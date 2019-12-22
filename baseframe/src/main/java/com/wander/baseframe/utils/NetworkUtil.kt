package com.wander.baseframe.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.wander.baseframe.BaseApp
import java.net.Inet4Address
import java.net.NetworkInterface

object NetworkUtil {

    val isNetworkAvailable: Boolean
        get() = isNetworkAvailable(BaseApp.getInstance())

    val isWifi: Boolean
        get() = isWifi(BaseApp.getInstance())


    val commonRequestParam: ParamMap
        get() = ParamMap()

    val operatorName: String
        get() {
            try {
                val tm =
                    BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return tm.networkOperatorName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "unknown"
        }


    val netTypeName: String
        get() {
            try {
                val tm =
                    BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                val connectivityManager =
                    BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo == null || !netInfo.isConnected) {
                    return "disconnect"
                } else if (netInfo.type == ConnectivityManager.TYPE_WIFI) {
                    return "WIFI"
                } else if (netInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    val networkType = tm.networkType
                    when (networkType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return "2G"
                        TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return "3G"
                        TelephonyManager.NETWORK_TYPE_LTE -> return "4G"
                        else -> return "mobile"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "unknown"
        }

    val netWorkType: String
        get() = getNetWorkType(BaseApp.getInstance())

    /**
     * 获取当前的运营商
     *
     * @return 运营商名字
     */
    val operator: String
        get() {
            try {
                val telephonyManager =
                    BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (telephonyManager != null) {
                    return telephonyManager.simOperatorName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "没有获取到sim卡信息"
        }

    /**
     * @return
     * @Description 获取手机网络ip地址
     */
    val phoneIp: String
        get() {
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en
                        .hasMoreElements()
                ) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr
                            .hasMoreElements()
                    ) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress().toString()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    fun isNetworkAvailable(context: Context? = BaseApp.getInstance()): Boolean {

        val connectivity =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivity != null) {
            val info = connectivity.activeNetworkInfo
            return info != null && info.isConnectedOrConnecting
        }
        return false
    }

    fun isWifi(context: Context): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            if (activeNetInfo != null && activeNetInfo.isAvailable) {
                return activeNetInfo.type == ConnectivityManager.TYPE_WIFI
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun isMobile(context: Context): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            if (activeNetInfo != null && activeNetInfo.isAvailable) {
                return activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }


    fun getAvailableNetWorkInfo(context: Context): NetworkInfo? {

        try {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return if (activeNetInfo != null && activeNetInfo.isAvailable) {
                activeNetInfo

            } else {
                null
            }

        } catch (e: Exception) {

            return null
        }

    }

    fun getNetWorkType(context: Context?): String {
        if (context == null) {
            return "-1"
        }

        var netWorkType = "-1"
        val netWorkInfo = getAvailableNetWorkInfo(context)

        if (netWorkInfo != null) {
            if (netWorkInfo.type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "1"
            } else if (netWorkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                val telephonyManager =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                when (telephonyManager.networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS -> netWorkType = "2"
                    TelephonyManager.NETWORK_TYPE_EDGE -> netWorkType = "3"
                    TelephonyManager.NETWORK_TYPE_UMTS -> netWorkType = "4"
                    TelephonyManager.NETWORK_TYPE_HSDPA -> netWorkType = "5"
                    TelephonyManager.NETWORK_TYPE_HSUPA -> netWorkType = "6"
                    TelephonyManager.NETWORK_TYPE_HSPA -> netWorkType = "7"
                    TelephonyManager.NETWORK_TYPE_CDMA -> netWorkType = "8"
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> netWorkType = "9"
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> netWorkType = "10"
                    TelephonyManager.NETWORK_TYPE_1xRTT -> netWorkType = "11"

                    TelephonyManager.NETWORK_TYPE_LTE -> netWorkType = "14"

                    else -> netWorkType = "-1"
                }
            }
        }

        return netWorkType
    }

}
