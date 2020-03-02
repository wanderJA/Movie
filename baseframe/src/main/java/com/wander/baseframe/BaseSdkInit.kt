package com.wander.baseframe

import android.app.Application
import android.graphics.Bitmap
import com.facebook.common.logging.FLog
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.backends.pipeline.Fresco
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.wander.baseframe.context.AppContext
import com.wander.baseframe.library.fresco.MyBitmapMemoryCacheParams
import com.wander.baseframe.library.fresco.OkHttpImagePipelineConfigFactory
import com.wander.baseframe.utils.OkHttpUtils

/**
 * author wander
 * date 2019/7/14
 *
 */
object BaseSdkInit {
    fun initOnAppCreate(context: Application) {
        AppContext.init(context)
        AndroidUtilities.onCreate(context)
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)
//        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        initFresco(context)
    }

    private fun initFresco(context: Application) {
        //当内存紧张时采取的措施
        val memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance()
        memoryTrimmableRegistry.registerMemoryTrimmable { trimType ->
            val suggestedTrimRatio = trimType.suggestedTrimRatio
            if (MemoryTrimType.OnCloseToDalvikHeapLimit.suggestedTrimRatio == suggestedTrimRatio
                || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.suggestedTrimRatio == suggestedTrimRatio
                || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.suggestedTrimRatio == suggestedTrimRatio
            ) {
                //清除内存缓存
                Fresco.getImagePipeline().clearMemoryCaches()
            }
        }
        val config = OkHttpImagePipelineConfigFactory
            .newBuilder(context, OkHttpUtils.getClient())
            .setBitmapsConfig(Bitmap.Config.RGB_565)
            .setBitmapMemoryCacheParamsSupplier(MyBitmapMemoryCacheParams(context))
            .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
            .build()
        Fresco.initialize(context, config)
        FLog.setMinimumLoggingLevel(FLog.ASSERT)
    }
}