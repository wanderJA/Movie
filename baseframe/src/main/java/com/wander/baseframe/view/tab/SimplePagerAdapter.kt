package com.wander.baseframe.view.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 * 长期贮存的viewPager,一直持有fragment
 */
class SimplePagerAdapter(
    fm: FragmentManager,
    var fragments: List<Fragment> = ArrayList(), private val titleArray: Array<String>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(arg0: Int): Fragment {
        return fragments[arg0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleArray?.get(position)
    }

}