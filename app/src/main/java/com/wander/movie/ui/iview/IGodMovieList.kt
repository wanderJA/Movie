package com.wander.movie.ui.iview

import com.wander.baseframe.mvp.IView
import com.wander.movie.bean.GodMovieItem

interface IGodMovieList : IView {
    fun refreshList(refresh: Boolean, list: List<GodMovieItem>?)
}