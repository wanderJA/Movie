package com.wander.movie.ui.fragment

import android.graphics.Color
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.model.Response
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.baseframe.view.recycler.PullRefreshRecyclerView
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter
import com.wander.movie.R
import com.wander.movie.adapter.cell.CellNewsGank
import com.wander.movie.ui.gank.GankModel
import com.wander.movie.ui.gank.GankResponse
import com.wander.movie.ui.gank.NewsCallback
import kotlinx.android.synthetic.main.fragment_news_tab.*

class NewsTabFragment : BaseLayerFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_news_tab
    }

    override fun isUseTitleView() = false

    private var fragmentTitle = ""
    private var url = ""
    private var mAdapter = RVSimpleAdapter()
    private var currentPage = 0
    private val URL_GANK_BASE = "http://gank.io/api/data/"
    private var hasNext = true


    private val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

    override fun initLazyData() {
        url = "$URL_GANK_BASE$fragmentTitle/$PAGE_SIZE/"
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter

        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        refreshLayout.setOnRefreshListener(this)

        recyclerView.setOnScrollBottomListener(object :
            PullRefreshRecyclerView.OnScrollBottomListener {
            override fun onLoadMore() {
                if (mAdapter.canLoadMore()) {
                    if (hasNext) {
                        mAdapter.showLoadMore()
                        onLoadMoreRequested()
                    } else {
                        if (mAdapter.itemCount > 3) {
                            mAdapter.showBottom()
                        }
                    }
                }
            }
        })

        //开启loading,获取数据
        setRefreshing(true)
        onRefresh()
    }

    /** 下拉刷新  */
    override fun onRefresh() {
        OkGo.get<GankResponse<List<GankModel>>>(url + "1")
            .cacheKey("TabFragment_$fragmentTitle")       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
            .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
            .execute(object : NewsCallback<GankResponse<List<GankModel>>>() {
                override fun onSuccess(response: Response<GankResponse<List<GankModel>>>?) {
                    val results = response?.body()?.results
                    if (results != null) {
                        currentPage = 2
                        refreshList(results)
                    }
                    setRefreshing(false)
                }

                override fun onError(response: Response<GankResponse<List<GankModel>>>?) {
                    //网络请求失败的回调,一般会弹个Toast
                    showToast(response?.exception?.message)
                }

            })
    }

    /** 上拉加载  */
    fun onLoadMoreRequested() {
        OkGo.get<GankResponse<List<GankModel>>>(url + currentPage)
            .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
            .execute(object : NewsCallback<GankResponse<List<GankModel>>>() {
                override fun onSuccess(response: Response<GankResponse<List<GankModel>>>?) {
                    val results = response?.body()?.results
                    if (results != null && results.isNotEmpty()) {
                        currentPage++
                        refreshList(results, false)
                    } else {
                        //显示没有更多数据
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                }

                override fun onError(response: Response<GankResponse<List<GankModel>>>?) {
                    //显示数据加载失败,点击重试
                    //网络请求失败的回调,一般会弹个Toast
                    showToast(response?.exception?.message)
                }
            })
    }

    private fun refreshList(results: List<GankModel>, refresh: Boolean = true) {
        if (refresh) {
            mAdapter.clear()
            setRefreshing(true)
            recyclerView.firstPagerNeedLoad()
        }
        mAdapter.hideLoadMore()
        results.forEach {
            val cellNewsGank = CellNewsGank()
            cellNewsGank.data = it
            mAdapter.add(cellNewsGank)
        }
    }

    fun showToast(msg: String?) {
        Snackbar.make(recyclerView, msg ?: "null", Snackbar.LENGTH_SHORT).show()
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout.post(Runnable { refreshLayout.isRefreshing = refreshing })
    }

    fun setTitle(title: String) {
        fragmentTitle = title
    }

}
