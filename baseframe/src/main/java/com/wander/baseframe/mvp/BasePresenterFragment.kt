package com.wander.baseframe.mvp


import android.content.Context
import android.os.Bundle
import com.wander.baseframe.component.BaseLayerFragment


/**
 * Created by wander on 2016/6/15.
 */
abstract class BasePresenterFragment<T : IPresenter> : BaseLayerFragment() {
    protected lateinit var mPresenter: T

    protected abstract val buildPresenter: T
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mPresenter = buildPresenter


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate()
    }

    override fun onDestroy() {
        mPresenter.onDetachView()
        super.onDestroy()
    }
}
