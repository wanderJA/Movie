package com.wander.movie.ui.fragment

import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.movie.R
import com.wander.movie.presenter.MovieSearchPresenter
import com.wander.movie.ui.iview.IMovieSearchView

/**
 * author wander
 * date 2020/1/5
 *
 */
class MovieSearchFragment: BasePresenterFragment<MovieSearchPresenter>(),IMovieSearchView{
    override val buildPresenter: MovieSearchPresenter
        get() = MovieSearchPresenter(this)

    override fun getLayoutId() = R.layout.movie_search


    override fun showError() {
    }

}