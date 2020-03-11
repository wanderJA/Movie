package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.baseframe.utils.ImmersionBar
import com.wander.movie.R
import com.wander.movie.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.fragment_gan.*
import java.util.*

/**
 * author wander
 * date 2020/1/5
 *
 */
class GanFragment: BaseLayerFragment() {
    override fun getLayoutId() = R.layout.fragment_gan
    override fun isUseTitleView() = false

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            ImmersionBar.initBar(mActivity)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateBarSpace.layoutParams?.height = ImmersionBar.getStatusBarHeight(resources)
        val fragments = ArrayList<NewsTabFragment>()
        val fragment1 = NewsTabFragment()
        fragment1.setTitle("Android")
        fragments.add(fragment1)
        val fragment2 = NewsTabFragment()
        fragment2.setTitle("iOS")
        fragments.add(fragment2)
        val fragment3 = NewsTabFragment()
        fragment3.setTitle("前端")
        fragments.add(fragment3)
        val adapter = MyPagerAdapter(
            childFragmentManager,
            fragments,
            arrayListOf("Android", "iOS", "前端")
        )
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(viewPager)
    }

}