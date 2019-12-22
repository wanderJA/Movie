package com.wander.baseframe.library.net.retrofit.converter

import java.io.Serializable

open class ResponseData<T> : Serializable {

    private var hasCheck = false
    var data: T? = null
        get() {
            if (!hasCheck) {
                checkCode()
                hasCheck = true
            }
            return field
        }

    var msg: String? = null


    open var code: String? = ""
        set(value) {
            field = value
            checkCode()
        }

    open fun checkCode() {

    }

    override fun toString(): String {
        return "ResponseData{" +
                "data=" + data +
                ", msg='" + msg + '\''.toString() +
                ", code='" + code + '\''.toString() +
                '}'.toString()
    }

    fun isSuc(): Boolean {
        checkCode()
        return code == "0"
    }
}