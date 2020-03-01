package com.wander.movie.ui.fragment

import android.graphics.Color
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.model.Response
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.movie.R
import com.wander.movie.ui.gank.GankModel
import com.wander.movie.ui.gank.GankResponse
import com.wander.movie.ui.gank.NewsAdapter
import com.wander.movie.ui.gank.NewsCallback
import kotlinx.android.synthetic.main.fragment_news_tab.*

class NewsTabFragment : BaseLayerFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_news_tab
    }

    override fun isUseTitleView() = false

    private var fragmentTitle = ""
    private var url = ""
    private var newsAdapter: NewsAdapter = NewsAdapter()
    private var currentPage = 0
    val URL_GANK_BASE = "http://gank.io/api/data/"
    private var loading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

    protected fun initData() {
        url = URL_GANK_BASE + fragmentTitle + "/" + PAGE_SIZE + "/"
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = newsAdapter

        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        refreshLayout.setOnRefreshListener(this)

        recyclerView.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                var lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition >= layoutManager.itemCount - 3 && !loading) {
                    loading = true
                    onLoadMoreRequested()
                }
            }
        })

        //开启loading,获取数据
        setRefreshing(true)
        onRefresh()
    }

    /** 下拉刷新  */
    override fun onRefresh() {
        OkGo.get<GankResponse<List<GankModel>>>(url + "1")//
            .cacheKey("TabFragment_" + fragmentTitle)       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
            .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
            .execute(object : NewsCallback<GankResponse<List<GankModel>>>() {
                override fun onSuccess(response: Response<GankResponse<List<GankModel>>>?) {
                    val results = response?.body()?.results
                    if (results != null) {
                        currentPage = 2
                        newsAdapter.addData(results)
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
        OkGo.get<GankResponse<List<GankModel>>>(url + currentPage)//
            .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
            .execute(object : NewsCallback<GankResponse<List<GankModel>>>() {
                override fun onSuccess(response: Response<GankResponse<List<GankModel>>>?) {
                    val results = response?.body()?.results
                    if (results != null && results.isNotEmpty()) {
                        currentPage++
                        newsAdapter.addData(results)
                    } else {
                        //显示没有更多数据
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    Handler().postDelayed({ loading = false }, 500)

                }

                override fun onError(response: Response<GankResponse<List<GankModel>>>?) {
                    //显示数据加载失败,点击重试
                    //网络请求失败的回调,一般会弹个Toast
                    showToast(response?.exception?.message)
                }
            })
    }

    fun showToast(msg: String?) {
        Snackbar.make(recyclerView, msg ?: "null", Snackbar.LENGTH_SHORT).show()
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout.post(Runnable { refreshLayout.isRefreshing = refreshing })
    }


    fun getTitle(): String {
        return fragmentTitle
    }

    fun setTitle(title: String) {
        fragmentTitle = title
    }

}
