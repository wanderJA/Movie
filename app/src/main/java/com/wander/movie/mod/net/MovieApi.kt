package com.wander.movie.mod.net

import com.wander.baseframe.BaseApp
import com.wander.baseframe.utils.DeviceTools
import com.wander.baseframe.utils.ParamMap
import com.wander.movie.bean.GodMovieDetail
import com.wander.movie.bean.GodMovieList
import com.wander.movie.bean.GodSearchKeyList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call

object MovieApi {

    val GodMovieApi = MovieRetrofit.apiRetrofit.createApi(IGodMovieApi::class.java)
    fun godMovieStart() {
        val paramMap = ParamMap()
        paramMap["deviceId"] = DeviceTools.getDeviceID(BaseApp.mApp)
        GodMovieApi.movieStart(paramMap).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    fun godMovieList(
        pageIndex: Int,
        type: String = "gndy"
    ): Observable<GodResponseData<GodMovieList>>? {
        val paramMap = ParamMap()
        paramMap["pagesize"] = "20"
        paramMap["pageindex"] = pageIndex.toString()
        paramMap["type"] = type
        paramMap["deviceid"] = DeviceTools.getDeviceID(BaseApp.mApp)
        return GodMovieApi.getListInfo(paramMap).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun godMovieDetail(
        id: String
    ): Observable<GodResponseData<GodMovieDetail>>? {
        val paramMap = ParamMap()
        paramMap["detailid"] = id
        return GodMovieApi.getGodMovieDetail(paramMap).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun godMovieSearchKey(key: String): Call<GodResponseData<GodSearchKeyList>> {
        val paramMap = ParamMap()
        paramMap["kw"] = key
        return GodMovieApi.getSearchKey(paramMap)
    }

    fun godMovieSearchResult(
        pageIndex: Int,
        key: String
    ): Observable<GodResponseData<GodMovieList>> {
        val paramMap = ParamMap()
        paramMap["pagesize"] = "20"
        paramMap["pageindex"] = pageIndex.toString()
        paramMap["searchKey"] = key
        return GodMovieApi.getSearchResult(paramMap).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}