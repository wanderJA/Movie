package com.wander.baseframe.utils

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

object AnimationUtils {

    val rotate = RotateAnimation(
        0f,
        360f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).apply {
        interpolator = LinearInterpolator()
        duration = 800//设置动画持续时间
        repeatCount = -1//设置重复次数
        fillAfter = true//动画执行完后是否停留在执行完的状态
        startOffset = 10//执行前的等待时间
    }

}