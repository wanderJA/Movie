package com.wander.baseframe.utils

import android.content.Context
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

}