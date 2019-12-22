package com.wander.baseframe.ext

import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import com.wander.baseframe.ext.ViewClickDelay.SPACE_TIME
import com.wander.baseframe.ext.ViewClickDelay.hash
import com.wander.baseframe.ext.ViewClickDelay.lastClickTime
import java.math.BigDecimal


fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.showOrHide(show: Boolean) {
    if (show) show() else hide()
}

fun View.showOrGone(show: Boolean) {
    if (show) show() else gone()
}

fun View.dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

fun View.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun View.sp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        dp,
        context.resources.displayMetrics
    )
}

/**
 * 去除小数点后多余的0
 */
fun BigDecimal.toPrettyStr() = this.stripTrailingZeros().toPlainString().toString()

object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 800
}

infix fun View.clickDelay(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            clickAction()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                clickAction()
            }
        }
    }
}


/**
 * Gson转换实体
 */
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson(json, T::class.java)

fun Any.toJson() = Gson().toJson(this)

fun BigDecimal.isZero() = this.compareTo(BigDecimal.ZERO) == 0


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

