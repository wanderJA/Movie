package com.wander.baseframe.library.rxbus

/**
 * Created by wander on 2017/3/24.
 * 通知更新使用
 */

class MessageEvent {
    var code: Int = 0
    var msg: String

    constructor() {
        code = -1
        msg = "msg error"

    }

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }


}
