package com.wander.movie

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.umeng.commonsdk.UMConfigure
import com.wander.baseframe.BaseApp
import com.wander.baseframe.context.AppContext
import com.wander.movie.mod.ad.TTAdManagerHolder
import com.wander.movie.mod.net.OkGoUtils
import timber.log.Timber


/**
 * author wander
 * date 2019/7/14
 *
 */
class App : BaseApp() {
    companion object {
        var startTime: Long = 0
    }

    override fun onCreate() {
        super.onCreate()
        startTime = System.currentTimeMillis()
        AppContext.versionName = BuildConfig.VERSION_NAME

    }

    override fun createOnMainProcess() {
        super.createOnMainProcess()
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        initLog()
        OkGoUtils.initOkGo()
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }

        TTAdManagerHolder.init(this)
    }

    private fun initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}