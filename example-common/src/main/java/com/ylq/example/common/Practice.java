package com.ylq.example.common;

import java.io.IOException;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {
    public static void main(String[] args) throws InterruptedException, IOException {
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("sdf");
//            }
//        }, 1, 1, TimeUnit.MINUTES);
//
//        Thread.sleep(100000000);
        Integer i=0;
        Integer j=i++;
        System.out.println(j);
    }
}
