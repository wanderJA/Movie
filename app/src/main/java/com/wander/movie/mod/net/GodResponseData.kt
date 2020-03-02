package com.wander.movie.mod.net

import java.io.Serializable

open class GodResponseData<T> : Serializable {

    private var hasCheck = false
    var result: T? = null
        get() {
            if (!hasCheck) {
                checkCode()
                hasCheck = true
            }
            return field
        }

    var message: String? = null


    open var returncode: String? = ""
        set(value) {
            field = value
            checkCode()
        }

    open fun checkCode() {

    }

    override fun toString(): String {
        return "ResponseData{" +
                "data=" + result +
                ", msg='" + message + '\''.toString() +
                ", code='" + returncode + '\''.toString() +
                '}'.toString()
    }

    fun isSuc(): Boolean {
        checkCode()
        return returncode == "0"
    }
}