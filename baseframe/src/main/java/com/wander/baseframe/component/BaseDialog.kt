package com.wander.baseframe.component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.wander.baseframe.R
import com.wander.baseframe.view.loading.LoadingDialog

abstract class BaseDialog(context: Context) : Dialog(context) {

    var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context.setTheme(themeId())
        setContentView(layoutId())

        loadingDialog = LoadingDialog(context)
        // 设置Dialog参数
        val window = window
        window?.run {
            val params = attributes
            with(params) {
                gravity = gravity()
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
        initDate()
        initView()
    }

    open fun gravity(): Int = Gravity.BOTTOM

    abstract fun initView()

    abstract fun initDate()

    abstract fun layoutId(): Int

    open fun themeId(): Int = R.style.Dialog

    override fun dismiss() {
        loadingDialog?.dismiss()
        super.dismiss()
    }
}