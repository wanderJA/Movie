package com.wander.baseframe

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.wander.baseframe.cockroach.Cockroach
import com.wander.baseframe.cockroach.CrashLog
import com.wander.baseframe.cockroach.ExceptionHandler
import com.wander.baseframe.context.AppContext
import com.wander.baseframe.context.CrashMain
import com.wander.baseframe.context.NeverCrash
import com.wander.baseframe.utils.DebugLog
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
//        install()
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

            override fun onBandageExceptionHappened(throwable: Throwable) {
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

    var mCurrentActivity: Activity? = null
    var isAppInBackground = false
    private var lastCrashTime: Long = 0
    private fun initCrash() {

        NeverCrash.init(object : NeverCrash.CrashHandler {
            override fun uncaughtException(t: Thread?, e: Throwable?) {
                if (System.currentTimeMillis() - lastCrashTime < 1000) {
                    return
                }
                lastCrashTime = System.currentTimeMillis()
                AndroidUtilities.runOnUIThread {
                    try {
                        if (mCurrentActivity != null && mCurrentActivity !is CrashMain && mCurrentActivity?.isFinishing == false) {
                            mCurrentActivity?.finish()
                        }
                    } catch (e: Exception) {
                        DebugLog.printStackTrace(e)
                    }
                }
                if (AppContext.isLog) {
                    val cause = e?.cause
                    if (cause != null) {
                        DebugLog.e(
                            tag,
                            cause.message + ""
                        )
                    }
                    DebugLog.printStackTrace(e)
                    Toast.makeText(
                        mApp,
                        "TMD bug 了\n" + DebugLog.getPrintStackStr(e),
                        Toast.LENGTH_LONG
                    ).show()
                }
                //处于首页位置
                if (mCurrentActivity is CrashMain) {
                    val currentFocus: View? = mCurrentActivity?.currentFocus
                    if (currentFocus == null) {
                        mCurrentActivity?.finish()
                    }
                }
            }
        })
    }
}