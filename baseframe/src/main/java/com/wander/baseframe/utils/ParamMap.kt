package com.wander.baseframe.utils

import java.util.*

/**
 * Retrofit 传参使用， 避免出现value为null，否则retrofit会抛异常
 */

class ParamMap : HashMap<String, String?>() {

    override fun put(key: String, value: String?): String? {
        var realValue = value
        if (realValue == null) {
            realValue = ""
        }
        return super.put(key, realValue)
    }

}
