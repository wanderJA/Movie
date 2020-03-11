package com.wander.movie.ui.iview

import com.wander.baseframe.mvp.IView

interface ISearchSuggest : IView {
    fun refreshList(kws: List<String>)
}