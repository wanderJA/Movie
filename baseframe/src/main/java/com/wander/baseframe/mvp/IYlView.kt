package com.wander.baseframe.mvp

interface IYlView : IView {
    fun showPageLoading()
    fun showContent()
    fun showEmpty()
    fun refresh(isSuccess: Boolean = true)
    fun loadMore(isSuccess: Boolean = true)
}