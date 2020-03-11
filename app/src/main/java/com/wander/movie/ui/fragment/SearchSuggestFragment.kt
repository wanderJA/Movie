package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter
import com.wander.baseframe.view.recycler.divider.RecyclerViewGapDecoration
import com.wander.movie.R
import com.wander.movie.adapter.cell.CellSearchKey
import com.wander.movie.presenter.SearchSuggestPresenter
import com.wander.movie.ui.iview.ISearchSuggest
import kotlinx.android.synthetic.main.recycle_layout.*

class SearchSuggestFragment : BasePresenterFragment<SearchSuggestPresenter>(), ISearchSuggest {
    override val buildPresenter: SearchSuggestPresenter
        get() = SearchSuggestPresenter(this)

    override fun getLayoutId() = R.layout.recycle_layout
    override fun isUseTitleView() = false
    private val mAdapter = RVSimpleAdapter()

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
    }

    override fun refreshList(kws: List<String>) {
        dismissTransparentLoading()
        mStateViewContainer.removeAllViews()
        mAdapter.clear()
        kws.forEach { key ->
            val cellSearchKey = CellSearchKey(key)
            cellSearchKey.onClickListener = View.OnClickListener {
                if (parentFragment is MovieSearchFragment) {
                    (parentFragment as MovieSearchFragment).requestSearchResult(key)
                }
            }
            mAdapter.add(cellSearchKey)
        }
    }

    override fun showError() {
        showEmptyReload(object : ReloadListener {
            override fun onReload() {
                requestSuggest("")
            }
        })
    }

    fun requestSuggest(searchKey: String) {
        mPresenter.loadData(searchKey)
        showTransparentLoading()
    }
}