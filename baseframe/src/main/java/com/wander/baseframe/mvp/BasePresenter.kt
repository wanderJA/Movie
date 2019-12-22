package com.wander.baseframe.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Call


/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
abstract class BasePresenter<T : IView>(mView: T) : IPresenter {
    protected var mView: T? = null
    protected var tag = javaClass.simpleName
    protected var mCompositeDisposable = CompositeDisposable()
    private var callList: ArrayList<Call<*>>? = null

    val isViewAttach: Boolean
        get() = mView != null

    val isViewDetach: Boolean
        get() = mView == null

    /**
     * onCreate 中创建
     */
    init {
        this.mView = mView
    }

    private fun unSubscribe() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.clear()
        }
        callList?.forEach {
            it.cancel()
        }
        callList?.clear()
        callList = null
    }

    protected fun addDisposable(subscription: Disposable) {
        mCompositeDisposable.add(subscription)
    }

    protected fun addCall(call: Call<*>?) {
        if (callList == null) {
            callList = ArrayList()
        }
        if (call != null) {
            callList?.add(call)
        }
    }

    override fun onDetachView() {
        mView = null
        unSubscribe()
    }


    //提供给autoDispose处理rxJava内存泄漏
    lateinit var lifecycleScope: AndroidLifecycleScopeProvider

    override fun onStart() {
        //提供给autoDispose处理rxJava内存泄漏
        if (mView is LifecycleOwner) lifecycleScope =
            AndroidLifecycleScopeProvider.from(mView as LifecycleOwner, Lifecycle.Event.ON_DESTROY)
    }
}
