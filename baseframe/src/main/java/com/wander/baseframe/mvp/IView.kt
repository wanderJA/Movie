package com.wander.baseframe.mvp

/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
interface IView {
    fun showMessage(msg: String)
    fun hideLoading()
    fun showLoading()
    fun showError()
    fun showNoNetWork()
}
