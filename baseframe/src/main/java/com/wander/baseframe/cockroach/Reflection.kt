//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wander.baseframe.cockroach

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build.VERSION

class Reflection {
    companion object {
        private var UNKNOWN: Int = 0
        private val ERROR_SET_APPLICATION_FAILED = -20
        private var unsealed: Int = 0

        private external fun unsealNative(var0: Int): Int

        @SuppressLint("SoonBlockedPrivateApi")
        fun unseal(context: Context?): Int {
            if (VERSION.SDK_INT < 28) {
                return 0
            } else if (context == null) {
                return -10
            } else {
                val applicationInfo = context.applicationInfo
                val targetSdkVersion = applicationInfo.targetSdkVersion
                val var3 = Reflection::class.java
                synchronized(Reflection::class.java) {
                    if (unsealed == UNKNOWN) {
                        unsealed = unsealNative(targetSdkVersion)
                        if (unsealed >= 0) {
                            try {
                                val setHiddenApiEnforcementPolicy =
                                    ApplicationInfo::class.java.getDeclaredMethod(
                                        "setHiddenApiEnforcementPolicy",
                                        Integer.TYPE
                                    )
                                setHiddenApiEnforcementPolicy.invoke(applicationInfo, 0)
                            } catch (var6: Throwable) {
                                var6.printStackTrace()
                                unsealed = -20
                            }

                        }
                    }
                }

                return unsealed
            }
        }

        init {
            System.loadLibrary("free-reflection")
            UNKNOWN = -9999
            unsealed = UNKNOWN
        }
    }
}
