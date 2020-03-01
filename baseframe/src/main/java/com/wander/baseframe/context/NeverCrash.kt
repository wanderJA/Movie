package com.wander.baseframe.context

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Created by wander on 2017/4/28.
 */
class NeverCrash private constructor() {
    private fun setCrashHandler(crashHandler: CrashHandler) {
        mCrashHandler = crashHandler
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    sendCrash(e)
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { t: Thread?, e: Throwable -> sendCrash(e) }
    }

    private fun sendCrash(e: Throwable) {
        handler.removeMessages(CRASH)
        val message =
            handler.obtainMessage(CRASH)
        message.obj = e
        handler.sendMessageDelayed(message, 100)
    }

    interface CrashHandler {
        fun uncaughtException(t: Thread?, e: Throwable?)
    }

    companion object {
        private var mCrashHandler: CrashHandler? = null
        private var mInstance: NeverCrash? = null
        private const val CRASH = 1
        private val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == CRASH) {
                    if (mCrashHandler != null) { //捕获异常处理
                        mCrashHandler!!.uncaughtException(
                            Looper.getMainLooper().thread,
                            msg.obj as Throwable
                        )
                    }
                }
            }
        }

        private val instance: NeverCrash?
            private get() {
                if (mInstance == null) {
                    synchronized(NeverCrash::class.java) {
                        if (mInstance == null) {
                            mInstance = NeverCrash()
                        }
                    }
                }
                return mInstance
            }

        fun init(crashHandler: CrashHandler) {
            instance!!.setCrashHandler(crashHandler)
        }
    }
}