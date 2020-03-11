package com.wander.movie.presenter

import com.wander.baseframe.mvp.BasePresenter
import com.wander.movie.mod.net.MovieApi
import com.wander.movie.ui.iview.ISearchResult

class SearchResultPresenter(mView: ISearchResult) : BasePresenter<ISearchResult>(mView) {
    var searchKey = ""
    private var pageNum = 1
    var hasNext = true
    fun loadData(searchKey: String = this.searchKey, refresh: Boolean = true) {
        this.searchKey = searchKey
        if (refresh) {
            pageNum = 1
            hasNext = true
        } else {
            pageNum++
        }
        val disposable =
            MovieApi.godMovieSearchResult(pageNum, searchKey)
                ?.subscribe {
                    hasNext = it.result?.list.isNullOrEmpty() == false
                    mView?.refreshList(refresh, it.result?.list)
                }
        addDisposable(disposable)

    }
}