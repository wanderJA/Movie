package com.wander.movie.presenter

import com.wander.baseframe.mvp.BasePresenter
import com.wander.movie.bean.GodSearchKeyList
import com.wander.movie.mod.net.GodResponseData
import com.wander.movie.mod.net.MovieApi
import com.wander.movie.ui.iview.ISearchSuggest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchSuggestPresenter(mView: ISearchSuggest) : BasePresenter<ISearchSuggest>(mView) {
    var searchKey = ""
    fun loadData(searchKey: String) {
        this.searchKey = searchKey
        val searchKeyCall = MovieApi.godMovieSearchKey(searchKey)
        addCall(searchKeyCall)
        searchKeyCall.enqueue(object : Callback<GodResponseData<GodSearchKeyList>?> {
            override fun onFailure(call: Call<GodResponseData<GodSearchKeyList>?>, t: Throwable) {
                mView?.showError()
            }

            override fun onResponse(
                call: Call<GodResponseData<GodSearchKeyList>?>,
                response: Response<GodResponseData<GodSearchKeyList>?>
            ) {
                val kws = response.body()?.result?.kws
                if (kws?.isNotEmpty() == true) {
                    mView?.refreshList(kws)
                } else {
                    mView?.showError()
                }
            }
        })
    }
}