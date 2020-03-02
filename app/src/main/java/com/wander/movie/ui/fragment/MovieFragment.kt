package com.wander.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.baseframe.utils.DebugLog
import com.wander.movie.R
import com.wander.movie.mod.net.MovieApi
import io.reactivex.disposables.Disposable

/**
 * author wander
 * date 2020/1/5
 *
 */
class MovieFragment : BaseLayerFragment() {
    override fun getLayoutId() = R.layout.fragment_movie
    override fun isUseTitleView() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        loadData()
    }

    private fun initView() {

    }

    var disposable: Disposable? = null
    @SuppressLint("AutoDispose")
    private fun loadData() {
        MovieApi.godMovieStart()
        disposable =
            MovieApi.godMovieList(1)
                ?.subscribe({ DebugLog.d(it?.message) })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


}