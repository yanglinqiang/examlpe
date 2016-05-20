package com.ylq.example.common;

/**
 * Class description goes here
 * Created by ylq on 16/4/18 下午5:35.
 */
public class SynchornizedString extends Thread {

    //    private static String a = "aaa";
    protected static volatile boolean inited = false;

    public SynchornizedString() {
    }

    public void fangfa() {
        if (!inited) {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            inited=true;
        }
    }

    public void run() {
        fangfa();
    }

    public static void main(String[] args) throws InterruptedException {
        SynchornizedString t1 = new SynchornizedString();
        t1.start();
        Thread.sleep(1000);
        SynchornizedString t2 = new SynchornizedString();
        t2.start();
    }
}
