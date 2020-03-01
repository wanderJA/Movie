package com.wander.movie.utils

import android.content.Context
import android.os.Bundle
import com.wander.baseframe.component.CommonWebFragment
import com.wander.baseframe.component.ContainActivity

object JumpUtils {

    fun jumpToWeb(context: Context, url: String, title: String? = null) {
        val params = Bundle()
        params.putString(CommonWebFragment.WEB_URL, url)
        params.putString(CommonWebFragment.WEB_TITLE, title)
        ContainActivity.start(context, CommonWebFragment::class.java, params)
    }
}