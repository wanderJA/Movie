package com.wander.baseframe.component

import android.graphics.drawable.Drawable
import android.os.Build
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
import com.wander.baseframe.utils.ImmersionBar
import com.wander.baseframe.utils.ResourceUtils
import com.wander.baseframe.view.SimpleTitleView
import com.wander.baseframe.view.loading.CircleLoadingView
import kotlinx.android.synthetic.main.base_view_contain.*

/**
 * author wander
 * date 2018/11/5
 *
 */
abstract class BaseLayerActivity : BaseActivity() {
    lateinit var mAboveContainer: FrameLayout// 用于引导图实现
    lateinit var mContentContainer: FrameLayout// 用于显示内容
    lateinit var mStateViewContainer: FrameLayout// 用于显示各种加载状态的内容
    var mTitleView: ICommonTitle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.initBar(mActivity)
        setContentView(R.layout.base_view_contain)
        mAboveContainer = findViewById(R.id.above_container)
        mContentContainer = findViewById(R.id.content_container)
        mStateViewContainer = findViewById(R.id.stateView_container)

        val titleContainer = findViewById<FrameLayout>(R.id.bar_container)
        if (isUseTitleView()) {
            mTitleView = onCreateTitleView()
            if (mTitleView == null) {
                titleContainer.visibility = View.GONE
            } else {
                titleContainer.removeAllViews()
                mTitleView?.backView?.setOnClickListener { finish() }
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
    }

    abstract fun getLayoutId(): Int

    /**
     * 重写此方法来修改特定的背景
     *
     * @param drawable
     */
    @Suppress("DEPRECATION")
    protected fun setBaseBackground(drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            baseBg.background = drawable
        } else {
            baseBg.setBackgroundDrawable(drawable)
        }
    }

    /**
     * 重写此方法来修改特定的背景
     */
    protected fun setBaseBackground(@ColorRes color: Int) {
        if (color == 0) {
            //            baseBg.setBackgroundResource(R.mipmap.bg);
        } else {
            baseBg.setBackgroundColor(ResourceUtils.getColor(color))
        }
    }

//    private var lottieAnimationView: LottieAnimationView? = null
//    private var mStateLayout: ViewGroup? = null
//
//    protected fun showLoading() {
//        mStateViewContainer.removeAllViews()
//        if (lottieAnimationView == null) {
//            mStateLayout = FrameLayout(mContext)
//            lottieAnimationView = LottieAnimationView(mContext)
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            mStateViewContainer.background = null
//        } else {
//            mStateViewContainer.setBackgroundDrawable(null)
//        }
//    }

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
        circleLoadingView.setOnClickListener { }
        mAboveContainer.addView(circleLoadingView)

    }

    fun dismissTransparentLoading() {
        mAboveContainer.removeAllViews()
    }


    /**
     * 加载失败重试
     *
     * @param reloadListener 重新加载触发
     */
    protected fun showReload(
        charSequence: CharSequence, @DrawableRes imageRes: Int,
        reloadListener: ReloadListener?
    ) {
        mStateViewContainer.removeAllViews()
        val view = LayoutInflater.from(mActivity)
            .inflate(R.layout.reload_layout, mStateViewContainer, false)
        view.setOnClickListener { }
        view.findViewById<TextView>(R.id.reloadText).text = charSequence
        view.findViewById<ImageView>(R.id.reloadImg)
            .setImageDrawable(ResourceUtils.getDrawable(imageRes))
        view.findViewById<View>(R.id.reloadRefresh).setOnClickListener { _ ->
            reloadListener?.let {
                it.onReload()
                mStateViewContainer.removeAllViews()
            }
        }
        mStateViewContainer.addView(view)
    }

    /**
     * 加载失败重试,网络异常
     */
    protected fun showNetReload(reloadListener: ReloadListener) {
        showReload(getString(R.string.netReloadTip), R.drawable.ic_no_net, reloadListener)
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

    private fun createSimple(): SimpleTitleView {
        return SimpleTitleView(mActivity)
    }

    fun setCustomTitle(title: CharSequence) {
        mTitleView?.setTitle(title)
    }

}