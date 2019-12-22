package com.wander.movie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 针对[com.google.android.material.bottomsheet.BottomSheetBehavior]
 * 适配的箭头指示器
 */
class SheetArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 默认为COLLAPSED状态
     */
    var slideOffset = 0f
        set(value) {
            field = value
            invalidate()
        }
    var arrowHeight = 10f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = Color.parseColor("#ffd8d8d8")
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = arrowHeight
    }


    override fun onDraw(canvas: Canvas) {
        val centerX = (width shr 1).toFloat()
        val centerY = (height shr 1).toFloat()
        val maxHeight = width / 6 - arrowHeight
        val offsetY = centerY + maxHeight * slideOffset
        canvas.drawLine(arrowHeight, centerY, centerX, offsetY, paint)
        canvas.drawLine(centerX, offsetY, width.toFloat() - arrowHeight, centerY, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var adjustHeight = heightMeasureSpec
        if (heightMode == MeasureSpec.EXACTLY) {
            if (heightSize < widthSize / 3) {
                adjustHeight = MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(widthSize / 3),
                    MeasureSpec.EXACTLY
                )
            }
        }
        super.onMeasure(widthMeasureSpec, adjustHeight)
    }
}