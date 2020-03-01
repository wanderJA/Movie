package com.wander.movie.mod.net

import com.google.gson.Gson
import com.wander.baseframe.exception.ApiException
import com.wander.baseframe.ext.fromJson
import com.wander.baseframe.library.net.retrofit.converter.ResponseData
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber


class ResponseInterceptor : Interceptor {
    @Throws(ApiException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        response.body?.run {
            contentType()?.let { contentType ->
                val body = string()
                val baseResp = MovieRetrofit.gson.fromJson<ResponseData<Any>>(body)
                if (baseResp.isSuc()) {
                    val responseBody =
                        ResponseBody.create(contentType, Gson().toJson(baseResp.data))
                    return response.newBuilder().body(responseBody).build()
                } else {
                    throw ApiException(baseResp.code?.run { toInt() } ?: let {
                        Timber.e("未返回结果码，返回内容：$body")
                        -99
                    }, baseResp.msg ?: "服务器异常：${baseResp.code}",
                        chain.request().url.toString()
                    )
                }
            }
        }
        return response
    }
}