package com.wander.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.baseframe.constants.TYPE
import com.wander.baseframe.utils.ImmersionBar
import com.wander.baseframe.view.tab.RecyclePageAdapter
import com.wander.baseframe.view.tab.TabInfo
import com.wander.movie.R
import com.wander.movie.mod.net.MovieApi
import com.wander.movie.utils.JumpUtils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_gan.*

/**
 * author wander
 * date 2020/1/5
 *
 */
class MovieFragment : BaseLayerFragment() {
    override fun getLayoutId() = R.layout.fragment_gan
    override fun isUseTitleView() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        loadData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            ImmersionBar.initBar(mActivity)
        }
    }

    private fun initView() {
        stateBarSpace.layoutParams?.height = ImmersionBar.getStatusBarHeight(resources)
        val pageTabs = ArrayList<TabInfo>()
        pageTabs.add(
            TabInfo(
                GodMovieListFragment::class.java,
                "电影",
                Bundle().apply { putString(TYPE, "gndy") })
        )
        pageTabs.add(
            TabInfo(
                GodMovieListFragment::class.java,
                "下载排行",
                Bundle().apply { putString(TYPE, "hot") })
        )
        pageTabs.add(
            TabInfo(
                GodMovieListFragment::class.java,
                "电视剧",
                Bundle().apply { putString(TYPE, "tv") })
        )
        pageTabs.add(
            TabInfo(
                GodMovieListFragment::class.java,
                "综艺",
                Bundle().apply { putString(TYPE, "zongyi") })
        )
        pageTabs.add(
            TabInfo(
                GodMovieListFragment::class.java,
                "动漫",
                Bundle().apply { putString(TYPE, "dongman") })
        )

        val adapter = RecyclePageAdapter(childFragmentManager)
        adapter.pageInfoList = pageTabs
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)

        menuIcon.setOnClickListener { mActivity?.let { it1 -> JumpUtils.jumpToMovieSearch(it1) } }

    }

    var disposable: Disposable? = null

    @SuppressLint("AutoDispose")
    private fun loadData() {
        MovieApi.godMovieStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


}