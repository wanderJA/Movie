package com.wander.baseframe.view.recycler

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.abs
import kotlin.math.min

/**
 * Created by wander on 2017/3/16.
 */

class PullRefreshRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    /**
     * RecyclerView 最后可见Item在Adapter中的位置
     */
    private var mLastVisiblePosition = -1
    private var mLastY = -1f
    private var onScrollBottomListener: OnScrollBottomListener? = null
    private var onHeaderScroll: OnHeaderScroll? = null
    private var isBottom: Boolean = false
    private var mScrollTop = true
    private val isRefresh = false
    private var isLoadMore = false
    private var withRoomHeader = false
    private var roomHeader: IRoomHeader? = null
    /**
     * 监听滑动百分比的item
     */
    private val scrollPosition = 0
    private var animator: ValueAnimator? = null
    private val tag = "PullRefreshRecyclerView"
    /**
     * 滑动到底部第preLoadItem位置时预加载下一页
     */
    val preLoadItem = 2

    private val mRecyclerItemHeight = SparseIntArray()
    private var hasScroll: Boolean = false

    private
    val scrollDistance: Int
        get() {
            val layoutManager = layoutManager
            if (layoutManager is LinearLayoutManager) {
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                //this is the first visible row
                val c = layoutManager.getChildAt(0)
                if (c != null) {
                    var scrollY = -c.top
                    mRecyclerItemHeight.put(firstVisibleItemPosition, c.height)
                    //add all heights of the views that are gone
                    for (i in 0 until firstVisibleItemPosition) {
                        scrollY += mRecyclerItemHeight.get(i)
                    }
                    return scrollY
                } else {
                    return 0
                }
            } else {
                return 0
            }

        }

    private//判断RecyclerView 的ItemView是否满屏，如果不满一屏，上拉不会触发加载更多
    //最后一个Item了
    //阻尼加载 释放时判断
    val isScrollBottom: Boolean
        get() {
            val firstView = getChildAt(0) ?: return false
            val top = firstView.top
            val topEdge = paddingTop
            val isFullScreen = top < topEdge
            val itemCount = layoutManager?.itemCount ?: 0
            if (mLastVisiblePosition >= itemCount - preLoadItem && isFullScreen) {
                isBottom = true
                onScrollBottomListener?.onLoadMore()
            } else {
                isBottom = false
            }
            return isBottom

        }

    fun firstPagerNeedLoad() {
        val firstView = getChildAt(0)
        val top = firstView?.top ?: 0
        val topEdge = paddingTop
        val isFullScreen = top < topEdge
        onScrollBottomListener?.onLoadMore()
    }

    init {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLoadMore) {
                    val layoutManager = layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        mLastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    } else if (layoutManager is StaggeredGridLayoutManager) {
                        val staggeredGridLayoutManager =
                            layoutManager as StaggeredGridLayoutManager?
                        val lastPositions = IntArray(staggeredGridLayoutManager!!.spanCount)
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                        mLastVisiblePosition = findMax(lastPositions)
                    }
                }
                if (onHeaderScroll != null || withRoomHeader) {
                    calculateScroll()
                }
            }
        })

    }

    private fun calculateScroll() {
        val layoutManager = layoutManager
        if (layoutManager is LinearLayoutManager) {
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (firstVisibleItemPosition == scrollPosition) {
                val childAt = layoutManager.getChildAt(firstVisibleItemPosition)
                if (childAt != null) {
                    val scroll = abs(childAt.top)
                    //初始化会调用scroll，真正滑动之后处理
                    if (scroll == 0) {
                        mScrollTop = true
                        if (!hasScroll) {
                            return
                        }
                    } else {
                        mScrollTop = false
                    }
                    hasScroll = true
                    val childAtHeight = roomHeader?.getOrgHeight() ?: childAt.height

                    val percent = min(scroll * 1.0f / childAtHeight, 1.0f)
                    onHeaderScroll?.onScroll(scroll, percent)
                }
            }
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mLastY == -1f) {
                    mLastY = e.rawY
                }
                animator?.cancel()
                calculateScroll()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastY == -1f) {
                    mLastY = e.rawY
                }
                if (isLoadMore) {
                    isScrollBottom
                }
                if (withRoomHeader) {
                    val deltaY = e.rawY - mLastY
                    mLastY = e.rawY

                    if (mScrollTop) {
                        if (roomHeader?.hasRest() == true) {
                            if (deltaY > 0) {
                                //复位状态向下拉
                                roomHeader!!.onMove(deltaY / 3)
                                return true
                            }
                        } else {
                            roomHeader!!.onMove(deltaY / 3)
                            return true
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                mLastY = -1f // reset
                if (withRoomHeader && !roomHeader!!.hasRest()) {
                    resetRoomHeader()
                    roomHeader!!.reset()
                }
            }
            else -> {
            }
        }

        return super.onTouchEvent(e)
    }

    private fun resetRoomHeader() {
        val orgHeight = roomHeader!!.getOrgHeight()
        val curHeight = roomHeader!!.getCurHeight()

        animator = ObjectAnimator.ofInt(curHeight, orgHeight).setDuration(200)
        animator?.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            roomHeader!!.recoverHeight(animatedValue)
        }
        animator?.start()
    }

    /**
     * 默认滑倒最后一个item 开始加载更多
     *
     * @param onScrollBottomListener
     */
    fun setOnScrollBottomListener(onScrollBottomListener: OnScrollBottomListener) {
        this.onScrollBottomListener = onScrollBottomListener
        isLoadMore = true
    }

    fun setOnHeaderScroll(onHeaderScroll: OnHeaderScroll) {
        this.onHeaderScroll = onHeaderScroll
    }

    fun setRoomHeader(roomHeader: IRoomHeader) {
        this.roomHeader = roomHeader
        withRoomHeader = true
    }

    /**
     * 获取组数最大值
     *
     * @param lastPositions
     * @return
     */
    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    interface OnScrollBottomListener {
        fun onLoadMore()
    }

    interface OnHeaderScroll {
        /**
         * @param distance 滑动距离
         * @param percent  首个View滑动的百分比
         */
        fun onScroll(distance: Int, percent: Float)
    }
}
