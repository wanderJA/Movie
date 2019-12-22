package com.wander.baseframe.view.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wander.baseframe.R


class FadingEdgeRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun getTopFadingEdgeStrength(): Float {
        return 0f
    }

    private var mMaxHeight: Int = 0


    init {
        attrs?.let {
            initialize(context, it)
        }
    }

    private fun initialize(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.FadingEdgeRecyclerView)
        mMaxHeight =
            arr.getLayoutDimension(R.styleable.FadingEdgeRecyclerView_maxHeight, mMaxHeight)
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (mMaxHeight > 0) {
            heightMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}