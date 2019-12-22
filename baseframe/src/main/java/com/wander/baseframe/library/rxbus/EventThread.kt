package com.wander.baseframe.library.rxbus

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wander
 */
enum class EventThread {
    /**
     * 主线程
     */
    MAIN_THREAD,
    /**
     * 新的线程
     */
    NEW_THREAD,
    /**
     * 读写线程
     */
    IO,
    /**
     * 计算工作默认线程
     */
    COMPUTATION,
    /**
     * 在当前线程中按照队列方式执行
     */
    TRAMPOLINE;


    companion object {

        fun getScheduler(threadMode: EventThread): Scheduler {
            return when (threadMode) {
                MAIN_THREAD -> AndroidSchedulers.mainThread()
                NEW_THREAD -> Schedulers.newThread()
                IO -> Schedulers.io()
                COMPUTATION -> Schedulers.computation()
                TRAMPOLINE -> Schedulers.trampoline()
            }
        }
    }
}
