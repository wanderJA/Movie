package com.wander.baseframe.component

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent

open class BaseActivity : AppCompatActivity() {
    protected lateinit var mActivity: BaseActivity
    private var umengCount = false
    private var umengName = "BaseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mActivity = this
    }

    open fun useRouterInject() = false

    open fun useEventBus() = false

    override fun onResume() {
        super.onResume()
        if (umengCount) {
            MobclickAgent.onPageStart(umengName)
        }
        MobclickAgent.onResume(this)          //统计时长
    }

    override fun onPause() {
        super.onPause()
        if (umengCount) {
            MobclickAgent.onPageEnd(umengName)
        }
        MobclickAgent.onPause(this)
    }

    /**
     * 统计直接由activity实现的页面
     * 在onCreate中调用
     *
     * @param name 自定义的activity名字
     */
    protected fun countThisActivity(name: String) {
        umengCount = true
        umengName = name
    }

}