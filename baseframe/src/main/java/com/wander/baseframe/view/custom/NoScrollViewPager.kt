package com.wander.baseframe.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {
    var noScroll = false


    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll)
            false
        else
            super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll)
            false
        else
            super.onInterceptTouchEvent(arg0)
    }


}
