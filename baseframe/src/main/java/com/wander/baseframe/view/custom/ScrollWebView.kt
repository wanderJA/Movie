package com.wander.baseframe.view.custom

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView


/**
 * author wander
 * date 2019/7/15
 *
 */
class ScrollWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WebView(context, attrs) {
    var callback: onScrollChangeCallback? = null
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        callback?.onScrollChanged(l, t, oldl, oldt)
    }

    interface onScrollChangeCallback {
        fun onScrollChanged(scrollY: Int, t: Int, oldScrollY: Int, oldt: Int)
    }
}