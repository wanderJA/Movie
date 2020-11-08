package com.wander.baseframe.view.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class RecyclePageAdapter(
    var fm: FragmentManager,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) :
    FragmentStatePagerAdapter(fm, behavior) {
    var pageInfoList = ArrayList<TabInfo>()
    override fun getCount(): Int {
        return pageInfoList.size

    }

    override fun getItem(position: Int): Fragment {
        val className = if (position < pageInfoList.size) {
            pageInfoList[position].fragmentClass.name
        } else {
            ErrorFragment::class.java.name
        }
        val fragment = fm.fragmentFactory.instantiate(
            ClassLoader.getSystemClassLoader(),
            className
        )
        fragment.arguments = pageInfoList[position].bundle
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageInfoList[position].title
    }


    fun addTab(tabInfo: TabInfo) {
        pageInfoList.add(tabInfo)
        notifyDataSetChanged()
    }

}

data class TabInfo(var fragmentClass: Class<*>, var title: String, var bundle: Bundle? = null) {

}