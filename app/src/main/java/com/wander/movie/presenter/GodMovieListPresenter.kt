package com.wander.movie.presenter

import com.wander.baseframe.mvp.BasePresenter
import com.wander.movie.mod.net.MovieApi
import com.wander.movie.ui.iview.IGodMovieList

class GodMovieListPresenter(mView: IGodMovieList) : BasePresenter<IGodMovieList>(mView) {
    private var pageNum = 1
    var hasNext = true
    fun loadData(refresh: Boolean = true) {
        if (refresh) {
            pageNum = 1
            hasNext = true
        } else {
            pageNum++
        }
        val disposable =
            MovieApi.godMovieList(pageNum, type)
                ?.subscribe {
                    hasNext = it.result?.list.isNullOrEmpty() == false
                    mView?.refreshList(refresh, it.result?.list)
                }
        addDisposable(disposable)

    }

    lateinit var type: String
}