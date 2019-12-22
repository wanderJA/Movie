package com.wander.baseframe.library.fresco

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.facebook.common.internal.Supplier
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.cache.MemoryCacheParams

/**
 * author wangdou
 * date 2018/8/22
 * @see com.facebook.imagepipeline.cache.DefaultBitmapMemoryCacheParamsSupplier
 */
class ReadBitmapMemoryCacheParams(var mContext: Context) : Supplier<MemoryCacheParams> {
    private val MAX_CACHE_ENTRIES = 256
    private val MAX_EVICTION_QUEUE_SIZE = Integer.MAX_VALUE
    private val MAX_EVICTION_QUEUE_ENTRIES = Integer.MAX_VALUE
    private val MAX_CACHE_ENTRY_SIZE = Integer.MAX_VALUE


    override fun get(): MemoryCacheParams {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return MemoryCacheParams(
                getMaxCacheSize(),
                128,
                MAX_EVICTION_QUEUE_SIZE,
                MAX_EVICTION_QUEUE_ENTRIES,
                MAX_CACHE_ENTRY_SIZE
            )
        }
        return MemoryCacheParams(
            getMaxCacheSize(),
            MAX_CACHE_ENTRIES,
            MAX_EVICTION_QUEUE_SIZE,
            MAX_EVICTION_QUEUE_ENTRIES,
            MAX_CACHE_ENTRY_SIZE
        )
    }

    fun getMaxCacheSize(): Int {
        val activityManager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val maxMemory = Math.min(activityManager.memoryClass * ByteConstants.MB, Integer.MAX_VALUE)
        return if (maxMemory < 32 * ByteConstants.MB) {
            4 * ByteConstants.MB
        } else if (maxMemory < 64 * ByteConstants.MB) {
            6 * ByteConstants.MB
        } else {
            // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
            // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
            maxMemory / 8
        }
    }

}