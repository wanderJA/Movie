package com.wander.movie.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wander.baseframe.component.BaseFragment

/**
 * @author wander
 */
class MainAdapter(
    fm: FragmentManager,
    private var fragments: List<BaseFragment>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position > fragments.size || position < 0) {
            throw RuntimeException("fragment null")
        } else fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}