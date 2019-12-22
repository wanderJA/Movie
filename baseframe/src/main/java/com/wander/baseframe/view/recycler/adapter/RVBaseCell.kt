package com.qiyi.video.reader.widget.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.wander.baseframe.view.recycler.adapter.Cell
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder

/**
 * Created by wander on 17/1/19.
 */

abstract class RVBaseCell<T> : Cell {
    protected var TAG = "RVBaseCell"
    var onClickListener: View.OnClickListener? = null
    /**
     * 是否每次都刷新view
     */
    var hasInit = false

    var data: T? = null
        set(value) {
            field = value
            hasInit = false
        }
    var isAttached = false

    constructor() {}

    constructor(t: T?) {
        data = t
    }

    override fun releaseResource() {
        isAttached = false
        // do nothing
        // 如果有需要回收的资源，子类自己实现
    }

    override fun onAttachedToWindow(holder: RVBaseViewHolder) {
        isAttached = true
    }

}

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun ViewGroup.createBaseViewHolder(@LayoutRes layoutId: Int): RVBaseViewHolder {
    return RVBaseViewHolder(inflate(layoutId))
}