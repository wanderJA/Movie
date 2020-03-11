package com.wander.movie.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wander.baseframe.component.CommonWebFragment
import com.wander.baseframe.component.ContainActivity
import com.wander.movie.ui.activity.GodMovieDetailActivity
import com.wander.movie.ui.fragment.MovieSearchFragment

object JumpUtils {

    fun jumpToWeb(context: Context, url: String, title: String? = null) {
        val params = Bundle()
        params.putString(CommonWebFragment.WEB_URL, url)
        params.putString(CommonWebFragment.WEB_TITLE, title)
        ContainActivity.start(context, CommonWebFragment::class.java, params)
    }

    fun jumpToGodMovieDetail(
        context: Context,
        id: String,
        name: String
    ) {
        val intent = Intent(context, GodMovieDetailActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("name", name)
        context.startActivity(intent)

    }

    fun jumpToMovieSearch(mActivity: Context) {
        ContainActivity.start(mActivity, MovieSearchFragment::class.java)
    }
}