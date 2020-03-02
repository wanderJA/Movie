package com.wander.movie.mod.net

import com.wander.baseframe.BaseApp
import com.wander.baseframe.utils.DeviceTools
import com.wander.baseframe.utils.ParamMap
import com.wander.movie.bean.GodMovieList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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


}