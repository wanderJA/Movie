package com.wander.movie.ui.fragment

import com.wander.baseframe.constants.TYPE
import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.baseframe.view.recycler.PullRefreshRecyclerView
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter
import com.wander.movie.R
import com.wander.movie.adapter.cell.CellGodMovie
import com.wander.movie.bean.GodMovieItem
import com.wander.movie.presenter.GodMovieListPresenter
import com.wander.movie.ui.iview.IGodMovieList
import kotlinx.android.synthetic.main.fragment_god_movie_list.*

class GodMovieListFragment : BasePresenterFragment<GodMovieListPresenter>(),
    IGodMovieList {
    private var mAdapter = RVSimpleAdapter()
    override fun isUseTitleView() = false
    override val buildPresenter: GodMovieListPresenter
        get() = GodMovieListPresenter(this)

    override fun getLayoutId() = R.layout.fragment_god_movie_list

    override fun initEventAndData() {
        mPresenter.type = arguments?.getString(TYPE, "gndy") ?: "gndy"
    }

    override fun initLazyData() {
        initView()
        mPresenter.loadData()
    }

    private fun initView() {
        showTransparentLoading()
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