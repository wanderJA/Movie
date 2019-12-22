package com.wander.baseframe.library.rxbus

/**
 * Created by Android on 2016/6/8.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Subscribe(val tag: Int, val thread: EventThread = EventThread.MAIN_THREAD)
