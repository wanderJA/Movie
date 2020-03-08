package com.wander.baseframe.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object AppUtils {

    fun goApp(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}