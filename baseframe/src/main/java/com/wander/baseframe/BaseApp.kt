package com.wander.baseframe

import androidx.multidex.MultiDexApplication
import com.wander.baseframe.cockroach.Cockroach
import com.wander.baseframe.cockroach.CrashLog
import com.wander.baseframe.cockroach.ExceptionHandler
import com.wander.baseframe.utils.ToastUtils
import org.jetbrains.anko.runOnUiThread
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * author wander
 * date 2019/7/14
 *
 */
open class BaseApp : MultiDexApplication() {
    protected val tag = javaClass.simpleName

    companion object {
        @JvmStatic
        lateinit var mApp: BaseApp

        @JvmStatic
        fun getInstance(): BaseApp {
            return mApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        install()
        mApp = this
        BaseSdkInit.initOnAppCreate(this)
    }


    private fun install() {
        val sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler()
        Cockroach.install(this, object : ExceptionHandler() {
            override fun onUncaughtExceptionHappened(thread: Thread, throwable: Throwable) {
                throwable.printStackTrace()
                CrashLog.saveCrashLog(applicationContext, throwable)
                runOnUiThread {
                    ToastUtils.showToast("未知异常：${throwable.message}")
                }
            }

            protected override fun onBandageExceptionHappened(throwable: Throwable) {
                Timber.e("Cockroach Worked:${throwable.message}")
                CrashLog.saveCrashLog(applicationContext, throwable)
                throwable.printStackTrace()//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
            }

            override fun onEnterSafeMode() {
                Timber.e("-->onEnterSafeMode")
            }

            override fun onMayBeBlackScreen(e: Throwable) {
                Timber.e("--->onUncaughtExceptionHappened:${e.message}<---")
                e.printStackTrace()
                CrashLog.saveCrashLog(applicationContext, e)
                //黑屏时建议直接杀死app
                exitProcess(0)
            }

        })

    }
}