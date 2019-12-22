package com.wander.baseframe.mvp


import android.os.Bundle
import com.wander.baseframe.component.BaseLayerFragment


/**
 * Created by wander on 2016/6/15.
 */
abstract class BasePresenterFragment<T : IPresenter> : BaseLayerFragment() {
    protected var mPresenter: T? = null

    protected abstract val buildPresenter: T?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = buildPresenter
        mPresenter?.onStart()
    }

    override fun onDestroy() {
        mPresenter?.onDetachView()
        mPresenter = null
        super.onDestroy()
    }

    fun hideLoading() {

    }

    fun showLoading() {
    }


    open fun showError() {

    }

    fun showNoNetWork() {
    }

}
