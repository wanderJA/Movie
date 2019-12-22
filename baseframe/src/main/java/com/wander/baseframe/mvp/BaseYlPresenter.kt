package com.wander.baseframe.mvp

open class BaseYlPresenter<T : IView, M : IModel>(mView: T, val mModel: M) :
    BasePresenter<T>(mView) {

}