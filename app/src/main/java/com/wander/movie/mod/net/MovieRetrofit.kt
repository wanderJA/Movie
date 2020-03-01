package com.wander.movie.mod.net

import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.safframework.http.interceptor.LoggingInterceptor
import com.wander.baseframe.BaseApp
import com.wander.baseframe.BuildConfig
import com.wander.baseframe.context.AppContext
import com.wander.baseframe.library.net.retrofit.ByteConvertFactory
import com.wander.baseframe.library.net.retrofit.RetryInterceptor
import com.wander.baseframe.library.net.retrofit.converter.DataConverterFactory
import com.wander.baseframe.utils.OkHttpUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class MovieRetrofit constructor(okHttpClient: OkHttpClient, baseUrl: String, isByte: Boolean) {

    companion object {
        var headerInterceptor: (() -> Interceptor)? = null
        val gson = GsonBuilder().create()
        var dataConverterFactory = DataConverterFactory.create(gson)
        private var mOkHttpClient: OkHttpClient? = null
        private val vipdHttpClient = getClient(true)
        const val API_HOST = "http://www.bytespace.cn"
        val apiRetrofit = createVipd(API_HOST)
        const val UNLOGIN = 402

        fun createVipd(baseUrl: String): MovieRetrofit {
            return MovieRetrofit(vipdHttpClient, baseUrl, false)
        }


        /**
         * @param needChuck 是否拦截到保存到数据库，开启gradle中isLog开关 加载chuck库
         * 线上默认加载chuck-no-op
         */
        private fun getClient(needChuck: Boolean): OkHttpClient {
            if (mOkHttpClient == null) {
                val okBuilder = OkHttpUtils.getBuilder()
                if (AppContext.isLog && needChuck) {
                    okBuilder.addInterceptor(ChuckInterceptor(BaseApp.getInstance()))
                }

                okBuilder.addInterceptor(RetryInterceptor())
                    .addInterceptor(ResponseInterceptor())
                    .addInterceptor(getLoggingInterceptor())
                mOkHttpClient = okBuilder.build()
                mOkHttpClient?.dispatcher?.maxRequestsPerHost = 16

            }
            return mOkHttpClient!!
        }

        private fun getLoggingInterceptor() = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .request()
            .requestTag("VipD-Request")
            .response()
            .responseTag("VipD-Response")
            .hideVerticalLine()// 隐藏竖线边框
            .build()

    }

    private var retrofit: Retrofit

    init {
        val factory: Converter.Factory
        if (isByte) {
            factory = ByteConvertFactory.create()
        } else {
            factory = dataConverterFactory
        }
        this.retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(factory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }


    fun <T> createApi(apiClass: Class<T>): T {
        return retrofit.create(apiClass)
    }
}