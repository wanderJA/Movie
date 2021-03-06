package com.wander.movie.ui.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.wander.baseframe.component.BaseLayerActivity
import com.wander.baseframe.ext.gone
import com.wander.baseframe.utils.AppUtils
import com.wander.baseframe.utils.ToastUtils
import com.wander.baseframe.utils.Tools
import com.wander.movie.R
import com.wander.movie.bean.GodMovieDetail
import com.wander.movie.mod.net.MovieApi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_god_movie_detail.*

class GodMovieDetailActivity : BaseLayerActivity() {
    var id = ""
    var name = "电影天堂"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id") ?: ""
        name = intent.getStringExtra("name") ?: name
        if (id.isEmpty()) {
            ToastUtils.showToast("id error")
            finish()
            return
        }
        initView()
        loadData()
    }

    @SuppressLint("AutoDispose")
    private fun loadData() {
        disposable = MovieApi.godMovieDetail(id)?.subscribe {
            fillView(it.result)

        }
    }

    private fun fillView(result: GodMovieDetail?) {
        result?.let {
            if (it.imgurls.isNotEmpty()) {
                godDetailPoster.hierarchy.setProgressBarImage(ProgressBarDrawable())
                godDetailPoster.setImageURI(it.imgurls[0])
            }
            godDetailContent.text = it.content
            if (it.downloadurls?.isNotEmpty() == true) {
                godDetailDownload.text = it.downloadurls?.get(0)
                val downloadUrl = godDetailDownload.text.toString()
                copy.setOnClickListener {
                    Tools.setClipBoardContent(downloadUrl)
                }
                jumpThund.setOnClickListener {
                    AppUtils.goApp(mActivity, Uri.parse(downloadUrl))
                }
            } else {
                copy.gone()
                jumpThund.gone()
            }

        }
    }

    var disposable: Disposable? = null

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun initView() {
        setCustomTitle(name)

    }

    override fun getLayoutId() = R.layout.activity_god_movie_detail
}
