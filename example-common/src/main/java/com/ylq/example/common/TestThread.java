package com.ylq.example.common;

/**
 * Class description goes here
 * Created by ylq on 16/4/16 下午3:53.
 */
public class TestThread extends Thread {
    public static int n = 0;
//    static ReentrantLock reentrantLock = new ReentrantLock();

    static synchronized void inc() {
//        reentrantLock.lock();
        n++;
//        reentrantLock.unlock();
    }

    public void run() {
        for (int i = 0; i < 10; i++)
            try {
                inc();
                sleep(3);  // 为了使运行结果更随机，延迟3毫秒

            } catch (Exception e) {
            }
    }

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
