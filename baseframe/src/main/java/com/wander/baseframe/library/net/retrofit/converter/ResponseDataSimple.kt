package com.wander.baseframe.library.net.retrofit.converter

import java.io.Serializable

/**
 * author wander
 * date 2018/8/30
 *
 */
class ResponseDataSimple : Serializable {
    var msg: String? = null

    var code: String? = null

    fun toResponseData(): ResponseData<String> {
        val response = ResponseData<String>()
        response.code = code
        response.msg = msg
        return response
    }

    fun isSuc(): Boolean {
        return code == "0"
    }
}