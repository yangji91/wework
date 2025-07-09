package com.robot.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObsThreadPool {
    // 单例线程池，最多同时执行4个任务
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void shutdown() {
        executor.shutdown();
    }
}

