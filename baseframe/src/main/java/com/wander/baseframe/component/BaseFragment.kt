package com.wander.baseframe.component

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.umeng.analytics.MobclickAgent
import com.wander.baseframe.utils.DebugLog

/**
 * author wander
 */
open class BaseFragment : Fragment() {

    lateinit var mContext: Context
    protected var tagFragment = javaClass.simpleName
    protected var isVisibleInPage = false               //是否可见状态
    private var isPrepared = false                 //标志位，View已经初始化完成。
    private var isFirstLoad = true      //是否第一次加载
    protected var isActive = false
    protected var statisticsName: String? = null

    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    open fun handleBackPressed() = false

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventAndData()
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        isFirstLoad = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        initViewOnCreated()
        lazyLoad()
    }

    /**
     * 非behavior方式实现PageAdapter，懒加载会子类onViewCreated之前，需注意
     */
    protected open fun initViewOnCreated() {}

    open fun initEventAndData() {
    }


    protected fun needResumeLoad(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(statisticsName ?: tagFragment) //统计页面("MainScreen"为页面名称，可自定义)
        isActive = true
        //isVisibleInPage 用于viewPage,  isHidden()用户show or hide 操作
        if (parentFragment != null) {
            val parentVisibleInPage = if (parentFragment is BaseFragment) {
                (parentFragment as BaseFragment).isVisibleInPage
            } else {
                parentFragment?.userVisibleHint ?: false
            }
            val parentVisible = parentFragment?.isHidden == false && parentVisibleInPage
            //有父fragment时，必须父页面可见时再判断可见性
            if (!isVisibleInPage && !isHidden && parentVisible && needResumeLoad()) {
                dealVisibleOrHidden(true)
            }
        } else {
            //没有父fragment，且当前fragment可见时
            if (!isVisibleInPage && !isHidden && needResumeLoad()) {
                dealVisibleOrHidden(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dealVisibleOrHidden(false);
        isActive = false;
        MobclickAgent.onPageEnd(statisticsName ?: tagFragment)
    }

    /**
     * onResume时会检测，当前uid与上次onResume触发时的uid是否一致， 不一致，则会触发本方法.
     * 页面第一次onResume时，按目前设计不会触发onUserChangedWhenResume, 也不会触发 onUserNotChangedWhenResume方法
     */
    protected fun onUserChangedWhenResume() {

    }

    /**
     * onResume时会检测，当前uid与上次onResume触发时的uid是否一致， 若一致，则会触发本方法.
     * 页面第一次onResume时，按目前设计不会触发onUserChangedWhenResume, 也不会触发onUserNotChangedWhenResume方法
     */
    protected fun onUserNotChangedWhenResume() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
        mContext = context
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     * 默认显示的fragment 比onCreate调用早
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        dealVisibleOrHidden(isVisibleToUser)
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        dealVisibleOrHidden(!hidden)
    }

    private fun dealVisibleOrHidden(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            isVisibleInPage = true
            DebugLog.d(tag, " [onVisibleInViewPager]\ttrue\tprepared: $isPrepared")
            if (!isPrepared) {
                return
            }
            onVisible()
        } else {
            isVisibleInPage = false
            DebugLog.d(tag, " [onVisibleInViewPager]\tfalse\tprepared: $isPrepared")
            if (!isPrepared) {
                return
            }
            onInvisible()
        }
    }

    /**
     * 懒加载 主要用于Viewpager 中使用fragment 创建时显示的第一个页面不会执行
     * 如果该Fragment在ViewPager里面,当Fragment可见的时候自动被回调(不包含第一次默认可见那次,必须是手动选中或者滑动选中)
     * 第一次可见可在setUserVisibleHint 中实现
     */
    open fun onVisible() {
        lazyLoad()
    }

    /**
     * 懒加载 主要用于Viewpager 中使用fragment 创建时显示的第一个页面不会执行
     * 如果该Fragment在ViewPager里面,当Fragment不可见的时候自动被回调
     */
    open fun onInvisible() {

    }

    open fun lazyLoad() {
        if (!isVisibleInPage || !isFirstLoad || !isPrepared) {
            return
        }
        isFirstLoad = false
        initLazyData()
    }

    open fun initLazyData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPrepared = false
        isFirstLoad = true
    }

    override fun onDetach() {
        super.onDetach()
        isPrepared = false
        isFirstLoad = true
    }


    protected var mActivity: FragmentActivity? = null
        get() {
            return field ?: activity
        }
}