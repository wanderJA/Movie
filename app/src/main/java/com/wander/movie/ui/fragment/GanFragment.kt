package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.movie.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val adapter = MyPagerAdapter(childFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(viewPager)
    }

    inner class MyPagerAdapter(fm: FragmentManager, private val fragments: List<NewsTabFragment>) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getPageTitle(position: Int): CharSequence? {
            return fragments[position].getTitle()
        }

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}