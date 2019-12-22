package com.wander.baseframe.utils

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.wander.baseframe.BaseApp

object ToastUtils {
    private val sHandler = Handler(Looper.getMainLooper())

    @JvmStatic
    fun showToast(resId: Int) {
        showToast(ResourceUtils.getString(resId))
    }

    /**
     * 线程安全Toast
     *
     * @param msg
     */
    @JvmStatic
    fun showToast(msg: String?) {
        msg?.let {
            if (!TextUtils.isEmpty(it)) {
                sHandler.post {
                    val toast = Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_SHORT)
                    toast.setText(msg)
                    toast.show()

                }
            }
        }
    }

    @JvmStatic
    fun showNetErr() {
        showToast("当前网络异常，请稍后重试")
    }
}
