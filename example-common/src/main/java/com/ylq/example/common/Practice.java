package com.ylq.example.common;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {
    public static void main(String[] args) throws InterruptedException {
        Long now = System.currentTimeMillis();
        Thread threads[] = new Thread[100];
        for (int i = 0; i < threads.length; i++)  // 建立100个线程
            threads[i] = new TestThread();
        for (int i = 0; i < threads.length; i++)   // 运行刚才建立的100个线程
            threads[i].start();
        for (int i = 0; i < threads.length; i++)   // 100个线程都执行完后继续
            threads[i].join();
        System.out.println("n=" + TestThread.n);
        System.out.println(System.currentTimeMillis() - now);
    }
}
