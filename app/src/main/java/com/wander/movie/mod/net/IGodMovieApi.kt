package com.wander.movie.mod.net

import com.wander.baseframe.utils.ParamMap
import com.wander.movie.bean.GodMovieList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IGodMovieApi {
    @GET("start")
    fun movieStart(@QueryMap params: ParamMap): Observable<GodResponseData<String>>

    @GET("infos")
    fun getListInfo(@QueryMap params: ParamMap): Observable<GodResponseData<GodMovieList>>
}