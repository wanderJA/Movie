package com.wander.baseframe.exception


class ApiException(val code: Int, val msg: String, val url: String = "未设置地址信息") :
    RuntimeException("错误码：$code 错误信息：$msg 接口地址：$url")