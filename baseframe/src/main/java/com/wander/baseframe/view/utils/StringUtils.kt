package com.wander.baseframe.view.utils

import java.text.DecimalFormat

object StringUtils {
    var distanceFormat = DecimalFormat("0.0")


    fun convertMb(size: Long): String {
        val v = (size / 1000).toDouble()
        return distanceFormat.format(v) + "M"
    }
}