package com.wander.baseframe.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.wander.baseframe.BaseApp

object Tools {

    /**
     * dip  px
     */
    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * dip  px
     */
    @JvmStatic
    fun dip2px(dipValue: Float): Int {
        val scale = BaseApp.getInstance().resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * dip  px
     */
    @JvmStatic
    fun dp2px(dipValue: Float): Float {
        val scale = BaseApp.getInstance().resources.displayMetrics.density
        return (dipValue * scale + 0.5f)
    }

    /**
     * px  dip
     */
    @JvmStatic
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun setClipBoardContent(content: String?) { //设置剪切板数据
        Handler(Looper.getMainLooper()).post {
            try {
                val clipboardManager =
                    BaseApp.mApp.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(
                    "label",
                    content
                ) //文本型数据 clipData 的构造方法。
                clipboardManager.setPrimaryClip(clipData) // 将 字符串 str 保存 到剪贴板。
                ToastUtils.showToast("复制成功")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}