package com.wander.movie.ui.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wander.baseframe.component.BaseActivity
import com.wander.baseframe.component.BaseFragment
import com.wander.baseframe.context.CrashMain
import com.wander.baseframe.utils.ImmersionBar
import com.wander.baseframe.utils.TimeUtils
import com.wander.baseframe.utils.ToastUtils
import com.wander.baseframe.view.tab.SimplePagerAdapter
import com.wander.baseframe.view.utils.ViewPagerScroller
import com.wander.movie.R
import com.wander.movie.databinding.ActivityMainBinding
import com.wander.movie.ui.fragment.GanFragment
import com.wander.movie.ui.fragment.MovieFragment
import com.wander.movie.ui.fragment.MovieSearchFragment
import java.util.*

class MainActivity : BaseActivity(), CrashMain {

    private lateinit var binding: ActivityMainBinding
    private val fragmentList = ArrayList<BaseFragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 100)
        }

    }

    private fun initView() {
        fragmentList.add(MovieFragment())
        fragmentList.add(GanFragment())
        val searchFragment = MovieSearchFragment().apply { fromMainFragment = true }
        fragmentList.add(searchFragment)

        val mAdapter = SimplePagerAdapter(supportFragmentManager)
        binding.mViewPage.adapter = mAdapter
        binding.mViewPage.offscreenPageLimit = 3
        val viewPagerScroller = ViewPagerScroller(mActivity, LinearInterpolator())
        viewPagerScroller.initViewPagerScroll(binding.mViewPage)
        binding.mViewPage.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.mNavigationView.selectedItemId = R.id.navigation_main
                    1 -> binding.mNavigationView.selectedItemId = R.id.navigation_assets
                    2 -> binding.mNavigationView.selectedItemId = R.id.navigation_service
                }
                viewPagerScroller.resetScrollDuration()
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //                WLog.e(TAG, "position:" + position + "\tpositionOffset:" + positionOffset + "\tpositionOffsetPixels:" + positionOffsetPixels);
            }
        })
        binding.mNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_AUTO
        binding.mNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            viewPagerScroller.setScrollDuration(0)
            when (item.itemId) {
                R.id.navigation_main -> {
                    binding.mViewPage.currentItem = 0
                    ImmersionBar.initBar(mActivity)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_assets -> {
                    binding.mViewPage.currentItem = 1
                    ImmersionBar.initBar(mActivity)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_service -> {
                    binding.mViewPage.currentItem = 2
                    ImmersionBar.initBar(mActivity, true)
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        })
        mAdapter.fragments = fragmentList
        mAdapter.notifyDataSetChanged()
        binding.mViewPage.currentItem = 0
    }

    private var lastBackPressed = 0L

    override fun onBackPressed() {
        if (fragmentList[binding.mViewPage.currentItem].handleBackPressed()) {
            return
        }
        if (System.currentTimeMillis() - lastBackPressed > TimeUtils.SECOND * 5) {
            ToastUtils.showToast("再按一次退出")
        } else {
            finish()
        }
        lastBackPressed = System.currentTimeMillis()

    }
}
