package com.wander.movie.mod.net

import com.wander.baseframe.utils.ParamMap
import com.wander.movie.bean.GodMovieDetail
import com.wander.movie.bean.GodMovieList
import com.wander.movie.bean.GodSearchKeyList
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IGodMovieApi {
    @GET("start")
    fun movieStart(@QueryMap params: ParamMap): Observable<GodResponseData<String>>

    @GET("infos")
    fun getListInfo(@QueryMap params: ParamMap): Observable<GodResponseData<GodMovieList>>

    @GET("detail")
    fun getGodMovieDetail(@QueryMap params: ParamMap): Observable<GodResponseData<GodMovieDetail>>

    @GET("keys")
    fun getSearchKey(@QueryMap params: ParamMap): Call<GodResponseData<GodSearchKeyList>>

    @GET("search")
    fun getSearchResult(@QueryMap params: ParamMap): Observable<GodResponseData<GodMovieList>>
}