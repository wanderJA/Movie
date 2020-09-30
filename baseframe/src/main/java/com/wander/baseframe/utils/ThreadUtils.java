package com.wander.baseframe.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程工具类
 * <p>
 * 使用该类时，需要注意区分各个线程池的作用，尽量使用优先级低的线程池
 */

public class ThreadUtils {

    //串行发送埋点信息
    private static DispatchQueue pingbackQueue = new DispatchQueue("pingbackQueue");

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int KEEP_ALIVE_SECONDS = 10;

    //后台线程池：负责优先级较低的任务（如与UI交互无关的工作，用户不可见，不感知的工作），后台优先级
    private static final Executor mBackgroundExecutor;

    //网络请求线程池：负责网络请求专用线程池，一般来说网络请求会与UI相关，所以优先级较高，如果是优先级较低也可使用后台线程池
    private static final Executor mNetWorkExecutor;
    //立即执行线程池：线程池没有大小，所有加入的线程都会立即执行(谨慎使用)
    private static final ExecutorService mImmediateExecutor;

    static {
        mBackgroundExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE, new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND, "BackgroundExecutor"));

        mNetWorkExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE, new PriorityThreadFactory(Process.THREAD_PRIORITY_DEFAULT, "NetWorkExecutor"));

        mImmediateExecutor = Executors.newCachedThreadPool(new PriorityThreadFactory(Process.THREAD_PRIORITY_DEFAULT,
                "ImmediateExecutor"));

    }

    public static Executor getBackgroundExecutor() {
        return mBackgroundExecutor;
    }

    public static Executor getNetWorkExecutor() {
        return mNetWorkExecutor;
    }

    public static ExecutorService getImmediateExecutor() {
        return mImmediateExecutor;
    }

    public static ExecutorService getSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static DispatchQueue getPingbackQueue() {
        return pingbackQueue;
    }

    public static boolean isMainProcess(Application application) {
        try {
            ActivityManager am = ((ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            String mainProcessName = application.getPackageName();
            int myPid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

