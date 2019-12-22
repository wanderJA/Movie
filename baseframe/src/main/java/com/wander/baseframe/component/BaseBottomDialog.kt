package com.wander.baseframe.component

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wander.baseframe.R
import com.wander.baseframe.ext.clickDelay
import com.wander.baseframe.ext.gone
import com.wander.baseframe.utils.Tools
import kotlinx.android.synthetic.main.base_dialog.*
import kotlinx.android.synthetic.main.base_dialog.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.px2dip

abstract class BaseBottomDialog(context: Context) : BottomSheetDialog(context) {

    lateinit var view: View

    var closeClick: (() -> Unit) = {
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = View.inflate(context, R.layout.base_dialog, null)
        with(view) {
            LayoutInflater.from(context).inflate(layoutId(), flDialogContent)
            setContentView(this)
            ivDialogClose.clickDelay {
                closeClick.invoke()
            }
            title()?.let {
                tvDialogTitle.text = it
            } ?: let {
                tvDialogTitle.gone()
                ivDialogClose.gone()
            }
            setContentMargin()
        }
        setViewSize()
        initDate()
        initView()
        if (!usePadding()) {
            view.setPadding(0, 0, 0, 0)
        }
    }

    private fun setContentMargin() {
        if (useDefaultMargin()) {
            val contentParams = flDialogContent.layoutParams as ConstraintLayout.LayoutParams
            contentParams.marginEnd = Tools.dip2px(marginEndDp())
            contentParams.marginStart = Tools.dip2px(marginStartDp())
            contentParams.topMargin = Tools.dip2px(marginTopDp())
            contentParams.bottomMargin = Tools.dip2px(marginBottomDp())
            flDialogContent.layoutParams = contentParams
        }
    }

    open fun useDefaultMargin() = true

    abstract fun layoutId(): Int
    abstract fun initView()
    abstract fun initDate()
    open fun title(): String? = null

    open fun marginStartDp() = context.px2dip(view.tvDialogTitle.marginStart)
    open fun marginEndDp() = context.px2dip(view.ivDialogClose.marginEnd)
    open fun marginTopDp() = 14f
    open fun marginBottomDp() = 0f

    open fun usePadding() = true

    //设置底部预览高度
    fun setPreViewHeight(dp: Int) {
        val mBehavior = BottomSheetBehavior.from(view.parent as View);
        mBehavior.peekHeight = context.dip(dp)
    }


    fun setViewSize(width: Int = viewWidth(), height: Int = viewHeight()) {
        window?.setLayout(width, height)
        window?.setGravity(Gravity.BOTTOM)
    }

    open fun viewWidth() = ViewGroup.LayoutParams.MATCH_PARENT
    open fun viewHeight() = ViewGroup.LayoutParams.MATCH_PARENT
}