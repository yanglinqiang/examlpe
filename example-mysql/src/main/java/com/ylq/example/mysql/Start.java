package com.ylq.example.mysql;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 杨林强 on 16/6/13.
 */
public class Start {
    public static void main(String[] args) throws InterruptedException {
        //测试数量
//        testNum();
        // 测试并发
        testRepeat();
    }

    private static void testRepeat() throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext
                .getBean("jdbcTemplate");
        long start = System.currentTimeMillis();
        System.out.println(new Date(start).toString());
        final String[] devices = new String[10];
        for (int i = 0; i < devices.length; i++) {
            devices[i] = UUID.randomUUID().toString().replace("-", "");
        }
        Thread[] threads = new Thread[24];
        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String device : devices) {
                        Map map = jdbcTemplate.queryForMap("call insert_device(?,?);", "device_offset_" + device.charAt(3), device);
                        System.out.println(Thread.currentThread().getName() + map);
                    }
                }
            });
            thread.start();
            threads[i] = thread;
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }


    private static void testNum() throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext
                .getBean("jdbcTemplate");
        long start = System.currentTimeMillis();
        System.out.println(new Date(start).toString());
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100000; i++) {
                        String device = UUID.randomUUID().toString().replace("-", "");
                        Map map = jdbcTemplate.queryForMap("call insert_device(?,?);", "device_offset_" + device.charAt(3), device);
                        if (i % 100 == 0) {
                            System.out.println(Thread.currentThread().getName() + map);
                        }
                    }
                }
            });
            thread.start();
            threads[i] = thread;
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }
}
