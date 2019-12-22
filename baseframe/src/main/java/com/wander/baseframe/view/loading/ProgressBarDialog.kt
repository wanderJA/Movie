package com.wander.baseframe.view.loading

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.wander.baseframe.R
import com.wander.baseframe.view.loading.indicators.BallSpinFadeLoaderIndicator
import kotlinx.android.synthetic.main.dialog_progress_view.*


class ProgressBarDialog(context: Context) : Dialog(context, R.style.Dialog_Loding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress_view)
        // 设置Dialog参数
        val window = window
        val params = window!!.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params

        progress_image.indicator = BallSpinFadeLoaderIndicator()
        progress_image.setIndicatorColor(ContextCompat.getColor(context, R.color.colorAccent))
        setCancelable(false)
    }
}