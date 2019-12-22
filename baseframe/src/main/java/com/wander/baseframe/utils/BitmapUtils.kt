package com.wander.baseframe.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

object BitmapUtils {
    fun getBitmapFromDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is VectorDrawable || drawable is VectorDrawableCompat) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } else {
            throw  IllegalArgumentException("unsupported drawable type")
        }
    }

}