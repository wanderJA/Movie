package com.wander.baseframe.utils;

import android.os.Process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luke on 2017/5/8.
 * 可以设置优先级的线程工厂类
 */
public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;
    private final String threadName;
    private final AtomicInteger mCount = new AtomicInteger(1);

    public PriorityThreadFactory(int threadPriority, String threadName) {
        this.mThreadPriority = threadPriority;
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                } catch (Throwable t) {
                    // just to be safe
                }
                runnable.run();
            }
        };
        return new Thread(wrapperRunnable, threadName + " #" + this.mCount.getAndIncrement());
    }

}