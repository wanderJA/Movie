package com.wander.baseframe.library.rxbus

import com.wander.baseframe.utils.DebugLog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.lang.reflect.Method
import java.util.*

/**
 * Created by wander on 2017/4/26.
 * 已解决leaks问题
 */
class RxBus
/**
 * PublishSubject 创建一个可以在订阅之后把数据传输给订阅者Subject
 * SerializedSubject 序列化Subject为线程安全的Subject RxJava2 暂无
 */
private constructor() {

    //发布者
    private val bus: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    //存放订阅者信息
    private val subscriptions = HashMap<Any, CompositeDisposable>()

    /**
     * 发布事件
     *
     * @param code 订阅事件的code
     * @param obj 订阅事件的参数
     *
     * 订阅方法可选择是否接受参数
     */
    fun post(code: Int, obj: Any) {
        bus.onNext(Msg(code, obj))
    }

    /**
     * 发布事件
     * 只处理code
     *
     * 默认订阅事件的参数为 [MessageEvent]
     *
     * 订阅方法可选择是否接受参数
     * @param code 订阅事件的code
     */
    fun post(code: Int) {
        bus.onNext(Msg(code, MessageEvent(code, "")))
    }


    /**
     * 订阅者注册
     * 寻找父类的方法并且限制五层
     *
     * @param subscriber  订阅者对象
     */
    @Synchronized
    fun register(subscriber: Any) {
        val compositeDisposable = subscriptions[subscriber]
        if (compositeDisposable != null) {
            return
        }
        //使非public方法可以被invoke,并且关闭安全检查提升反射效率
        //方法必须被Subscribe注解
        var i = 0
        var obj: Class<*>? = subscriber.javaClass
        while (obj != null && i < 5) {
            for (method in obj.declaredMethods) {
                if (method.isAnnotationPresent(Subscribe::class.java)) {
                    method.isAccessible = true
                    addSubscription(method, subscriber)
                }
            }
            obj = obj.superclass
            i++
        }

    }

    /**
     * 添加订阅
     *
     * @param m          方法
     * @param subscriber 订阅者
     */
    private fun addSubscription(m: Method, subscriber: Any) {
        //获取方法内参数
        val parameterType = m.parameterTypes
        //只获取第一个方法参数，否则默认为Object
        var cla: Class<*> = Any::class.java
        var hasParam = true
        if (parameterType.isNotEmpty()) {
            cla = parameterType[0]
        } else {
            hasParam = false
        }
        //获取注解
        val sub = m.getAnnotation(Subscribe::class.java)
        //订阅事件
        val disposable = tObservable(sub.tag, cla)
            .observeOn(EventThread.getScheduler(sub.thread))
            .subscribe({ o ->
                if (hasParam) {
                    m.invoke(subscriber, o)
                } else {
                    m.invoke(subscriber)
                }
            }, { t -> t.printStackTrace() })
        putSubscriptionsData(m, subscriber, disposable)
    }

    /**
     * 订阅事件
     */
    private fun <T> tObservable(code: Int, eventType: Class<T>): Observable<T> {
        return bus.ofType(Msg::class.java)//判断接收事件类型
            .filter { msg -> msg.code == code }
            .map { msg -> msg.`object` }
            .cast(eventType)
    }

    /**
     * 添加订阅者到map空间来unRegister
     *
     * @param m 注册的方法
     * @param subscriber 订阅者
     * @param disposable 订阅者 Subscription
     */
    private fun putSubscriptionsData(m: Method, subscriber: Any, disposable: Disposable) {
        addDisposable(subscriptions, subscriber, disposable)
        DebugLog.d(TAG, "register:" + subscriber + "method:" + m.name)
    }

    private fun addDisposable(
        subscriptions: MutableMap<Any, CompositeDisposable>,
        subscriber: Any,
        disposable: Disposable
    ) {
        var subs: CompositeDisposable? = subscriptions[subscriber]
        if (subs == null) {
            subs = CompositeDisposable()
            subscriptions[subscriber] = subs
        }
        subs.add(disposable)
    }

    /**
     * 解除订阅者
     *
     * @param subscriber 订阅者
     */
    fun unRegister(subscriber: Any?) {
        if (subscriber == null) {
            throw NullPointerException("Object to unregister must not be null.")
        }
        val compositeDisposable = subscriptions[subscriber]
        if (compositeDisposable != null) {
            if (!compositeDisposable.isDisposed) {
                compositeDisposable.dispose()
            }
            subscriptions.remove(subscriber)
            DebugLog.d(TAG, "unRegister:$subscriber")
        }
    }

    companion object {
        fun getInstance(): RxBus {
            return RxbusHolder.holder
        }

        private const val TAG = "RxBus"
    }

    private object RxbusHolder {
        val holder = RxBus()
    }

}
