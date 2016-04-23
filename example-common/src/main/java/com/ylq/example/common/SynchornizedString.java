package com.ylq.example.common;

/**
 * Class description goes here
 * Created by ylq on 16/4/18 下午5:35.
 */
public class SynchornizedString extends Thread {

//    private static String a = "aaa";

    private static Object b;

    public SynchornizedString(Object b) {
        this.b = b;
    }

    public static void fangfa() {
        synchronized (b) {//b的引用对象发生改变，锁失效
            while (true) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(b);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        fangfa();
    }

    public static void main(String[] args) throws InterruptedException {
        SynchornizedString t1 = new SynchornizedString(new Object());
        t1.start();
        Thread.sleep(1000);
        SynchornizedString t2 = new SynchornizedString(new Object());
        t2.start();
    }
}
