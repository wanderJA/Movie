package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.baseframe.view.recycler.PullRefreshRecyclerView
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter
import com.wander.movie.R
import com.wander.movie.adapter.cell.CellGodMovie
import com.wander.movie.bean.GodMovieItem
import com.wander.movie.presenter.GodMovieListPresenter
import com.wander.movie.ui.iview.IGodMovieList
import kotlinx.android.synthetic.main.fragment_god_movie_list.*

class GodMovieListFragment(var type: String) : BasePresenterFragment<GodMovieListPresenter>(),
    IGodMovieList {
    private var mAdapter = RVSimpleAdapter()
    override fun isUseTitleView() = false
    override val buildPresenter: GodMovieListPresenter
        get() = GodMovieListPresenter(this)

    override fun getLayoutId() = R.layout.fragment_god_movie_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.type = type
        initView()
        mPresenter.loadData()
    }

    private fun initView() {
        mRecyclerView.adapter = mAdapter
        mRefreshLayout.setOnRefreshListener {
            mPresenter.loadData()
        }
        mRecyclerView.setOnScrollBottomListener(object :
            PullRefreshRecyclerView.OnScrollBottomListener {
            override fun onLoadMore() {
                if (mAdapter.canLoadMore()) {
                    if (mPresenter.hasNext) {
                        mAdapter.showLoadMore()
                        mPresenter.loadData(false)
                    } else {
                        if (mAdapter.itemCount > 3) {
                            mAdapter.showBottom()
                        }
                    }
                }
            }
        })

    }

    override fun refreshList(refresh: Boolean, list: List<GodMovieItem>?) {
        if (refresh) {
            mAdapter.clear()
            mRefreshLayout.isRefreshing = false
        }
        mAdapter.hideLoadMore()
        list?.forEach {
            val cellGodMovie = CellGodMovie()
            cellGodMovie.data = it
            mAdapter.add(cellGodMovie)
        }

    }

    override fun showError() {

    }
}