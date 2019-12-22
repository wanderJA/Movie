package com.wander.baseframe.view.utils

import android.text.TextPaint
import android.text.style.URLSpan

/**
 * author wander
 * date 2019/8/28
 *
 */
open class NoUnderLineUrlSpan(url: String?) : URLSpan(url) {

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
    }
}