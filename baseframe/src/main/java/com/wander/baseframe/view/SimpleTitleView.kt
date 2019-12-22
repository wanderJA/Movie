package com.wander.baseframe.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.wander.baseframe.R
import com.wander.baseframe.component.ICommonTitle
import com.wander.baseframe.utils.ImmersionBar
import kotlinx.android.synthetic.main.simple_title.view.*

/**
 * author wangdou
 * date 2018/6/20
 *
 */
class SimpleTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    useImmersionBar: Boolean = true
) : FrameLayout(context, attrs, defStyleAttr),
    ICommonTitle {

    init {
        LayoutInflater.from(context).inflate(R.layout.simple_title, this)
        setBackgroundColor(resources.getColor(R.color.colorPrimary))
        if (useImmersionBar) {
            setPadding(0, ImmersionBar.getStatusBarHeight(resources), 0, 0)
        }
    }

    fun useImmersionBar() {
        setPadding(0, ImmersionBar.getStatusBarHeight(resources), 0, 0)
    }

    override fun setTitle(title: CharSequence?) {
        simpleTitle.text = title
    }

    override fun getBackView(): ImageView? {
        return simpleBack
    }


    override fun setVisibleGone() {
        visibility = View.GONE
    }

    override fun setVisibleVisible() {
        visibility = View.VISIBLE
    }

    fun getTitleView(): TextView? {
        return simpleTitle
    }


    fun getTitleMenu(): TextView? {
        simpleMenu.visibility = View.VISIBLE
        return simpleMenu
    }
}