package com.wander.movie.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.wander.baseframe.ext.gone
import com.wander.baseframe.mvp.BasePresenterFragment
import com.wander.baseframe.utils.ImmersionBar
import com.wander.baseframe.utils.Tools
import com.wander.movie.R
import com.wander.movie.presenter.MovieSearchPresenter
import com.wander.movie.ui.iview.IMovieSearchView
import kotlinx.android.synthetic.main.movie_search.*

/**
 * author wander
 * date 2020/1/5
 *
 */
class MovieSearchFragment: BasePresenterFragment<MovieSearchPresenter>(),IMovieSearchView{
    override val buildPresenter: MovieSearchPresenter
        get() = MovieSearchPresenter(this)

    override fun getLayoutId() = R.layout.movie_search
    override fun isUseTitleView() = false
    var fromMainFragment = false
    private var searchKey = ""
    private var searchInitFragment = SearchInitFragment()
    private var searchSuggestFragment = SearchSuggestFragment()
    private var searchResultFragment = SearchResultFragment()

    override fun showError() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        stateBarSpace.layoutParams?.height =
            ImmersionBar.getStatusBarHeight(resources) + Tools.dip2px(10f)
        if (fromMainFragment) {
            searchCancel.gone()
        }
        initFragment()
        searchCancel.setOnClickListener { mActivity?.finish() }
        searchEdit.doAfterTextChanged {
            searchKey = it?.toString() ?: ""
            requestSuggest(searchKey)

        }
        searchEdit.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (!TextUtils.isEmpty(searchKey)) {
                    searchEdit.setText(searchKey)
                    searchEdit.setSelection(searchKey.length)
                    requestSearchResult()
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun initFragment() {
        childFragmentManager.beginTransaction().add(R.id.searchContain, searchInitFragment)
            .add(R.id.searchContain, searchSuggestFragment)
            .add(R.id.searchContain, searchResultFragment)
            .hide(searchResultFragment)
            .hide(searchSuggestFragment)
            .show(searchInitFragment)
            .commitAllowingStateLoss()

    }

    private fun showInitPage() {
        childFragmentManager.beginTransaction()
            .hide(searchResultFragment)
            .hide(searchSuggestFragment)
            .show(searchInitFragment)
            .commitAllowingStateLoss()
    }

    private fun requestSuggest(searchKey: String) {
        if (searchKey.isEmpty()) {
            showInitPage()
        } else {
            searchSuggestFragment.requestSuggest(searchKey)
            childFragmentManager.beginTransaction()
                .hide(searchResultFragment)
                .hide(searchInitFragment)
                .show(searchSuggestFragment)
                .commitAllowingStateLoss()
        }
    }

    fun requestSearchResult(searchKey: String = this.searchKey) {
        searchResultFragment.requestResult(searchKey)
        childFragmentManager.beginTransaction()
            .show(searchResultFragment)
            .hide(searchInitFragment)
            .hide(searchSuggestFragment)
            .commitAllowingStateLoss()
    }

}