package com.wander.baseframe.view.utils

import android.content.Context
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

/**
 * Created by wander on 2016/12/8.
 */
class ViewPagerScroller : Scroller {
    // 滑动速度
    private var mScrollDuration = ViewConfiguration.getScrollBarFadeDuration()

    /**
     * 设置速度速度
     *
     * @param duration
     */
    fun setScrollDuration(duration: Int) {
        mScrollDuration = duration
    }

    fun resetScrollDuration() {
        mScrollDuration = ViewConfiguration.getScrollBarFadeDuration()
    }

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?,
        interpolator: Interpolator?
    ) : super(context, interpolator)

    override fun startScroll(
        startX: Int,
        startY: Int,
        dx: Int,
        dy: Int,
        duration: Int
    ) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration)
    }

    fun initViewPagerScroll(viewPager: ViewPager?) {
        try {
            val mScroller =
                ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            mScroller[viewPager] = this
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}