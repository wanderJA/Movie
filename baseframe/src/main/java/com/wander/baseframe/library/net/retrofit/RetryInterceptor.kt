package com.wander.baseframe.library.net.retrofit


import com.wander.baseframe.utils.DebugLog
import com.wander.baseframe.utils.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class RetryInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // 当前在网络请求的子线程中
        var response: Response? = null
        var retryCount = 0
        var retry = false
        val url = request.url.toString()
        do {
            //            Log.w(TAG , "retryCount-->" + retryCount);
            try {
                val response = chain.proceed(request)
                // no Exception
                retry = shouldRetry(retryCount++, response, null)
                return response

            } catch (e: ConnectException) {
                DebugLog.d(TAG, "ConnectException : $e")
                retry = shouldRetry(retryCount++, response, e)
                if (!retry) {
                    throw e
                }
            } catch (e: SocketException) {
                DebugLog.d(TAG, "SocketException : $e")
                retry = shouldRetry(retryCount++, response, e)
                if (!retry) {
                    throw e
                }
            } catch (e: UnknownHostException) {
                DebugLog.d(TAG, "UnknownHostException : $e")
                retry = shouldRetry(retryCount++, response, e)
                if (!retry) {
                    throw e
                }
            } catch (e: SocketTimeoutException) {
                DebugLog.d(TAG, "SocketTimeoutException : $e")
                retry = shouldRetry(retryCount++, response, e)
                if (!retry) {
                    throw e
                }
            } catch (e: Exception) {
                DebugLog.d(TAG, "Exception : $e")
                throw e
            }

            if (retry) {
                DebugLog.d(TAG, "RetryInterceptor tryCount: $retryCount")

                try {
                    Thread.sleep(RETRY_WAIT_TIME.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        } while (retry)
        return chain.proceed(request)
    }

    companion object {

        private val RETRY_COUNT = 3
        private val RETRY_WAIT_TIME = 1000

        val ERROR_CODE_CONNECT_EXCEPTION = 1001
        val ERROR_CODE_SOCKET_EXCEPTION = 1002
        val ERROR_CODE_UNKNOWN_HOST_EXCEPTION = 1003

        val TAG = "RetryInterceptor"

        // 默认网络不可用的情况下不会重试，如果需要重试的话需，重载该函数。
        protected fun shouldRetry(
            retryTimes: Int,
            response: Response?,
            e: Exception?
        ): Boolean {
            if (retryTimes >= RETRY_COUNT) {
                return false
            }

            if (response != null) {
                if (response.isSuccessful) {
                    return false
                } else {
                    DebugLog.d(
                        TAG,
                        "response.isSuccessful()? : " + response.isSuccessful + " code " + response.code
                    )
                }
            }
            return NetworkUtil.isNetworkAvailable
        }
    }


}
