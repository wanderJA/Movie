package com.wander.baseframe.view.loading

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.wander.baseframe.R


class LoadingDialog(context: Context) : Dialog(context, R.style.Dialog_Loding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress_view)
        // 设置Dialog参数
        val window = window
        val params = window!!.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params
        setCancelable(false)
    }
}