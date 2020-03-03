package com.wander.movie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.wander.baseframe.component.BaseFragment
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.movie.R
import com.wander.movie.adapter.MyPagerAdapter
import com.wander.movie.mod.net.MovieApi
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

    private fun initView() {
        val fragments = ArrayList<BaseFragment>()
        val titles = ArrayList<String>()
        val fragment1 = GodMovieListFragment("gndy")
        titles.add("电影")
        fragments.add(fragment1)
        val fragment2 = GodMovieListFragment("hot")
        titles.add("下载排行")
        fragments.add(fragment2)
        val fragment3 = GodMovieListFragment("tv")
        titles.add("电视剧")
        fragments.add(fragment3)
        val fragment4 = GodMovieListFragment("zongyi")
        titles.add("综艺")
        fragments.add(fragment4)
        val fragment5 = GodMovieListFragment("dongman")
        titles.add("动漫")
        fragments.add(fragment5)


        val adapter = MyPagerAdapter(
            childFragmentManager,
            fragments,
            titles
        )
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(viewPager)

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