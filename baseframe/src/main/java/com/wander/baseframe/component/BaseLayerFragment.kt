package com.wander.baseframe.component

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.wander.baseframe.R
import com.wander.baseframe.utils.*
import com.wander.baseframe.view.SimpleTitleView
import com.wander.baseframe.view.loading.CircleLoadingView
import kotlinx.android.synthetic.main.base_view_contain.view.*
import java.util.logging.Logger

/**
 * author wander
 * date 2018/3/14
 *
 */
abstract class BaseLayerFragment : BaseFragment() {
    lateinit var mView: View

    lateinit var mAboveContainer: FrameLayout// 用于引导图实现
    lateinit var mContentContainer: FrameLayout// 用于显示内容
    lateinit var mStateViewContainer: FrameLayout// 用于显示各种加载状态的内容
    var mTitleView: ICommonTitle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ImmersionBar.initBar(mActivity)
        mView = layoutInflater.inflate(R.layout.base_view_contain, container, false)
        mAboveContainer = mView.findViewById(R.id.above_container)
        mContentContainer = mView.findViewById(R.id.content_container)
        mStateViewContainer = mView.findViewById(R.id.stateView_container)


        val titleContainer = if (useFloatBar()) {
            mView.floatBarContainer
        } else {
            mView.bar_container
        }
        if (isUseTitleView()) {
            mTitleView = onCreateTitleView()
            if (mTitleView == null) {
                titleContainer.visibility = View.GONE
            } else {
                titleContainer.removeAllViews()
                mTitleView?.backView?.setOnClickListener { mActivity?.finish() }
                titleContainer.addView(mTitleView as View)
                titleContainer.visibility = View.VISIBLE
            }
        } else {
            titleContainer.visibility = View.GONE
        }

        if (getLayoutId() > 0) {
            LayoutInflater.from(mActivity).inflate(getLayoutId(), mContentContainer)
        }
        setBaseBackground(null)
        return mView
    }


    open fun useFloatBar(): Boolean {
        return false
    }

    abstract fun getLayoutId(): Int

    /**
     * 重写此方法来修改特定的背景
     *
     * @param drawable
     */
    @Suppress("DEPRECATION")
    protected fun setBaseBackground(drawable: Drawable?) {
        mView.baseBg.background = drawable
    }

    /**
     * 重写此方法来修改特定的背景
     */
    protected fun setBaseBackground(@ColorRes color: Int) {
        if (color == 0) {
        } else {
            mView.baseBg.setBackgroundColor(ResourceUtils.getColor(color))
        }
    }

//    private var lottieAnimationView: LottieAnimationView? = null
//    private var mStateLayout: ViewGroup? = null
//
//    protected fun showLoading() {
//        mStateViewContainer.removeAllViews()
//        if (lottieAnimationView == null) {
//            mStateLayout = FrameLayout(mActivity)
//            lottieAnimationView = LottieAnimationView(mActivity)
//            lottieAnimationView?.setAnimation("loading.json")
//            lottieAnimationView?.loop(true)
//            lottieAnimationView?.layoutParams = FrameLayout.LayoutParams(Tools.dip2px(135f), Tools.dip2px(135f), Gravity.CENTER)
//            mStateLayout?.addView(lottieAnimationView)
//        }
//        mStateLayout?.let {
//            if (it.parent is ViewGroup) {
//                (it.parent as ViewGroup).removeView(it)
//            }
//            it.setOnClickListener { }
//            mStateViewContainer.setBackgroundColor(ResourceUtils.getColor(R.color.app_background))
//            mStateViewContainer.addView(it)
//            lottieAnimationView?.playAnimation()
//        }
//    }
//
//    @Suppress("DEPRECATION")
//    protected fun dismissLoading() {
//        lottieAnimationView?.cancelAnimation()
//        mStateViewContainer.removeAllViews()
//            mStateViewContainer.setBackgroundDrawable(null)
//    }


    /**
     * 加载失败重试
     *
     * @param reloadListener 重新加载触发
     * @param paddingBottom 内容区与底部的距离，用于整体靠下时，内容向上移动一点
     */
    protected fun showReload(
        charSequence: CharSequence, @DrawableRes imageRes: Int,
        reloadListener: ReloadListener? = null,
        paddingBottom: Int = 0
    ) {
        mStateViewContainer.removeAllViews()
        val view = LayoutInflater.from(mActivity)
            .inflate(R.layout.reload_layout, mStateViewContainer, false)
        view.setOnClickListener {}
        view.findViewById<TextView>(R.id.reloadText).text = charSequence
        view.findViewById<ImageView>(R.id.reloadImg)
            .setImageDrawable(ResourceUtils.getDrawable(imageRes))
        if (reloadListener != null) {
            view.findViewById<View>(R.id.reloadRefresh).setOnClickListener {
                showTransparentLoading()
                reloadListener.onReload()
            }
        } else {
            view.findViewById<View>(R.id.reloadRefresh).visibility = View.GONE
        }
        view.setPadding(0, 0, 0, paddingBottom)
        mStateViewContainer.addView(view)
    }

    protected fun removeStateViews() {
        mStateViewContainer.removeAllViews()
    }

    fun showDataEmptyReload(reloadListener: ReloadListener) {
        if (NetworkUtil.isNetworkAvailable()) {
            showEmptyReload(reloadListener)
        } else {
            showNetReload(reloadListener)
        }
    }

    /**
     * 加载失败重试,网络异常
     */
    protected fun showNetReload(reloadListener: ReloadListener, paddingBottom: Int = 0) {
        showReload(
            getString(R.string.netReloadTip),
            R.drawable.ic_no_net,
            reloadListener,
            paddingBottom
        )
    }

    /**
     * 加载失败重试,网络异常
     */
    protected fun showEmptyReload(
        reloadListener: ReloadListener,
        paddingBottom: Int = Tools.dip2px(100f),
        tip: CharSequence = getString(R.string.page_empty)
    ) {
        showReload(tip, R.drawable.ic_no_net, reloadListener, paddingBottom)
    }


    /**
     * 透明loading  基线loading
     */
    fun showTransparentLoading() {
        mAboveContainer.removeAllViews()
        val circleLoadingView = CircleLoadingView(mActivity)
        circleLoadingView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        circleLoadingView.setOnClickListener {
            DebugLog.e(tagFragment, "click loading")
        }
        mAboveContainer.addView(circleLoadingView)
    }

    fun dismissTransparentLoading() {
        mAboveContainer.removeAllViews()
    }


    interface ReloadListener {
        fun onReload()
    }

    /**
     * @return 是否使用导航
     */
    open fun isUseTitleView(): Boolean {
        return true
    }

    open fun onCreateTitleView(): ICommonTitle? {
        return createSimple()
    }

    private fun createSimple(): SimpleTitleView? {
        return mActivity?.let { SimpleTitleView(it) }
    }

    fun setCustomTitle(title: CharSequence) {
        mTitleView?.setTitle(title)
    }

}