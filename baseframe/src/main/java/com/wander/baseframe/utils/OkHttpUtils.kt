package com.wander.baseframe.utils

import okhttp3.OkHttpClient


object OkHttpUtils {
    private val okHttpClient = getBuilder().build()

    @JvmStatic
    fun getBuilder(): OkHttpClient.Builder {
        // 设置IPv6相关实现到OkHttp
        return OkHttpClient.Builder()
    }

    @JvmStatic
    fun getClient(): OkHttpClient {
        return okHttpClient
    }


}