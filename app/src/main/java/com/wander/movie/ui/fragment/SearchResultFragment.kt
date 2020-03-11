package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.baseframe.view.recycler.PullRefreshRecyclerView
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter
import com.wander.baseframe.view.recycler.divider.RecyclerViewGapDecoration
import com.wander.movie.R
import com.wander.movie.adapter.cell.CellGodMovie
import com.wander.movie.bean.GodMovieItem
import com.wander.movie.presenter.SearchResultPresenter
import com.wander.movie.ui.iview.ISearchResult
import kotlinx.android.synthetic.main.recycle_layout.*

class SearchResultFragment : BasePresenterFragment<SearchResultPresenter>(), ISearchResult {
    override val buildPresenter: SearchResultPresenter
        get() = SearchResultPresenter(this)
    private val mAdapter = RVSimpleAdapter()

    override fun getLayoutId() = R.layout.recycle_layout
    override fun isUseTitleView() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(
            RecyclerViewGapDecoration().setEdgeMargin(
                resources.getDimensionPixelSize(
                    R.dimen.content_padding
                )
            )
        )
        mRecyclerView.setOnScrollBottomListener(object :
            PullRefreshRecyclerView.OnScrollBottomListener {
            override fun onLoadMore() {
                if (mAdapter.canLoadMore()) {
                    if (mPresenter.hasNext) {
                        mAdapter.showLoadMore()
                        mPresenter.loadData(refresh = false)
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
        mStateViewContainer.removeAllViews()
        dismissTransparentLoading()
        if (refresh) {
            mAdapter.clear()
        }
        mAdapter.hideLoadMore()
        list?.forEach {
            val cellGodMovie = CellGodMovie()
            cellGodMovie.data = it
            mAdapter.add(cellGodMovie)
        }

    }

    override fun showError() {
        showDataEmptyReload(object : ReloadListener {
            override fun onReload() {
                requestResult(mPresenter.searchKey)
            }
        })
    }

    fun requestResult(searchKey: String) {
        showTransparentLoading()
        mPresenter.loadData(searchKey)
    }
}